package org.doxa.contract.repositories.common;

import org.doxa.contract.models.common.SupplierInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierInformationRepository extends JpaRepository<SupplierInformation, Long> {
    @Query("SELECT b FROM SupplierInformation b WHERE b.supplierCode =:supplierCode AND b.supplierVendorConnectionUuid =:supplierVendorConnectionUuid AND b.supplierCompanyUuid =:supplierCompanyUuid AND b.companyName =:companyName AND b.taxRegNo =:taxRegNo AND b.companyAddress.addressLabel =:addressLabel AND b.companyAddress.addressFirstLine =:addressFirstLine AND b.companyAddress.addressSecondLine =:addressSecondLine AND b.companyAddress.city =:city AND b.companyAddress.state =:state AND b.companyAddress.country =:country AND b.companyAddress.postalCode =:postalCode AND b.contactInformation.contactName =:contactName AND b.contactInformation.contactEmail =:contactEmail AND b.contactInformation.contactNumber =:contactNumber AND b.companyCountry =:companyCountry")
    Optional<SupplierInformation> findByBusinessDomain(@Param("supplierCode")String supplierCode, @Param("supplierVendorConnectionUuid")String supplierVendorConnectionUuid,
                                                    @Param("supplierCompanyUuid")String supplierCompanyUuid, @Param("companyName")String companyName,
                                                    @Param("taxRegNo")String taxRegNo, @Param("addressLabel")String addressLabel, @Param("addressFirstLine")String addressFirstLine,  @Param("addressSecondLine")String addressSecondLine,
                                                    @Param("city")String city, @Param("state")String state, @Param("country")String country, @Param("postalCode")String postalCode, @Param("contactName")String contactName,
                                                    @Param("contactEmail")String contactEmail, @Param("contactNumber")String contactNumber, @Param("companyCountry")String companyCountry);
}
