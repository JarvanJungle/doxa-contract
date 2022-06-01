package org.doxa.contract.microservices;


import org.doxa.contract.exceptions.AuthorizationApiException;
import org.doxa.contract.config.DoxaServiceNameConsts;
import org.doxa.contract.microservices.DTO.UserPrivilegeCoreDto;
import org.doxa.contract.microservices.DTO.UserPrivilegeDto;
import org.doxa.contract.microservices.interfaces.IAuthorizationService;
import org.doxa.contract.microservices.interfaces.IEurekaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AuthorizationService implements IAuthorizationService, IEurekaService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    private static final String getUserPrivilegePath = "/privileges/v2/userPrivilege";

    private static final String circuitBreakerIdentifier = "circuitbreaker";

    private final EurekaService eurekaService;

    public AuthorizationService() {
        eurekaService = new EurekaService();
    }

    @Autowired
    private Environment environment;

    @Override
    public ResponseEntity<UserPrivilegeDto> getUserPrivilege(Long userId, String featureCode, String token)
        throws AuthorizationApiException {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
        ResponseEntity<UserPrivilegeDto> response = null;
        try {
            String baseUrl = getService(DoxaServiceNameConsts.AUTHORIZATION_SERVICE.getValue());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + getUserPrivilegePath)
                    .queryParam("userId", userId)
                    .queryParam("featureCode", featureCode);
            HttpHeaders headers = new HttpHeaders();
            token.replace("Bearer ","");
            headers.add("Authorization", token );
            HttpEntity<?> entity = new HttpEntity<>(headers);
            response = circuitBreaker.run(() ->  restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    UserPrivilegeDto.class
            ), throwable -> handleExceptionUserPrivilege(throwable));
        } catch (Exception e) {
            throw new AuthorizationApiException("A problem occured while calling User privilege API" + e.getMessage());
        }
        return response;
    }

    private ResponseEntity<UserPrivilegeDto> handleExceptionUserPrivilege(Throwable throwable) {
        ResponseEntity<UserPrivilegeDto> response = null;
        try{
            if(throwable.getMessage().matches(".*\\b5[0-9][0-9]\\b.*")){
                throw new AuthorizationApiException("A problem occured while calling User privilege API" + throwable.getMessage());
            } else {
                UserPrivilegeDto defaultResponse = new UserPrivilegeDto();
                List<UserPrivilegeCoreDto> defaultCoreResponseList = new ArrayList<UserPrivilegeCoreDto>();
                UserPrivilegeCoreDto defaultCoreResponse = new UserPrivilegeCoreDto();
                List<String> defaultRestriction = new ArrayList<String>();
//                defaultRestriction.add(CatalogueFieldEnum.UNIT_PRICE.getValue());
                defaultCoreResponse.setFieldsRestricted(defaultRestriction);
                defaultCoreResponseList.add(defaultCoreResponse);
                defaultResponse.setData(defaultCoreResponseList);
                response = new ResponseEntity<>(defaultResponse,HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new AuthorizationApiException("A problem occured while calling User privilege API" + throwable.getMessage());
        }

        return response;
    }

    @Override
    public String getService(String serviceName) {
        if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
//            return eurekaService.getService(DoxaServiceNameConsts.AUTHORIZATION_SERVICE.getValue());
            // Localhost
            //return "http://localhost:8080";
            return "https://api-connex-dev.doxa-holdings.com";
        }
        // Use kubernetes service discovery
        return "http://" + DoxaServiceNameConsts.AUTHORIZATION_SERVICE.getValue();
    }
}
