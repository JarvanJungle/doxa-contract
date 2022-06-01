package org.doxa.contract.DTO.contractRequest;

import java.math.BigDecimal;
import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CreateCRItemDtoWithValidation {

    @NotNull
    private String itemCode;

    @NotNull
    private String itemName;

    @NotNull
    private String itemDescription;

    private String itemModel;

    private String itemSize;

    private String itemBrand;

    @NotNull
    private String trade;

    @NotNull
    private String uomCode;

    @NotNull
    private BigDecimal itemQuantity;

    @NotNull
    private String currencyCode;

    @NotNull
    private BigDecimal itemUnitPrice;

    @NotNull
    private String taxCode;

    @NotNull
    private String taxCodeUuid;

    @NotNull
    private BigDecimal taxPercentage;

    @NotNull
    private BigDecimal exchangeRate;

    private String deliveryAddress;


    private String glAccountNumber;

    private String glAccountUuid;

    private String note;

    private boolean manualItem;

}
