package org.doxa.contract.microservices.DTO;

import lombok.Data;

/**
 * Receiving supplier information from entity service
 */
@Data
public class SupplierInfo {
    private String companyCode;

    private String companyName;

    private String uen;

    private String gstRegBusiness;

    private String gstRegNo;

    private String uuid;
    private String countryCode;
    private String countryOfOrigin;
    private String contactPersonName;
    private String contactPersonEmail;
    private String contactPersonNumber;

    // Address
    private String addressLabel;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String supplierCompanyUuid;
    private String paymentTerm;
    private boolean connected;

    public boolean isNotConnected() {
        return !isConnected();
    }
}
