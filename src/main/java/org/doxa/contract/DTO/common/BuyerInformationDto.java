package org.doxa.contract.DTO.common;

import lombok.Data;
import org.doxa.contract.models.common.ContactInformation;

@Data
public class BuyerInformationDto {
    private String buyerCode;
    private String buyerVendorConnectionUuid; //required is buyer and supplier is not onboarded
    private String buyerCompanyUuid;
    private String companyName;
    private String taxRegNo;
    private String companyCountry;
    private AddressDto companyAddress;
    private ContactInformationDto contactInformation;
}
