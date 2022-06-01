package org.doxa.contract.cache;

import org.doxa.contract.DTO.contractRequest.CRDocumentDto;
import org.doxa.contract.DTO.contractRequest.CreateCRDto;
import org.doxa.contract.DTO.contractRequest.CreateCRDtoWithValidation;
import org.doxa.contract.DTO.contractRequest.EditCRDtoWithValidation;
import org.doxa.contract.exceptions.AccessDeniedException;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.serviceImpl.contractRequest.ContractRequestServiceImpl;
import org.doxa.contract.services.contractRequest.IContractRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractRequestCache implements IContractRequestService {

    @Autowired
    private ContractRequestServiceImpl contractRequestService;


    @Override
    public ApiResponse createContractRequest(String companyUuid, CreateCRDto createDto, boolean isSaveDraft) throws Exception {
        return contractRequestService.createContractRequest(companyUuid, createDto, isSaveDraft);
    }

    @Override
    public ApiResponse listContractRequest(String companyUuid) throws AccessDeniedException {
        return contractRequestService.listContractRequest(companyUuid);
    }

    @Override
    public ApiResponse retrieveContractRequestDetails(String companyUuid, String contractRequestUuid) throws AccessDeniedException {
        return contractRequestService.retrieveContractRequestDetails(companyUuid, contractRequestUuid);
    }

    @Override
    public ApiResponse recallCancelContractRequest(String companyUuid, String contractRequestUuid, boolean isRecall) throws AccessDeniedException {
        return contractRequestService.recallCancelContractRequest(companyUuid, contractRequestUuid, isRecall);
    }

    @Override
    public ApiResponse approverAction(String companyUuid, String contractRequestUuid, List<CRDocumentDto> newlyAddedDocuments, String action) throws AccessDeniedException {
        return contractRequestService.approverAction(companyUuid, contractRequestUuid, newlyAddedDocuments, action);
    }

    @Override
    public ApiResponse submitContractRequest(String companyUuid, CreateCRDtoWithValidation submitDto) throws Exception {
        return contractRequestService.submitContractRequest(companyUuid, submitDto);
    }

    @Override
    public ApiResponse editSubmitContractRequest(String companyUuid, EditCRDtoWithValidation editDto) throws Exception {
        return contractRequestService.editSubmitContractRequest(companyUuid, editDto);
    }
}
