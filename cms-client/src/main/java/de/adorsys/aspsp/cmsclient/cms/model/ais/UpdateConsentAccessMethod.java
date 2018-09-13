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

package de.adorsys.aspsp.cmsclient.cms.model.ais;

import de.adorsys.aspsp.cmsclient.cms.RestCmsRequestMethod;
import de.adorsys.aspsp.cmsclient.core.HttpMethod;
import de.adorsys.aspsp.cmsclient.core.util.HttpUriParams;
import de.adorsys.aspsp.xs2a.consent.api.ais.AisAccountAccessInfo;
import de.adorsys.aspsp.xs2a.consent.api.ais.CreateAisConsentResponse;

public class UpdateConsentAccessMethod extends RestCmsRequestMethod<AisAccountAccessInfo, CreateAisConsentResponse> {
    private static final String UPDATE_AIS_CONSENT_ACCESS_URI = "api/v1/ais/consent/{consent-id}/access";

    public UpdateConsentAccessMethod(final AisAccountAccessInfo accessInfo, HttpUriParams uriParams) {
        super(accessInfo, HttpMethod.PUT, UPDATE_AIS_CONSENT_ACCESS_URI, uriParams);
    }
}
