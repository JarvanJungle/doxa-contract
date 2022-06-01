package org.doxa.contract.DTO.contractRequest;

import java.math.BigDecimal;
import java.time.Instant;

import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CRItemDto {

    private String itemCode;

    private String itemName;

    private String itemDescription;

    private String itemModel;

    private String itemSize;

    private String itemBrand;

    private String trade;

    private String uomCode;

    private BigDecimal itemQuantity;

    private String currencyCode;

    private BigDecimal itemUnitPrice;

    private String taxCode;

    private String taxCodeUuid;

    private BigDecimal taxPercentage;

    private BigDecimal exchangeRate;

    private String deliveryAddress;

    private String glAccountNumber;

    private String glAccountUuid;

    private String note;

    private boolean manualItem;
    
    //for retrieving details
    private BigDecimal inSourceCurrencyBeforeTax;

    private BigDecimal inDocumentCurrencyBeforeTax;

    private BigDecimal inDocumentCurrencyTaxAmount;

    private BigDecimal inDocumentCurrencyAfterTax;
	
    public CRItemDto(CreateCRItemDtoWithValidation itemDto) {
    	this.itemCode = itemDto.getItemCode();
    	this.itemName = itemDto.getItemName();
    	this.itemDescription = itemDto.getItemDescription();
    	this.itemModel = itemDto.getItemModel();
    	this.itemSize = itemDto.getItemSize();
    	this.itemBrand = itemDto.getItemBrand();
    	this.trade = itemDto.getTrade();
    	this.uomCode = itemDto.getUomCode();
    	this.itemQuantity = itemDto.getItemQuantity();
    	this.currencyCode = itemDto.getCurrencyCode();
    	this.itemUnitPrice = itemDto.getItemUnitPrice();
    	this.taxCode = itemDto.getTaxCode();
    	this.taxCodeUuid = itemDto.getTaxCodeUuid();
    	this.taxPercentage = itemDto.getTaxPercentage();
    	this.exchangeRate = itemDto.getExchangeRate();
    	this.deliveryAddress = itemDto.getDeliveryAddress();
    	this.glAccountNumber = itemDto.getGlAccountNumber();
    	this.glAccountUuid = itemDto.getGlAccountUuid();
    	this.note = itemDto.getNote();
    	this.manualItem = itemDto.isManualItem();
    }
}
