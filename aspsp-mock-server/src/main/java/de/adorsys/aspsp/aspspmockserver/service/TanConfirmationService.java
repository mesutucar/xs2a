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

package de.adorsys.aspsp.aspspmockserver.service;

import de.adorsys.aspsp.aspspmockserver.repository.PsuRepository;
import de.adorsys.aspsp.aspspmockserver.repository.TanRepository;
import de.adorsys.aspsp.xs2a.spi.domain.psu.Tan;
import de.adorsys.aspsp.xs2a.spi.domain.psu.TanStatus;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TanConfirmationService {
    private final TanRepository tanRepository;
    private final PsuRepository psuRepository;
    private final JavaMailSender emailSender;
    private final AccountService accountService;

    public boolean generateAndSendTanForPsuByIban(String iban) {
        return accountService.getPsuIdByIban(iban)
                   .map(this::generateAndSendTanForPsu)
                   .orElse(false);
    }

    public boolean generateAndSendTanForPsu(String psuId) {
        return Optional.ofNullable(psuRepository.findOne(psuId))
                   .map(psu -> createAndSendTan(psu.getId(), psu.getEmail()))
                   .orElse(false);
    }

    public boolean isTanNumberValidByIban(String iban, String tanNumber) {
        return accountService.getPsuIdByIban(iban)
                   .map(psuId-> isPsuTanNumberValid(psuId, tanNumber))
                   .orElse(false);
    }

    public boolean isPsuTanNumberValid(String psuId, String tanNumber) {
        return tanRepository.findByPsuIdAndTanStatus(psuId, TanStatus.UNUSED)
                   .map(t -> validateTanAndUpdateTanStatus(t, tanNumber))
                   .orElse(false);
    }

    private boolean createAndSendTan(String psuId, String email) {
        Tan tan = new Tan(psuId, generateTanNumber());

        return Optional.ofNullable(tanRepository.save(tan))
                   .map(t -> sendTanNumberOnEmail(email, t.getTanNumber()))
                   .orElse(false);
    }

    private boolean validateTanAndUpdateTanStatus(Tan originalTan, String givenTanNumber) {
        boolean isValid = originalTan.getTanNumber().equals(givenTanNumber);
        if (isValid) {
            originalTan.setTanStatus(TanStatus.VALID);
        } else {
            originalTan.setTanStatus(TanStatus.INVALID);
        }
        tanRepository.save(originalTan);

        return isValid;
    }

    private String generateTanNumber() {
        return RandomStringUtils.random(6, true, true);
    }

    private boolean sendTanNumberOnEmail(String email, String tanNumber) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject("Your TAN for payment confirmation");
        mail.setFrom(email);
        mail.setTo(email);
        mail.setText("Your TAN number is " + tanNumber);
        emailSender.send(mail);
        return true;
    }
}
