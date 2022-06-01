package org.doxa.contract.microservices.DTO.entityService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.doxa.contract.config.CacheConfiguration;
import org.doxa.contract.config.DoxaServiceNameConsts;
import org.doxa.contract.config.Message;
import org.doxa.contract.exceptions.EntitiesApiException;
import org.doxa.contract.exceptions.ObjectDoesNotExistException;
import org.doxa.contract.microservices.DTO.ApprovalGroupDetail;
import org.doxa.contract.microservices.DTO.ApprovalMatrixDetailsAPIDto;
import org.doxa.contract.microservices.DTO.ProjectDetailsApiDto;
import org.doxa.contract.microservices.DTO.SupplierInfo;
import org.doxa.contract.microservices.DTO.Wrapper.*;
import org.doxa.contract.microservices.EurekaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class EntitiesService implements IEntitiesService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CircuitBreakerFactory circuitBreakerFactory;

	private static final String circuitBreakerIdentifier = "circuitbreaker";
	
	private static final String getApprovalMatrixDetailsPath = "/private/approval-details";

	private static final String getListOfSuppliersPath = "/private/supplier-uuid-list";
	
	private static final String validateUserGroupPath = "/private/validate/usergroup";

	private static final String getApprovalGroupDetail = "/private/group-detail";

	private static final String getDefaultAddress = "/private/address-details";

	private static final String GET_SUPPLIER = "/private/supplier-info/{companyUuid}/{uuid}";
	private static final String GET_LATEST_FUNCTION_NUMBER = "/private/prefix/latest-number";
	private static final String GET_PROJECT_DETAILS = "/private/{companyUuid}/get-project-details";
	
	private final EurekaService eurekaService;
	
	public EntitiesService() {
		eurekaService = new EurekaService();
	}

	@Autowired
	private Environment environment;

	@Autowired
	private RetryTemplate retryTemplate;

	@Override
	public ApprovalMatrixDetailsAPIDto getApprovalMatrixDetails(String approvalMatrixUuid, BigDecimal totalAmount, String currencyCode, String companyUuid) throws ObjectDoesNotExistException {
		ResponseEntity<ApprovalMatrixApiResponseDto> response = retryTemplate.execute(retryContext -> {
			log.info("getApprovalMatrixDetails Retry count {}", retryContext.getRetryCount());
			return getApprovalMatrixDetailsApi(approvalMatrixUuid, totalAmount, currencyCode, companyUuid);
		}, retryContext -> {
			log.info("Recover from {}", retryContext.getLastThrowable().getMessage());
			return null;
		});
		if (response == null) {
			throw new ObjectDoesNotExistException("Approval Matrix details not found");
		}
		return Objects.requireNonNull(response.getBody()).getData();
	}

	@Override
	@Cacheable(cacheNames = CacheConfiguration.ENTITIES_SERVICE_CACHE, keyGenerator = "connexCacheKeyGenerator", unless = "#result == null")
	public boolean checkUserGroupValidity(String userUuid, String companyUuid, String groupName) {
		ResponseEntity<UserGroupApiResponseDto> response = retryTemplate.execute(retryContext -> {
			log.info("checkUserGroupValidity Retry count {}", retryContext.getRetryCount());
			return checkUserGroupValidityApi(userUuid, companyUuid, groupName);
		}, retryContext -> {
			log.info("Recover from {}", retryContext.getLastThrowable().getMessage());
			return null;
		});
		if (response != null) {
			return Objects.requireNonNull(response.getBody()).isData();
		}
		return false;
	}

	
    private ResponseEntity<Object> handleExceptionEntities(Throwable throwable) {
        ResponseEntity response = null;
        try{
            if(throwable.getMessage().matches(".*\\b5[0-9][0-9]\\b.*")){
                throw new EntitiesApiException("A problem occured while calling Entities API" + throwable.getMessage());
            } else {
            	UserGroupApiResponseDto defaultResponse = new UserGroupApiResponseDto();
                defaultResponse.setData(false);
                response = new ResponseEntity(defaultResponse,HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new EntitiesApiException("A problem occured while calling Entities API" + throwable.getMessage());
        }

        return response;
    }

	@Override
	public String getService(String serviceName) {
		if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
//            return eurekaService.getService(DoxaServiceNameConsts.ENTITIES_SERVICE.getValue());
			// Localhost
			return "http://localhost:8060";
//            return "https://api-connex-dev.doxa-holdings.com";
		}
		// Use kubernetes service discovery
		return "http://" + DoxaServiceNameConsts.ENTITIES_SERVICE.getValue();
	}


	@Override
	public ResponseEntity<SupplierListApiResponseDto > getSupplierListWithSupplierCompanyUuid(String companyUuid, boolean isBuyer) throws EntitiesApiException {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
		ResponseEntity response = null;
		try {

			String baseUrl = getService(DoxaServiceNameConsts.ENTITIES_SERVICE.getValue());
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + getListOfSuppliersPath)
					.queryParam("companyUuid", companyUuid)
					.queryParam("buyer", isBuyer);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<>(headers);
			response = circuitBreaker.run(() ->  restTemplate.exchange(
					builder.toUriString(),
					HttpMethod.GET,
					entity,
					SupplierListApiResponseDto.class
			), throwable -> handleExceptionThrowError(throwable));
		} catch (Exception e) {
			throw new EntitiesApiException(Message.RETRIEVE_SUPPLIERS_FAILED.getValue() + ": " + e.getMessage());
		}
		return response;
	}

	private ResponseEntity<Object> handleExceptionThrowError(Throwable throwable) {
		throw new EntitiesApiException("A problem occured while calling Entities API: " + throwable.getMessage());
	}

	@Override
	public ApprovalGroupDetail getApprovalGroupDetail(String companyUuid, String groupUuid) {
		return Objects.requireNonNull(getApprovalGroupApi(companyUuid, groupUuid)
				.getBody()).getData();
	}


	private ResponseEntity<ApprovalGroupDetailApiResponse> getApprovalGroupApi(String companyUuid, String groupUuid) throws EntitiesApiException {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
		ResponseEntity response = null;
		try {

			String baseUrl = getService(DoxaServiceNameConsts.ENTITIES_SERVICE.getValue());
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + getApprovalGroupDetail)
					.queryParam("companyUuid", companyUuid)
					.queryParam("groupUuid", groupUuid);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<>(headers);
			response = circuitBreaker.run(() -> restTemplate.exchange(
					builder.toUriString(),
					HttpMethod.GET,
					entity,
					ApprovalGroupDetailApiResponse.class
			), this::handleApprovalGroupApi);
		} catch (Exception e) {
			throw new EntitiesApiException("A problem occured while calling Entities API" + e.getMessage());
		}
		return response;
	}

	private ResponseEntity<ApprovalGroupDetailApiResponse> handleApprovalGroupApi(Throwable throwable) {
		ResponseEntity response = null;
		try {
			if (throwable.getMessage().matches(".*\\b5[0-9][0-9]\\b.*")) {
				throw new EntitiesApiException("A problem occured while calling Entities API" + throwable.getMessage());
			} else {
				ApprovalGroupDetailApiResponse defaultResponse = new ApprovalGroupDetailApiResponse();
				defaultResponse.setData(null);
				response = new ResponseEntity(defaultResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new EntitiesApiException("A problem occured while calling Entities API" + throwable.getMessage());
		}

		return response;
	}

	@Override
	public AddressDetails getDefaultAddress(String companyUuid) {
		ResponseEntity<AddressesResponseWrapper> response = getDefaultAddressApi(companyUuid);
		return response.getBody().getData();
	}

	public ResponseEntity<AddressesResponseWrapper> getDefaultAddressApi(String companyUuid)
			throws EntitiesApiException {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
		ResponseEntity response = null;
		try {
			String baseUrl = getService(DoxaServiceNameConsts.ENTITIES_SERVICE.getValue());
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + getDefaultAddress)
					.queryParam("companyUuid", companyUuid);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<?> entity = new HttpEntity<>(headers);
			response = circuitBreaker.run(
					() -> restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
							AddressesResponseWrapper.class),
					this::handleExceptionCompanyCurrencyProjectValid);
		} catch (Exception e) {
			throw new EntitiesApiException("A problem occured while calling Entities API" + e.getMessage());
		}
		return response;
	}

	private ResponseEntity<CompanyCurrencyProjectValidApiResponseDto> handleExceptionCompanyCurrencyProjectValid(
			Throwable throwable) {
		ResponseEntity response = null;
		try {
			if (throwable.getMessage().matches(".*\\b5[0-9][0-9]\\b.*")) {
				throw new EntitiesApiException("A problem occured while calling Entities API" + throwable.getMessage());
			} else {
				CompanyCurrencyProjectValidApiResponseDto defaultResponse = new CompanyCurrencyProjectValidApiResponseDto();
				defaultResponse.setData(false);
				response = new ResponseEntity(defaultResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new EntitiesApiException("A problem occured while calling Entities API" + throwable.getMessage());
		}

		return response;
	}


	@Override
	@Cacheable(cacheNames = CacheConfiguration.ENTITIES_SERVICE_CACHE, keyGenerator = "connexCacheKeyGenerator", unless = "#result == null")
	public SupplierInfo getSupplierInfo(String companyUuid, String uuid) throws ObjectDoesNotExistException {
		ResponseEntity<SupplierInfoResponse> response = retryTemplate.execute(retryContext -> {
			log.info("getSupplierInfo Retry count {}", retryContext.getRetryCount());
			return getSupplierInfoApi(companyUuid, uuid);
		}, retryContext -> {
			log.info("Recover from {}", retryContext.getLastThrowable().getMessage());
			return null;
		});
		if (response == null) {
			throw new ObjectDoesNotExistException(Message.SUPPLIER_NOT_FOUND.getValue());
		}
		return Objects.requireNonNull(response.getBody()).getData();
	}


	@Override
	public String getLatestFunctionNumber(String companyUuid, String projectCode, String supplierCode, String functionCode) throws EntitiesApiException {
		ResponseEntity<PrefixNumberApiResponseDto> response = retryTemplate.execute(retryContext -> {
			log.info("getLatestFunctionNumber Retry count {}", retryContext.getRetryCount());
			return getLatestFunctionNumberApi(companyUuid, projectCode, supplierCode, functionCode);
		}, retryContext -> {
			log.info("Recover from {}", retryContext.getLastThrowable().getMessage());
			return null;
		});
		if (response != null) {
			return Objects.requireNonNull(response.getBody()).getData();
		}
		return null;
	}

	@Override
	@Cacheable(cacheNames = CacheConfiguration.ENTITIES_SERVICE_CACHE, keyGenerator = "connexCacheKeyGenerator", unless = "#result == null")
	public ProjectDetailsApiDto getProjectDetails(String companyUuid, String projectCodeUuid) throws ObjectDoesNotExistException {
		ResponseEntity<ProjectDetailsApiResponseDto> response = retryTemplate.execute(retryContext -> {
			log.info("getProjectDetails Retry count {}", retryContext.getRetryCount());
			return getProjectDetailsApi(companyUuid, projectCodeUuid);
		}, retryContext -> {
			log.info("Recover from {}", retryContext.getLastThrowable().getMessage());
			return null;
		});
		if (response == null) {
			throw new ObjectDoesNotExistException("Project not found");
		}
		return Objects.requireNonNull(response.getBody()).getData();
	}



	// ==================================================================CALLING API FROM HERE========================================================================================================================//

	/**
	 * Pass in NULL for projectCode if its a NON PROJECT!!
	 * Need to check if the return is String "Manual", if "Manual" means the function number should be filled in manually by the user
	 */
	private ResponseEntity<PrefixNumberApiResponseDto> getLatestFunctionNumberApi(String companyUuid, String projectCode, String supplierCode, String functionCode) throws EntitiesApiException {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
		ResponseEntity response = null;
		String baseUrl = getService(DoxaServiceNameConsts.ENTITIES_SERVICE.getValue());
		UriComponentsBuilder builder;
		if (projectCode == null) {
			builder = UriComponentsBuilder.fromHttpUrl(baseUrl + GET_LATEST_FUNCTION_NUMBER)
					.queryParam("companyUuid", companyUuid)
					.queryParam("supplierCode", supplierCode)
					.queryParam("functionCode", functionCode);
		} else {
			builder = UriComponentsBuilder.fromHttpUrl(baseUrl + GET_LATEST_FUNCTION_NUMBER)
					.queryParam("companyUuid", companyUuid)
					.queryParam("projectCode", projectCode)
					.queryParam("supplierCode", supplierCode)
					.queryParam("functionCode", functionCode);
		}

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);
		response = circuitBreaker.run(() -> restTemplate.exchange(
				builder.toUriString(),
				HttpMethod.GET,
				entity,
				PrefixNumberApiResponseDto.class
		));
		return response;
	}

	private ResponseEntity<ApprovalMatrixApiResponseDto> getApprovalMatrixDetailsApi(String approvalMatrixUuid, BigDecimal totalAmount, String currencyCode, String companyUuid) throws EntitiesApiException {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
		ResponseEntity response;
		String baseUrl = getService(DoxaServiceNameConsts.ENTITIES_SERVICE.getValue());
		UriComponentsBuilder builder;
		if (currencyCode == null) {
			builder = UriComponentsBuilder.fromHttpUrl(baseUrl + getApprovalMatrixDetailsPath)
					.queryParam("companyUuid", companyUuid)
					.queryParam("approvalMatrixUuid", approvalMatrixUuid)
					.queryParam("totalAmount", totalAmount);
		} else {
			builder = UriComponentsBuilder.fromHttpUrl(baseUrl + getApprovalMatrixDetailsPath)
					.queryParam("companyUuid", companyUuid)
					.queryParam("approvalMatrixUuid", approvalMatrixUuid)
					.queryParam("totalAmount", totalAmount)
					.queryParam("currencyCode", currencyCode);
		}
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);
		response = circuitBreaker.run(() -> restTemplate.exchange(
				builder.toUriString(),
				HttpMethod.GET,
				entity,
				ApprovalMatrixApiResponseDto.class
		));
		return response;
	}

	private ResponseEntity<UserGroupApiResponseDto> checkUserGroupValidityApi(String userUuid, String companyUuid, String groupUuid) throws EntitiesApiException {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
		ResponseEntity response = null;
		String baseUrl = getService(DoxaServiceNameConsts.ENTITIES_SERVICE.getValue());
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + validateUserGroupPath)
				.queryParam("companyUuid", companyUuid)
				.queryParam("groupUuid", groupUuid)
				.queryParam("userUuid", userUuid);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);
		response = circuitBreaker.run(() -> restTemplate.exchange(
				builder.toUriString(),
				HttpMethod.GET,
				entity,
				UserGroupApiResponseDto.class
		));
		return response;
	}


	private ResponseEntity<SupplierInfoResponse> getSupplierInfoApi(String companyUuid, String uuid) throws EntitiesApiException {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
		ResponseEntity response = null;
		String baseUrl = getService(DoxaServiceNameConsts.ENTITIES_SERVICE.getValue());
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl +
				GET_SUPPLIER.replace("{companyUuid}", companyUuid)
						.replace("{uuid}", uuid));
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);
		response = circuitBreaker.run(() -> restTemplate.exchange(
				builder.toUriString(),
				HttpMethod.GET,
				entity,
				SupplierInfoResponse.class
		));
		return response;
	}

	private ResponseEntity<ProjectDetailsApiResponseDto> getProjectDetailsApi(String companyUuid, String projectCodeUuid) {
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create(circuitBreakerIdentifier);
		ResponseEntity response;
		String baseUrl = getService(DoxaServiceNameConsts.ENTITIES_SERVICE.getValue());
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + GET_PROJECT_DETAILS.replace("{companyUuid}", companyUuid))
				.queryParam("projectCodeUuid", projectCodeUuid);
		response = circuitBreaker.run(() -> restTemplate.exchange(
				builder.toUriString(),
				HttpMethod.GET,
				new HttpEntity<>(new HttpHeaders()),
				ProjectDetailsApiResponseDto.class
		));
		return response;
	}



}
