package org.doxa.contract.microservices.DTO.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDetailApiWrapper {
    private String status;
    private CompanyDetails data;
}

