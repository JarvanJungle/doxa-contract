package org.doxa.contract.mapper;

import org.doxa.contract.DTO.contract.ContractAuditTrailDto;
import org.doxa.contract.DTO.contract.ContractDocumentDto;
import org.doxa.contract.microservices.DTO.purchase.RfqDocumentMetaDataDto;
import org.doxa.contract.models.contract.ContractAuditTrail;
import org.doxa.contract.models.contract.ContractDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContractInformationMapper {
    ContractAuditTrail contractAuditTrail(ContractAuditTrailDto contractAuditTrailDto);
    ContractAuditTrailDto contractAuditTrailDto(ContractAuditTrail contractAuditTrail);
    ContractDocument contractDocument(ContractDocumentDto contractDocumentDto);
    ContractDocumentDto contractDocumentDto(ContractDocument contractDocument);

    @Mapping(source = "fileLabel", target = "fileName")
    @Mapping(source = "fileDescription", target = "description")
    @Mapping(source = "uploadedOn", target = "uploadOn")
    @Mapping(source = "uploadedBy", target = "uploadBy")
    ContractDocument fromRfqDocument(RfqDocumentMetaDataDto rfqDocumentMetaDataDto);
}
