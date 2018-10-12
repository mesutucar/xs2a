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

package de.adorsys.aspsp.xs2a.service.authorization.pis;

import de.adorsys.aspsp.xs2a.domain.MessageErrorCode;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.TppInfo;
import de.adorsys.aspsp.xs2a.domain.consent.Xsa2CreatePisConsentAuthorisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.*;
import de.adorsys.aspsp.xs2a.exception.MessageError;
import de.adorsys.aspsp.xs2a.service.authorization.AuthorisationMethodService;
import de.adorsys.aspsp.xs2a.service.consent.PisConsentService;
import de.adorsys.aspsp.xs2a.service.payment.ScaPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateSinglePaymentService implements CreatePaymentService<SinglePaymentInitiateResponse, SinglePayment> {
    private final ScaPaymentService scaPaymentService;
    private final PisConsentService pisConsentService;
    private final PisScaAuthorisationService pisScaAuthorisationService;
    private final AuthorisationMethodService authorisationMethodService;

    /**
     * Initiates a single payment
     *
     * @param payment        Single payment information
     * @param paymentProduct The addressed payment product
     * @return Response containing information about created single payment or corresponding error
     */
    @Override
    public ResponseObject<SinglePaymentInitiateResponse> createPayment(SinglePayment payment, PaymentProduct paymentProduct, boolean tppExplicitAuthorisationPreferred, String consentId, TppInfo tppInfo) {
        SinglePaymentInitiateResponse response = scaPaymentService.createSinglePayment(payment, tppInfo, paymentProduct);
        response.setPisConsentId(consentId);

        updateSinglePaymentInPisConsent(payment, paymentProduct, consentId, response);

        boolean implicitMethod = authorisationMethodService.isImplicitMethod(tppExplicitAuthorisationPreferred);
        if (implicitMethod) {
            Optional<Xsa2CreatePisConsentAuthorisationResponse> consentAuthorisation = pisScaAuthorisationService.createConsentAuthorisation(response.getPaymentId(), PaymentType.SINGLE);
            if (!consentAuthorisation.isPresent()) {
                return ResponseObject.<SinglePaymentInitiateResponse>builder()
                           .fail(new MessageError(MessageErrorCode.CONSENT_INVALID))
                           .build();
            }
            Xsa2CreatePisConsentAuthorisationResponse authorisationResponse = consentAuthorisation.get();
            response.setAuthorizationId(authorisationResponse.getAuthorizationId());
            response.setScaStatus(authorisationResponse.getScaStatus());
        }
        return ResponseObject.<SinglePaymentInitiateResponse>builder()
                   .body(response)
                   .build();
    }

    private void updateSinglePaymentInPisConsent(SinglePayment payment, PaymentProduct paymentProduct, String consentId, SinglePaymentInitiateResponse response) {
        payment.setPaymentId(response.getPaymentId());
        payment.setTransactionStatus(response.getTransactionStatus());

        pisConsentService.updateSinglePaymentInPisConsent(payment, paymentProduct, consentId);
    }
}