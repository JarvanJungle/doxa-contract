package org.doxa.contract.DTO.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.doxa.contract.DTO.common.AddressDto;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ContractItemDto {
    private String itemCode;
    private String itemName;
    private String itemDescription;
    private String model;
    private String size;
    private String brand;
    private String trade;
    private String uom;
    private String currency;
    private BigDecimal qty;
    private BigDecimal unitPrice;
    private String taxCode;
    private String taxCodeUuid;
    private BigDecimal taxCodeValue;
    private BigDecimal inSourceCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal taxAmount;
    private BigDecimal inDocumentCurrency;
    private BigDecimal inDocumentCurrencyAfterTax;
    private AddressDto deliveryAddress;

    private String glAccount;
    private String glAccountUuid;
    private String note;
}
