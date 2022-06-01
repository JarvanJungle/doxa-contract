package org.doxa.contract.services.contract;

import org.doxa.contract.DTO.contract.ContractDto;
import org.doxa.contract.DTO.contract.ContractRequestDto;
import org.doxa.contract.DTO.contract.ContractSubmissionDto;
import org.doxa.contract.exceptions.AccessDeniedException;
import org.doxa.contract.responses.ApiResponse;

public interface IContractService {
    public ApiResponse convertToContract(String companyUuid, String contractRequestUuid) throws Exception;
    public ApiResponse getBuyerContractDetail(String companyUuid, String contractUuid) throws AccessDeniedException, Exception;
    public ApiResponse getSupplierContractDetail(String companyUuid, String contractUuid) throws AccessDeniedException, Exception;
    public ApiResponse listBuyerContracts(String companyUuid) throws Exception;
    public ApiResponse listSupplierContracts(String companyUuid) throws Exception;
    public ApiResponse submitContract(String companyUuid, String contractUuid, ContractSubmissionDto contractInformation, String contractStatus, String contractAction)throws Exception;
    public ApiResponse changeContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction)throws Exception;
    public ApiResponse approveContract(String companyUuid, String contractUuid)throws Exception;
    public ApiResponse recallContract(String companyUuid, String contractUuid) throws Exception;
    public ApiResponse issueContract(String companyUuid, String contractUuid) throws Exception;
    public ApiResponse terminateContract(String companyUuid, String contractUuid) throws Exception;
    public ApiResponse supplierChangeContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) throws Exception;
    public ApiResponse changeRejectandSendBackContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) throws Exception;
}
