package org.doxa.contract.microservices.DTO.Wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupApiResponseDto {

	public String status;
    public boolean data;
    public Long timeStamp;
}
