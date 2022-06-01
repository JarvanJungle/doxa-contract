package org.doxa.contract.microservices.DTO.oauth;


import lombok.extern.slf4j.Slf4j;
import org.doxa.auth.Authority;
import org.doxa.auth.AuthorityApiResponse;
import org.doxa.contract.config.DoxaServiceNameConsts;
import org.doxa.contract.config.Message;
import org.doxa.contract.exceptions.EntitiesApiException;
import org.doxa.contract.exceptions.ObjectDoesNotExistException;
import org.doxa.contract.microservices.interfaces.IMicroservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OauthService implements IOauthService, IMicroservice {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RetryTemplate retryTemplate;
    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;
    @Autowired
    private Environment environment;

    private static final String circuitBreakerIdentifier = "oauth-circuit-breaker";
    private static final String getCompanyDetails = "/api/private/company-details/{uuid}";
    private static final String getUserDetails = "/api/private/user-details/{uuid}";
    private static final String GET_AUTHORITY = "/api/private/company/{companyUuid}/user/{userUuid}/authority";

    @Override
    public CompanyDetails getCompanyDetails(String companyUuid) {
        ResponseEntity<CompanyDetailApiWrapper> response = getCompanyDetailsApi(companyUuid);
        CompanyDetailApiWrapper companyDetailApiWrapper = response.getBody();
        return companyDetailApiWrapper.getData();
    }

    @Override
    public UserDetail getUserDetails(String userUuid) {
        ResponseEntity<UserDetailApiWrapper> response = getUserDetailsApi(userUuid);
        UserDetailApiWrapper userDetailApiWrapper = response.getBody();
        return userDetailApiWrapper.getData();
    }

    private ResponseEntity<CompanyDetailApiWrapper> getCompanyDetailsApi(String companyUuid) throws EntitiesApiException {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
        ResponseEntity response = null;
        try {

            String baseUrl = getService(DoxaServiceNameConsts.OAUTH_SERVICE.getValue());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + getCompanyDetails.replace("{uuid}", companyUuid));
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);
            response = circuitBreaker.run(() -> restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    CompanyDetailApiWrapper.class
            ), throwable -> handleExceptionThrowError(throwable));
        } catch (Exception e) {
            throw new EntitiesApiException(Message.NOT_CONNECTED.getValue() + ": " + e.getMessage());
        }
        return response;
    }

    private ResponseEntity<UserDetailApiWrapper> getUserDetailsApi(String userUuid) throws EntitiesApiException {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
        ResponseEntity response = null;
        try {

            String baseUrl = getService(DoxaServiceNameConsts.OAUTH_SERVICE.getValue());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + getUserDetails.replace("{uuid}", userUuid));
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);
            response = circuitBreaker.run(() -> restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    UserDetailApiWrapper.class
            ), throwable -> handleExceptionThrowError(throwable));
        } catch (Exception e) {
            throw new EntitiesApiException(Message.NOT_CONNECTED.getValue() + ": " + e.getMessage());
        }
        return response;
    }

    private ResponseEntity<Object> handleExceptionThrowError(Throwable throwable) {
        throw new EntitiesApiException("A problem occured while calling Entities API: " + throwable.getMessage());
    }


    @Override
    public List<Authority> getAuthorities(String companyUuid, String userUuid) throws ObjectDoesNotExistException {
        log.info("Getting authority companyUuid= {}, userUuid = {}", companyUuid, userUuid);
        ResponseEntity<AuthorityApiResponse> response = retryTemplate.execute(retryContext -> {
            log.info("Get user authorities, Retry count {}", retryContext.getRetryCount());
            return getAuthoritiesApi(companyUuid, userUuid);
        }, retryContext -> {
            log.info("Recover from {}", retryContext.getLastThrowable().getMessage());
            return null;
        });
        if (response == null) {
            throw new ObjectDoesNotExistException(Message.OBJECT_DOESNOT_EXIST.getValue());
        }
        return Objects.requireNonNull(response.getBody()).getData();
    }

    private ResponseEntity<AuthorityApiResponse> getAuthoritiesApi(String companyUuid, String userUuid) throws EntitiesApiException {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
        ResponseEntity response;
        String baseUrl = getService(null);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + GET_AUTHORITY
                .replace("{userUuid}", userUuid)
                .replace("{companyUuid}", companyUuid));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        response = circuitBreaker.run(() -> restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                AuthorityApiResponse.class
        ));
        return response;
    }

    @Override
    public String getService(String serviceName) {
        if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            return "http://localhost:8031";
        }
        // Use kubernetes service discovery
        return "http://" + DoxaServiceNameConsts.OAUTH_SERVICE.getValue();
    }
}