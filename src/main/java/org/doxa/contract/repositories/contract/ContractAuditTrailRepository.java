package org.doxa.contract.repositories.contract;

import org.doxa.contract.models.contract.ContractAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractAuditTrailRepository extends JpaRepository<ContractAuditTrail, Long> {
    @Query("SELECT  p FROM ContractAuditTrail p where p.contractId =:contractId ORDER BY date DESC")
    List<ContractAuditTrail> auditTrailByContractId(@Param("contractId")Long contractId);

}
