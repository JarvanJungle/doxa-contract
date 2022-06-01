package org.doxa.contract.microservices.DTO.oauth;

import lombok.Data;
import org.doxa.contract.microservices.DTO.ResponseData;


@Data
public class CompanyDetails extends ResponseData {
    private String companyCode;

    private String companyName;

    private String countryCode;

    private String logo;

    private String contactPersonEmail;

    private String contactPersonName;

    private String contactPersonWorkNumber;

    private String gstRegNo;

    private String uen;

    private String countryOfOrigin;

    private String uuid;
}

