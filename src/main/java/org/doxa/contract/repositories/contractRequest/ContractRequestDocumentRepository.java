package org.doxa.contract.repositories.contractRequest;

import java.util.List;

import org.doxa.contract.models.contractRequest.ContractRequestDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContractRequestDocumentRepository extends JpaRepository<ContractRequestDocument, Long> {
	
	@Query("SELECT c FROM ContractRequestDocument c WHERE c.contractRequest.contractRequestUuid = :contractRequestUuid")
	List<ContractRequestDocument> findByContractRequestUuid(@Param("contractRequestUuid") String contractRequestUuid);

}
