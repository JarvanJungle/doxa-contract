package org.doxa.contract.mapper;

import org.doxa.contract.DTO.common.BuyerInformationDto;
import org.doxa.contract.DTO.common.SupplierInformationDto;
import org.doxa.contract.models.common.BuyerInformation;
import org.doxa.contract.models.common.SupplierInformation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses ={ContactInformationMapper.class, AddressMapper.class})
public interface VendorInformationMapper {
    BuyerInformationDto buyerInformationDto(BuyerInformation buyerInformation);
    BuyerInformation buyerInformation(BuyerInformationDto buyerInformationDto);
    SupplierInformationDto supplierInformationDto(SupplierInformation supplierInformation);
    SupplierInformation supplierInformation(SupplierInformationDto supplierInformationDto);
}
