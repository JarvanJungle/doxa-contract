package org.doxa.contract.repositories.contract;

import org.doxa.contract.models.contract.ContractItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContractItemRepository extends JpaRepository<ContractItem, Long> {
    @Query("SELECT SUM(c.inDocumentCurrency) FROM ContractItem c WHERE c.contractId=:contractId")
    BigDecimal subTotalOfContract(@Param("contractId")Long contractId);

    @Query("SELECT SUM(c.taxAmount) FROM ContractItem c WHERE c.contractId=:contractId")
    BigDecimal taxTotalForContract(@Param("contractId")Long contractId);

    @Query("SELECT c FROM ContractItem c WHERE c.contractId= :contractId")
    List<ContractItem> findItemsByContractId(@Param("contractId") Long id);

    @Query("SELECT c FROM ContractItem c where c.contractId=:contractId AND c.itemCode=:itemCode")
    Optional<ContractItem> findItemByContractId(@Param("contractId") Long contractId, @Param("itemCode") String itemCode);
}
