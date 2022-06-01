package org.doxa.contract.repositories.contract;

import org.doxa.contract.models.contract.ContractDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractDocumentRepository extends JpaRepository<ContractDocument, Long> {
}
