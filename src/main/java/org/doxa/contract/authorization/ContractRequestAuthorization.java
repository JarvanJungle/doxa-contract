package org.doxa.contract.authorization;

import java.util.List;

import org.doxa.contract.DTO.contractRequest.CRDocumentDto;
import org.doxa.contract.DTO.contractRequest.CreateCRDto;
import org.doxa.contract.DTO.contractRequest.CreateCRDtoWithValidation;
import org.doxa.contract.DTO.contractRequest.EditCRDtoWithValidation;
import org.doxa.contract.cache.ContractRequestCache;
import org.doxa.auth.DoxaAuthenticationManager;
import org.doxa.contract.exceptions.AccessDeniedException;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.services.contractRequest.IContractRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractRequestAuthorization implements IContractRequestService {

	private static final String READ_CONTRACT_AUTHORITY = "contract:read";
	private static final String WRITE_CONTRACT_AUTHORITY = "contract:write";
	private static final String APPROVE_CONTRACT_AUTHORITY = "contract:approve";
	
	@Autowired
    private ContractRequestCache contractRequestCache;

    @Autowired
    private DoxaAuthenticationManager authenticationManager;

	@Override
	public ApiResponse createContractRequest(String companyUuid, CreateCRDto createDto, boolean isSaveDraft) throws Exception {
	      if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
	    	  return contractRequestCache.createContractRequest(companyUuid, createDto, isSaveDraft);
	      }
			throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
	}
	
	@Override
	public ApiResponse submitContractRequest(String companyUuid, CreateCRDtoWithValidation submitDto)  throws Exception {
	      if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
	    	  return contractRequestCache.submitContractRequest(companyUuid, submitDto);
	      }
		throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
	}
	
	@Override
	public ApiResponse editSubmitContractRequest(String companyUuid, EditCRDtoWithValidation editDto) throws Exception  {
	      if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
	    	  return contractRequestCache.editSubmitContractRequest(companyUuid, editDto);
	      }
		throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
	}

	@Override
	public ApiResponse listContractRequest(String companyUuid) throws Exception  {
	      if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, READ_CONTRACT_AUTHORITY)) {
	    	  return contractRequestCache.listContractRequest(companyUuid);
	      }
		throw new AccessDeniedException(READ_CONTRACT_AUTHORITY);
	}

	@Override
	public ApiResponse retrieveContractRequestDetails(String companyUuid, String contractRequestUuid) throws Exception  {
	      if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, READ_CONTRACT_AUTHORITY)) {
	    	  return contractRequestCache.retrieveContractRequestDetails(companyUuid, contractRequestUuid);
	      }
		throw new AccessDeniedException(READ_CONTRACT_AUTHORITY);
	}

	@Override
	public ApiResponse recallCancelContractRequest(String companyUuid, String contractRequestUuid, boolean isRecall) throws Exception  {
	      if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
	    	  return contractRequestCache.recallCancelContractRequest(companyUuid, contractRequestUuid, isRecall);
	      }
		throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
	}

	@Override
	public ApiResponse approverAction(String companyUuid, String contractRequestUuid, List<CRDocumentDto> newlyAddedDocuments, String action) throws Exception  {
	      if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, APPROVE_CONTRACT_AUTHORITY)) {
	    	  return contractRequestCache.approverAction(companyUuid, contractRequestUuid, newlyAddedDocuments, action);
	      }
		throw new AccessDeniedException(APPROVE_CONTRACT_AUTHORITY);
	}
	
}
