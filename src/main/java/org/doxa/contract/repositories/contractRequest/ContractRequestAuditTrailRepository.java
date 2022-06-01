package org.doxa.contract.repositories.contractRequest;

import java.util.List;

import org.doxa.contract.models.contractRequest.ContractRequestAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContractRequestAuditTrailRepository extends JpaRepository<ContractRequestAuditTrail, Long> {

	@Query("SELECT c FROM ContractRequestAuditTrail c WHERE c.contractRequest.contractRequestUuid = :contractRequestUuid ORDER BY c.createdDate DESC")
	List<ContractRequestAuditTrail> findByContractRequestUuid(@Param("contractRequestUuid") String contractRequestUuid);
	
	@Query(nativeQuery = true, value = "SELECT c.action FROM contract_request_audit_trail c WHERE c.contract_request_id = :contractRequestId ORDER BY c.created_date DESC LIMIT 1")
    String findLatestCRAuditByCRId(@Param("contractRequestId") Long contractRequestId);
}
