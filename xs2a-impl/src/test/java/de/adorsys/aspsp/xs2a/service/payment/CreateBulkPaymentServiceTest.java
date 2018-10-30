/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.aspsp.xs2a.service.payment;

import de.adorsys.aspsp.xs2a.domain.*;
import de.adorsys.aspsp.xs2a.domain.account.Xs2aAccountReference;
import de.adorsys.aspsp.xs2a.domain.consent.Xs2aPisConsent;
import de.adorsys.aspsp.xs2a.domain.pis.BulkPayment;
import de.adorsys.aspsp.xs2a.domain.pis.BulkPaymentInitiationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitiationParameters;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayment;
import de.adorsys.aspsp.xs2a.service.authorization.AuthorisationMethodService;
import de.adorsys.aspsp.xs2a.service.authorization.pis.PisScaAuthorisationService;
import de.adorsys.aspsp.xs2a.service.consent.Xs2aPisConsentService;
import de.adorsys.psd2.xs2a.core.profile.PaymentProduct;
import de.adorsys.psd2.xs2a.core.profile.PaymentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus.RCVD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateBulkPaymentServiceTest {
    private final Currency EUR_CURRENCY = Currency.getInstance("EUR");
    private static final String CONSENT_ID = "d6cb50e5-bb88-4bbf-a5c1-42ee1ed1df2c";
    private static final String PAYMENT_ID = "12345";
    private static final String IBAN = "DE123456789";
    private final TppInfo TPP_INFO = buildTppInfo();

    @InjectMocks
    private CreateBulkPaymentService createBulkPaymentService;
    @Mock
    private ScaPaymentService scaPaymentService;
    @Mock
    private Xs2aPisConsentService pisConsentService;
    @Mock
    private AuthorisationMethodService authorisationMethodService;
    @Mock
    private PisScaAuthorisationService pisScaAuthorisationService;

    @Before
    public void init() {
        when(scaPaymentService.createBulkPayment(buildBulkPayment(), TPP_INFO, PaymentProduct.SEPA, buildXs2aPisConsent())).thenReturn(buildBulkPaymentInitiationResponse());
    }

    @Test
    public void success_initiate_single_payment() {
        //When
        ResponseObject<BulkPaymentInitiationResponse> actualResponse = createBulkPaymentService.createPayment(buildBulkPayment(), buildPaymentInitiationParameters(), buildTppInfo(), buildXs2aPisConsent());

        //Then
        assertThat(actualResponse.hasError()).isFalse();
        assertThat(actualResponse.getBody().getPaymentId()).isEqualTo(PAYMENT_ID);
        assertThat(actualResponse.getBody().getTransactionStatus()).isEqualTo(RCVD);
    }

    private BulkPayment buildBulkPayment() {
        BulkPayment payment = new BulkPayment();
        payment.setPayments(buildListSinglePayment());
        payment.setDebtorAccount(buildReference());
        payment.setTransactionStatus(Xs2aTransactionStatus.RCVD);
        return payment;
    }

    private List<SinglePayment> buildListSinglePayment() {
        List<SinglePayment> list = new ArrayList<>();
        SinglePayment payment = new SinglePayment();
        Xs2aAmount amount = buildXs2aAmount();
        payment.setInstructedAmount(amount);
        payment.setDebtorAccount(buildReference());
        payment.setCreditorAccount(buildReference());
        payment.setTransactionStatus(Xs2aTransactionStatus.RCVD);
        list.add(payment);
        return list;
    }

    private Xs2aAmount buildXs2aAmount() {
        Xs2aAmount amount = new Xs2aAmount();
        amount.setCurrency(EUR_CURRENCY);
        amount.setAmount("100");
        return amount;
    }

    private Xs2aAccountReference buildReference() {
        Xs2aAccountReference reference = new Xs2aAccountReference();
        reference.setIban(IBAN);
        reference.setCurrency(EUR_CURRENCY);
        return reference;
    }

    private Xs2aPisConsent buildXs2aPisConsent() {
        return new Xs2aPisConsent(CONSENT_ID);
    }

    private PaymentInitiationParameters buildPaymentInitiationParameters() {
        PaymentInitiationParameters parameters = new PaymentInitiationParameters();
        parameters.setPaymentProduct(PaymentProduct.SEPA);
        parameters.setPaymentType(PaymentType.BULK);
        return parameters;
    }

    private BulkPaymentInitiationResponse buildBulkPaymentInitiationResponse() {
        BulkPaymentInitiationResponse response = new BulkPaymentInitiationResponse();
        response.setPaymentId(PAYMENT_ID);
        response.setTransactionStatus(Xs2aTransactionStatus.RCVD);
        response.setPisConsentId(CONSENT_ID);
        return response;
    }

    private TppInfo buildTppInfo() {
        TppInfo tppInfo = new TppInfo();
        tppInfo.setAuthorisationNumber("registrationNumber");
        tppInfo.setTppName("tppName");
        tppInfo.setTppRoles(Collections.singletonList(Xs2aTppRole.PISP));
        tppInfo.setAuthorityId("authorityId");
        return tppInfo;
    }
}
