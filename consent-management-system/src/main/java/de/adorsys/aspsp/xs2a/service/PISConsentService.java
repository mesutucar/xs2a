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

package de.adorsys.aspsp.xs2a.service;

import de.adorsys.aspsp.xs2a.consent.api.ConsentStatus;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.PisConsentRequest;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.PisConsentResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PisConsent;
import de.adorsys.aspsp.xs2a.repository.PisConsentRepository;
import de.adorsys.aspsp.xs2a.service.mapper.PISConsentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PISConsentService {
    private final PisConsentRepository pisConsentRepository;
    private final PISConsentMapper pisConsentMapper;

    public Optional<String> createPaymentConsent(PisConsentRequest request) {
        return pisConsentMapper.mapToPisConsent(request)
                   .map(pisConsentRepository::save)
                   .map(PisConsent::getExternalId);
    }

    public Optional<ConsentStatus> getConsentStatusById(String consentId) {
        return getPisConsentById(consentId)
                   .map(PisConsent::getConsentStatus);
    }

    public Optional<PisConsentResponse> getConsentById(String consentId) {
        return getPisConsentById(consentId)
                   .flatMap(pisConsentMapper::mapToPisConsentResponse);
    }

    public Optional<Boolean> updateConsentStatusById(String consentId, ConsentStatus status) {
        return getPisConsentById(consentId)
                   .map(con -> setStatusAndSaveConsent(con, status))
                   .map(con -> con.getConsentStatus() == status);
    }

    private Optional<PisConsent> getPisConsentById(String consentId) {
        return Optional.ofNullable(consentId)
                   .flatMap(pisConsentRepository::findByExternalId);
    }

    private PisConsent setStatusAndSaveConsent(PisConsent consent, ConsentStatus status) {
        consent.setConsentStatus(status);
        return pisConsentRepository.save(consent);
    }
}
