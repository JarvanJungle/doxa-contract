package org.doxa.contract.microservices.DTO.entityService;

import lombok.Data;

@Data
public class AddressDetails {
    private String addressLabel;

    private String addressFirstLine;

    private String addressSecondLine;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private String uuid;
}
