package org.doxa.contract.repositories.contractRequest;

import java.util.List;

import org.doxa.contract.models.contractRequest.ContractRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContractRequestItemRepository extends JpaRepository<ContractRequestItem, Long> {

	@Query("SELECT c FROM ContractRequestItem c WHERE c.contractRequest.contractRequestUuid = :contractRequestUuid")
	List<ContractRequestItem> findByContractRequestUuid(@Param("contractRequestUuid") String contractRequestUuid);
}
