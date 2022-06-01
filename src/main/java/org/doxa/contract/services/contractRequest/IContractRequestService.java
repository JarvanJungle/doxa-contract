package org.doxa.contract.services.contractRequest;

import java.util.List;

import org.doxa.contract.DTO.contractRequest.CRDocumentDto;
import org.doxa.contract.DTO.contractRequest.CreateCRDto;
import org.doxa.contract.DTO.contractRequest.CreateCRDtoWithValidation;
import org.doxa.contract.DTO.contractRequest.EditCRDtoWithValidation;
import org.doxa.contract.exceptions.AccessDeniedException;
import org.doxa.contract.responses.ApiResponse;

public interface IContractRequestService {

	ApiResponse createContractRequest(String companyUuid, CreateCRDto createDto, boolean isSaveDraft) throws AccessDeniedException, Exception;

	ApiResponse listContractRequest(String companyUuid) throws AccessDeniedException, Exception;

	ApiResponse retrieveContractRequestDetails(String companyUuid, String contractRequestUuid) throws AccessDeniedException, Exception;

	ApiResponse recallCancelContractRequest(String companyUuid, String contractRequestUuid, boolean isRecall) throws AccessDeniedException, Exception;

	ApiResponse approverAction(String companyUuid, String contractRequestUuid, List<CRDocumentDto> newlyAddedDocuments, String action) throws AccessDeniedException, Exception;

	ApiResponse submitContractRequest(String companyUuid, CreateCRDtoWithValidation submitDto) throws AccessDeniedException, Exception;

	ApiResponse editSubmitContractRequest(String companyUuid, EditCRDtoWithValidation editDto) throws AccessDeniedException, Exception;

}
