package org.doxa.contract.microservices.DTO.Wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.doxa.contract.microservices.DTO.ProjectDetailsApiDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetailsApiResponseDto {
    public String status;
    public ProjectDetailsApiDto data;
    public Long timeStamp;
}