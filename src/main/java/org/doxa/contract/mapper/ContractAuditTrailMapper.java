package org.doxa.contract.mapper;

import org.doxa.contract.DTO.contract.ContractAuditTrailDto;
import org.doxa.contract.models.contract.ContractAuditTrail;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ContractAuditTrailMapper {
    ContractAuditTrail contractAuditTrail(ContractAuditTrailDto contractAuditTrailDto);
    ContractAuditTrailDto contractAuditTrailDto(ContractAuditTrail contractAuditTrail);
    ContractAuditTrail createAuditTrail(String userName, String userUuid, String role, String action, Instant date, Long contractId, String status, String currentGroup);
}
