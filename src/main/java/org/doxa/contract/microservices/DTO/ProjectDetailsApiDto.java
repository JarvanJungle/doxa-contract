package org.doxa.contract.microservices.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDetailsApiDto {

    private String uuid;

    private String projectCode;

    private String projectTitle;

    private List<ProjectUser> projectUserDtoList;

}
