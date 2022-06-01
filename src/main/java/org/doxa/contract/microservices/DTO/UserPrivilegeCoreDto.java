package org.doxa.contract.microservices.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserPrivilegeCoreDto {

    private List<String> actionsAllowed;
    private List<String> fieldsRestricted;
    private String _id;
    private String companyId;
    private String featureCode;
    private String userId;
    private String __v;
    private String featureName;
}
