/*
 * Copyright 2018-2019 adorsys GmbH & Co KG
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

package de.adorsys.psd2.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Links to the account, which can be directly used for retrieving account information from this dedicated account.  Links to \&quot;balances\&quot; and/or \&quot;transactions\&quot;  These links are only supported, when the corresponding consent has been already granted. 
 */
@ApiModel(description = "Links to the account, which can be directly used for retrieving account information from this dedicated account.  Links to \"balances\" and/or \"transactions\"  These links are only supported, when the corresponding consent has been already granted. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-01-11T12:48:04.675377+02:00[Europe/Kiev]")

public class LinksAccountDetails extends HashMap<String, String>  {
  @JsonProperty("balances")
  private String balances = null;

  @JsonProperty("transactions")
  private String transactions = null;

  public LinksAccountDetails balances(String balances) {
    this.balances = balances;
    return this;
  }

  /**
   * Get balances
   * @return balances
  **/
  @ApiModelProperty(value = "")


  public String getBalances() {
    return balances;
  }

  public void setBalances(String balances) {
    this.balances = balances;
  }

  public LinksAccountDetails transactions(String transactions) {
    this.transactions = transactions;
    return this;
  }

  /**
   * Get transactions
   * @return transactions
  **/
  @ApiModelProperty(value = "")


  public String getTransactions() {
    return transactions;
  }

  public void setTransactions(String transactions) {
    this.transactions = transactions;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinksAccountDetails _linksAccountDetails = (LinksAccountDetails) o;
    return Objects.equals(this.balances, _linksAccountDetails.balances) &&
        Objects.equals(this.transactions, _linksAccountDetails.transactions) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(balances, transactions, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinksAccountDetails {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    balances: ").append(toIndentedString(balances)).append("\n");
    sb.append("    transactions: ").append(toIndentedString(transactions)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

