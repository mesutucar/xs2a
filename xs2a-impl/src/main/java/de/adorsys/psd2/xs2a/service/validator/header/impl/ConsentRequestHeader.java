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

package de.adorsys.psd2.xs2a.service.validator.header.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Consent request header", value = "ConsentRequestHeader")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsentRequestHeader extends CommonRequestHeader {

    @ApiModelProperty(value = "Might be mandated in the ASPSP's documentation, if OAuth is not chosen as Pre-Step", required = false, example = "5845f9b0-cef6-4f2d-97e0-ed1e0469a907")
    @JsonProperty(value = "psu-id")
    private String psuId;

    @ApiModelProperty(value = "Type of the PSU-ID, needed in scenarios where PSUs have several PSU-IDs as access possibility.", required = false, example = "5845f9b0-cef6-4f2d-97e0-ed1e0469a907")
    @JsonProperty(value = "psu-id-type")
    private String psuIdType;

    @ApiModelProperty(value = "Might be mandated in the ASPSP's documentation. Only used in a corporate context", required = false, example = "5845f9b0-cef6-4f2d-97e0-ed1e0469a907")
    @JsonProperty(value = "psu-corporate-id")
    private String psuCorporateId;

    @ApiModelProperty(value = "Might be mandated in the ASPSP's documentation. Only used in a corporate context", required = false, example = "5845f9b0-cef6-4f2d-97e0-ed1e0469a907")
    @JsonProperty(value = "psu-corporate-id-type")
    private String psuCorporateIdType;

    @ApiModelProperty(value = "URI of the TPP, where the transaction flow shall be redirected to after a Redirect. Shall be contained at least if the tppRedirectPreferred parameter is set to true or is missing.", required = false, example = "5845f9b0-cef6-4f2d-97e0-ed1e0469a907")
    @JsonProperty(value = "tpp-redirect-uri")
    private String tppRedirectUri;
}
