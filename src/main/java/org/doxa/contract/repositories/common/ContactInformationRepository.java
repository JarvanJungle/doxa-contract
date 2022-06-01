package org.doxa.contract.repositories.common;

import org.doxa.contract.models.common.ContactInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContactInformationRepository extends JpaRepository<ContactInformation, Long> {
    @Query("SELECT c FROM ContactInformation c WHERE c.contactName =:contactName AND c.contactEmail =:contactEmail AND c.contactNumber =:contactNumber ")
    Optional<ContactInformation> findByBusinessDomain(@Param("contactName")String contactName, @Param("contactEmail")String contactEmail, @Param("contactNumber")String contactNumber);
}
