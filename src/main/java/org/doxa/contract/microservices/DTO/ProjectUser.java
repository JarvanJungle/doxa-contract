package org.doxa.contract.microservices.DTO;

import lombok.Data;

@Data
public class ProjectUser {
    private String userUuid;
    private String userName;
    private String projectUserRole;
}
