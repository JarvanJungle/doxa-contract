package org.doxa.contract.microservices.DTO.oauth;

import lombok.Data;
import org.doxa.contract.microservices.DTO.ResponseData;


@Data
public class UserDetail extends ResponseData {
    private String name;
    private String email;
    private String uuid;
    private String workNumber;
    private String designation;
    private String countryCode;
}

