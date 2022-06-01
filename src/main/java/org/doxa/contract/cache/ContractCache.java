package org.doxa.contract.cache;

import org.doxa.contract.DTO.contract.ContractRequestDto;
import org.doxa.contract.DTO.contract.ContractSubmissionDto;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.serviceImpl.contract.ContractServiceImpl;
import org.doxa.contract.services.contract.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractCache implements IContractService {

    @Autowired
    private ContractServiceImpl contractServiceImpl;

    @Override
    public ApiResponse convertToContract(String companyUuid, String contractRequestUuid) throws Exception {
        return contractServiceImpl.convertToContract(companyUuid,contractRequestUuid);
    }

    @Override
    public ApiResponse getBuyerContractDetail(String companyUuid, String contractUuid) {
        return contractServiceImpl.getBuyerContractDetail(companyUuid,contractUuid);
    }

    @Override
    public ApiResponse getSupplierContractDetail(String companyUuid, String contractUuid) {
        return contractServiceImpl.getSupplierContractDetail(companyUuid,contractUuid);
    }

    @Override
    public ApiResponse listBuyerContracts(String companyUuid) {
        return contractServiceImpl.listBuyerContracts(companyUuid);
    }

    @Override
    public ApiResponse listSupplierContracts(String companyUuid) {
        return contractServiceImpl.listSupplierContracts(companyUuid);
    }

    @Override
    public ApiResponse submitContract(String companyUuid, String contractUuid, ContractSubmissionDto contractInformation, String contractStatus, String contractAction) throws Exception {
        return contractServiceImpl.submitContract(companyUuid,contractUuid,contractInformation,contractStatus,contractAction);
    }

    @Override
    public ApiResponse changeContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) throws Exception {
        return contractServiceImpl.changeContractStatus(companyUuid,contractUuid, contractStatus, contractAction);
    }

    @Override
    public ApiResponse approveContract(String companyUuid, String contractUuid) throws Exception {
        return contractServiceImpl.approveContract(companyUuid,contractUuid);
    }

    @Override
    public ApiResponse recallContract(String companyUuid, String contractUuid) {
        return contractServiceImpl.recallContract(companyUuid,contractUuid);
    }

    @Override
    public ApiResponse issueContract(String companyUuid, String contractUuid) {
        return contractServiceImpl.issueContract(companyUuid,contractUuid);
    }

    @Override
    public ApiResponse terminateContract(String companyUuid, String contractUuid) {
        return contractServiceImpl.terminateContract(companyUuid,contractUuid);
    }

    @Override
    public ApiResponse supplierChangeContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) {
        return contractServiceImpl.supplierChangeContractStatus(companyUuid,contractUuid,contractStatus,contractAction);
    }

    @Override
    public ApiResponse changeRejectandSendBackContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) throws Exception {
        return contractServiceImpl.changeContractStatus(companyUuid,contractUuid, contractStatus, contractAction);
    }
}
