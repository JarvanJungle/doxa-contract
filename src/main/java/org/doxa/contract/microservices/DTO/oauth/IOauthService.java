package org.doxa.contract.microservices.DTO.oauth;

import org.doxa.auth.Authority;
import org.doxa.contract.exceptions.ObjectDoesNotExistException;

import java.util.List;

public interface IOauthService {
    CompanyDetails getCompanyDetails(String companyUuid);
    UserDetail getUserDetails(String userUuid);
    List<Authority> getAuthorities(String companyUuid, String userUuid) throws ObjectDoesNotExistException;
}
