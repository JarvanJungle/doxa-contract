package org.doxa.contract.repositories.common;

import org.doxa.contract.models.common.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.addressLabel =:addressLabel AND a.addressFirstLine =:addressFirstLine AND a.addressSecondLine =:addressSecondLine AND a.city =:city AND a.state =:state AND a.country =:country AND a.postalCode =:postalCode" )
    Optional<Address> findByBusinessDomain(@Param("addressLabel")String addressLabel, @Param("addressFirstLine")String addressFirstLine,  @Param("addressSecondLine")String addressSecondLine,
                                           @Param("city")String city, @Param("state")String state, @Param("country")String country, @Param("postalCode")String postalCode);
}
