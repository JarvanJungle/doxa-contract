package org.doxa.contract.microservices.DTO.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractInfoApiDto {

    private String contractUuid;

    private String contractNumber;

    private int convertedFromRFQ;

}
