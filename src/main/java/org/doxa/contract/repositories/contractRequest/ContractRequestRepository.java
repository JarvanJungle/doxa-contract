package org.doxa.contract.repositories.contractRequest;

import java.util.List;
import java.util.Optional;

import org.doxa.contract.models.contractRequest.ContractRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContractRequestRepository extends JpaRepository<ContractRequest, Long> {

	@Query(nativeQuery = true, value ="SELECT nextval('cr_global')")
    long getNextValMySequence();
	
    @Query("SELECT COUNT(c) FROM ContractRequest c WHERE c.companyUuid =:companyUuid AND c.contractRequestNumber IS NOT NULL")
    int totalRow(@Param("companyUuid")String companyUuid);
	
	List<ContractRequest> findByCompanyUuid(String companyUuid);
	
	Optional<ContractRequest> findByCompanyUuidAndContractRequestUuid(String companyUuid, String contractRequestUuid);
}
