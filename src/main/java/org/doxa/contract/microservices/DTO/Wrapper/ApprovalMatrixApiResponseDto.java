package org.doxa.contract.microservices.DTO.Wrapper;

import org.doxa.contract.microservices.DTO.ApprovalMatrixDetailsAPIDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalMatrixApiResponseDto {

	public String status;
    public ApprovalMatrixDetailsAPIDto data;
    public Long timeStamp;
}
