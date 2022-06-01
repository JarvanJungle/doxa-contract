package org.doxa.contract.microservices.DTO.entityService;

import org.doxa.contract.exceptions.EntitiesApiException;
import org.doxa.contract.exceptions.ObjectDoesNotExistException;
import org.doxa.contract.microservices.DTO.ApprovalGroupDetail;
import org.doxa.contract.microservices.DTO.ApprovalMatrixDetailsAPIDto;
import org.doxa.contract.microservices.DTO.ProjectDetailsApiDto;
import org.doxa.contract.microservices.DTO.SupplierInfo;
import org.doxa.contract.microservices.DTO.Wrapper.ApprovalMatrixApiResponseDto;
import org.doxa.contract.microservices.DTO.Wrapper.UserGroupApiResponseDto;
import org.doxa.contract.microservices.DTO.Wrapper.SupplierListApiResponseDto;
import org.doxa.contract.microservices.DTO.entityService.AddressDetails;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface IEntitiesService {

	ApprovalMatrixDetailsAPIDto getApprovalMatrixDetails(String approvalMatrixUuid, BigDecimal totalAmount, String currencyCode, String companyUuid) throws ObjectDoesNotExistException;

	ResponseEntity<SupplierListApiResponseDto> getSupplierListWithSupplierCompanyUuid(String companyUuid,
																					  boolean isBuyer) throws EntitiesApiException;

	String getService(String serviceName);

	boolean checkUserGroupValidity(String userUuid, String companyUuid,
			String groupUuid) throws EntitiesApiException;

	ApprovalGroupDetail getApprovalGroupDetail(String companyUuid, String groupUuid);

	AddressDetails getDefaultAddress(String companyUuid);

	SupplierInfo getSupplierInfo(String companyUuid, String uuid) throws ObjectDoesNotExistException;

	String getLatestFunctionNumber(String companyUuid, String projectCode,
								   String supplierCode, String functionCode) throws EntitiesApiException;

	ProjectDetailsApiDto getProjectDetails(String companyUuid, String projectCodeUuid) throws ObjectDoesNotExistException;

}
