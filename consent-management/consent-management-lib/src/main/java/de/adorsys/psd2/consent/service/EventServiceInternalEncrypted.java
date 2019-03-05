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

package de.adorsys.psd2.consent.service;

import de.adorsys.psd2.consent.api.service.EventService;
import de.adorsys.psd2.consent.api.service.EventServiceEncrypted;
import de.adorsys.psd2.consent.service.security.SecurityDataService;
import de.adorsys.psd2.xs2a.core.event.Event;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceInternalEncrypted implements EventServiceEncrypted {
    private final SecurityDataService securityDataService;
    private final EventService eventService;

    @Override
    @Transactional
    public boolean recordEvent(@NotNull Event event) {
        String decryptedConsentId = decryptId(event.getConsentId());
        String decryptedPaymentId = decryptId(event.getPaymentId());

        Event decryptedEvent = Event.builder()
                                   .timestamp(event.getTimestamp())
                                   .consentId(decryptedConsentId)
                                   .paymentId(decryptedPaymentId)
                                   .payload(event.getPayload())
                                   .eventOrigin(event.getEventOrigin())
                                   .eventType(event.getEventType())
                                   .psuId(event.getPsuId())
                                   .psuCorporateId(event.getPsuCorporateId())
                                   .authorisationNumber(event.getAuthorisationNumber())
                                   .requestId(event.getRequestId())
                                   .build();
        return eventService.recordEvent(decryptedEvent);
    }

    private String decryptId(String id) {
        return Optional.ofNullable(id)
                   .flatMap(securityDataService::decryptId)
                   .orElse(null);
    }
}
