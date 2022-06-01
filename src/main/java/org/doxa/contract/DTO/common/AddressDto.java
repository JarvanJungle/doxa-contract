package org.doxa.contract.DTO.common;

import lombok.Data;


@Data
public class AddressDto {
    private String addressLabel;
    private String addressFirstLine;
    private String addressSecondLine;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String uuid;
    private boolean isDefault;
    private boolean isActive;
}
