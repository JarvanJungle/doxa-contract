package org.doxa.contract.mapper;

import org.doxa.contract.DTO.contract.*;
import org.doxa.contract.models.contract.Contract;
import org.doxa.contract.models.contractRequest.ContractRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring", uses ={VendorInformationMapper.class, ContractInformationMapper.class, AddressMapper.class})
public interface ContractMapper {
    @Mapping(source ="contractUuid", target = "contractUuid")
    @Mapping(source ="createdDate", target = "updatedDate")
    @Mapping(source ="createdDate", target = "createdDate")
    @Mapping(source ="createdByName", target = "createdByName")
    @Mapping(source ="createdByUuid", target = "createdByUuid")
    @Mapping(source ="contractRequest.connected", target = "connectedVendor")
    @Mapping(target ="id", ignore = true)
    @Mapping(source = "contractRequest.globalCRNumber", target = "globalContractNumber")
    Contract convertToPendingContract(ContractRequest contractRequest, String contractStatus, String contractUuid, Instant createdDate, String createdByName, String createdByUuid);


    Contract contract(ContractDto contractDto);
    ContractDto contractDto(Contract contract);
    @Mapping(source ="contract.supplierInformation.companyName", target = "supplierCompanyName")
    BuyerListingViewDto buyerView(Contract contract);
    @Mapping(source ="contract.buyerInformation.companyName", target = "buyerCompanyName")
    SupplierListingViewDto supplierView(Contract contract);

    List<BuyerListingViewDto> buyerListingView(List<Contract> contracts);
    List<SupplierListingViewDto> supplierListingView(List<Contract> contracts);

    @Mapping(source ="contractNumber", target = "contractNumber")
    @Mapping(source ="contractStatus", target = "contractStatus")
    @Mapping(source ="updatedDate", target = "updatedDate")
    Contract contractSubmission(String contractStatus, ContractSubmissionDto contractSubmissionDto, String contractNumber, Instant updatedDate);
}
