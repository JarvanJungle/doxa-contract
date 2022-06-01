package org.doxa.contract.mapper;

import org.doxa.contract.DTO.common.ContactInformationDto;
import org.doxa.contract.models.common.ContactInformation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactInformationMapper {
    ContactInformationDto contactInformationDto(ContactInformation contactInformation);
    ContactInformation contactInformation(ContactInformationDto contactInformationDto);

}
