package org.doxa.contract.mapper;

import org.doxa.contract.DTO.contract.ContractItemDto;
import org.doxa.contract.microservices.DTO.purchase.QuoteItemDto;
import org.doxa.contract.models.contract.ContractItem;
import org.doxa.contract.models.contractRequest.ContractRequestItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ContractItemMapper {
    ContractItem contractItem(ContractItemDto contractItemDto);
    ContractItemDto contractItemDto(ContractItem contractItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveryAddress", ignore = true)
    @Mapping(source = "contractRequestItem.itemModel", target = "model")
    @Mapping(source = "contractRequestItem.itemSize", target = "size")
    @Mapping(source = "contractRequestItem.itemBrand", target = "brand")
    @Mapping(source = "contractRequestItem.uomCode", target = "uom")
    @Mapping(source = "contractRequestItem.itemQuantity", target = "qty")
    @Mapping(source = "contractRequestItem.currencyCode", target = "currency")
    @Mapping(source = "contractRequestItem.itemUnitPrice", target = "unitPrice")
    @Mapping(source = "contractRequestItem.inSourceCurrencyBeforeTax", target = "inSourceCurrency")
    @Mapping(source = "contractRequestItem.inDocumentCurrencyBeforeTax", target = "inDocumentCurrency")
    @Mapping(source = "contractRequestItem.inDocumentCurrencyTaxAmount", target = "taxAmount")
    @Mapping(source = "contractRequestItem.inDocumentCurrencyAfterTax", target = "inDocumentCurrencyAfterTax")
    @Mapping(source = "contractRequestItem.glAccountNumber", target = "glAccount")
    @Mapping(source = "contractRequestItem.taxPercentage", target = "taxCodeValue")
    ContractItem contractItemFromContractRequestItem(ContractRequestItem contractRequestItem);

    @Mapping(source = "itemModel", target = "model")
    @Mapping(source = "itemSize", target = "size")
    @Mapping(source = "itemBrand", target = "brand")
    @Mapping(source = "currencyCode", target = "currency")
    @Mapping(source = "quotedUnitPrice", target = "unitPrice")
    @Mapping(source = "awardedQty", target = "qty")
    @Mapping(source = "taxRate", target = "taxCodeValue")
    @Mapping(source = "buyerNote", target = "note")
    ContractItem fromRfqItem(QuoteItemDto quoteItemDto);


}
