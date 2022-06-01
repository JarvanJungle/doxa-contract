package org.doxa.contract.repositories.contract;

import org.doxa.contract.models.contract.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    @Query(nativeQuery = true, value ="SELECT nextval('contract_global')")
    long getNextValMySequence();

    @Query("SELECT COUNT(c) FROM Contract c WHERE c.buyerInformation.buyerCompanyUuid=:companyUuid AND c.contractNumber IS NOT NULL")
    int totalRow(@Param("companyUuid")String companyUuid);

    @Query("SELECT c FROM Contract c WHERE c.buyerInformation.buyerCompanyUuid=:companyUuid AND c.contractRequestUuid=:contractRequestUuid")
    Optional<Contract> findByCRUuid(@Param("contractRequestUuid")String contractRequestUuid, @Param("companyUuid")String companyUuid);

    @Query("SELECT c FROM Contract c WHERE c.buyerInformation.buyerCompanyUuid=:companyUuid AND c.contractUuid=:contractUuid")
    Optional<Contract> findContractByBuyerCompanyUuidAndContractUuid(@Param("companyUuid")String companyUuid, @Param("contractUuid")String contractUuid);

    @Query("SELECT c FROM Contract c WHERE c.supplierInformation.supplierCompanyUuid=:companyUuid AND c.contractUuid=:contractUuid")
    Optional<Contract> findContractBySupplierCompanyUuidAndContractUuid(@Param("companyUuid")String companyUuid, @Param("contractUuid")String contractUuid);

    @Query("SELECT c FROM Contract c WHERE c.buyerInformation.buyerCompanyUuid=:companyUuid")
    List<Contract> findAllByCompanyUuid(@Param("companyUuid")String companyUuid);

    @Query("SELECT c FROM Contract c WHERE c.supplierInformation.supplierCompanyUuid=:companyUuid AND c.contractStatus IN ('PENDING_ACKNOWLEDGEMENT', 'COMPLETE')")
    List<Contract> findAllBySupplierCompanyUuid(@Param("companyUuid")String companyUuid);

    @Query("SELECT c FROM Contract c WHERE c.supplierInformation.supplierVendorConnectionUuid=:supplierVendorConnectionUuid")
    List<Contract> findAllBySupplierVendorConnectionUuid(@Param("supplierVendorConnectionUuid")String supplierVendorConnectionUuid);

    @Query("SELECT c FROM Contract c WHERE c.rfqUuid =:rfqUuid and c.supplierInformation.supplierVendorConnectionUuid=:supplierUuid")
    Optional<Contract> findByRfqUuidAndSupplierUuid(@Param("rfqUuid") String rfqUuid, @Param("supplierUuid") String supplierUUid);

    @Query("SELECT COUNT(c.id) FROM Contract c WHERE c.rfqUuid =:rfqUuid and c.buyerInformation.buyerCompanyUuid =:companyUuid")
    int countContractsByRfq(String companyUuid, String rfqUuid);

    @Query("SELECT c FROM Contract c WHERE c.buyerInformation.buyerCompanyUuid=:companyUuid AND c.contractNumber=:contractNumber")
    List<Contract> findContractByBuyerCompanyUuidAndContractNumber(@Param("companyUuid")String companyUuid, @Param("contractNumber")String contractNumber);
}
