package org.doxa.contract.DTO.common;

import lombok.Data;
import org.doxa.contract.models.common.Address;
import org.doxa.contract.models.common.ContactInformation;

import javax.validation.constraints.NotEmpty;

@Data
public class SupplierInformationDto {
    private String supplierCode;
    @NotEmpty
    private String supplierVendorConnectionUuid;
    private String supplierCompanyUuid;
    private String companyName;
    private String taxRegNo;
    private String companyCountry;
    private AddressDto companyAddress;
    private ContactInformation contactInformation;
}
