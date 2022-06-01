package org.doxa.contract.microservices.interfaces;


import org.doxa.contract.microservices.DTO.UserPrivilegeDto;
import org.springframework.http.ResponseEntity;

public interface IAuthorizationService {

    public ResponseEntity<UserPrivilegeDto> getUserPrivilege(Long userId, String featureCode, String token);
}
