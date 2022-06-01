package org.doxa.contract.mapper;

import org.doxa.contract.DTO.common.AddressDto;
import org.doxa.contract.models.common.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto addressDto(Address address);
    Address address(AddressDto addressDto);
}
