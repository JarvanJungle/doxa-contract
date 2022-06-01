package org.doxa.contract.microservices.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrivilegeDto {

    public String status;
    public List<UserPrivilegeCoreDto> data;
    public Long timeStamp;

}
