package org.doxa.contract.authorization;

import org.doxa.contract.DTO.contract.ContractSubmissionDto;
import org.doxa.contract.cache.ContractCache;
import org.doxa.auth.DoxaAuthenticationManager;
import org.doxa.contract.exceptions.AccessDeniedException;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.services.contract.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractAuthorization implements IContractService {
    private static final String READ_CONTRACT_AUTHORITY = "contract:read";
    private static final String WRITE_CONTRACT_AUTHORITY = "contract:write";
    private static final String APPROVE_CONTRACT_AUTHORITY = "contract:approve";

    @Autowired
    private DoxaAuthenticationManager authenticationManager;

    @Autowired
    private ContractCache contractCache;

    @Override
    public ApiResponse convertToContract(String companyUuid, String contractRequestUuid) throws Exception {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
            return contractCache.convertToContract(companyUuid, contractRequestUuid);
        }
        throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse getBuyerContractDetail(String companyUuid, String contractUuid) throws Exception {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, READ_CONTRACT_AUTHORITY)) {
            return contractCache.getBuyerContractDetail(companyUuid, contractUuid);
        }
        throw new AccessDeniedException(READ_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse getSupplierContractDetail(String companyUuid, String contractUuid) throws Exception {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, READ_CONTRACT_AUTHORITY)) {
            return contractCache.getSupplierContractDetail(companyUuid, contractUuid);
        }
        throw new AccessDeniedException(READ_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse listBuyerContracts(String companyUuid) throws Exception  {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, READ_CONTRACT_AUTHORITY)) {
            return contractCache.listBuyerContracts(companyUuid);
        }
        throw new AccessDeniedException(READ_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse listSupplierContracts(String companyUuid) throws Exception  {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, READ_CONTRACT_AUTHORITY)) {
            return contractCache.listSupplierContracts(companyUuid);
        }
        throw new AccessDeniedException(READ_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse submitContract(String companyUuid, String contractUuid, ContractSubmissionDto contractInformation, String contractStatus, String contractAction) throws Exception {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
            return contractCache.submitContract(companyUuid, contractUuid,contractInformation,contractStatus,contractAction);
        }
        throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse changeContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) throws Exception {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
            return contractCache.changeContractStatus(companyUuid, contractUuid,contractStatus,contractAction);
        }
        throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse changeRejectandSendBackContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) throws Exception {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY) || (authenticationManager.hasAuthority(companyUuid, APPROVE_CONTRACT_AUTHORITY) && authenticationManager.hasAuthority(companyUuid, READ_CONTRACT_AUTHORITY))) {
            return contractCache.changeContractStatus(companyUuid, contractUuid,contractStatus,contractAction);
        }
        throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse approveContract(String companyUuid, String contractUuid) throws Exception {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, APPROVE_CONTRACT_AUTHORITY)) {
            return contractCache.approveContract(companyUuid, contractUuid);
        }
        throw new AccessDeniedException(APPROVE_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse recallContract(String companyUuid, String contractUuid) throws Exception  {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
            return contractCache.recallContract(companyUuid, contractUuid);
        }
        throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse issueContract(String companyUuid, String contractUuid) throws Exception   {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
            return contractCache.issueContract(companyUuid, contractUuid);
        }
        throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse terminateContract(String companyUuid, String contractUuid) throws Exception  {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
            return contractCache.terminateContract(companyUuid, contractUuid);
        }
        throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
    }

    @Override
    public ApiResponse supplierChangeContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) throws Exception  {
        if (authenticationManager.hasAdminRole(companyUuid) || authenticationManager.hasAuthority(companyUuid, WRITE_CONTRACT_AUTHORITY)) {
            return contractCache.supplierChangeContractStatus(companyUuid, contractUuid,contractStatus,contractAction);
        }
        throw new AccessDeniedException(WRITE_CONTRACT_AUTHORITY);
    }
}
