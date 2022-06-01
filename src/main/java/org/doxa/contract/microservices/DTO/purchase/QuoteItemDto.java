package org.doxa.contract.microservices.DTO.purchase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuoteItemDto {

    @JsonProperty
    @NotNull
    private Long rfqItemId;

    @JsonProperty
    private String itemCode;

    @JsonProperty
    private String itemName;

    @JsonProperty
    private String itemDescription;

    @JsonProperty
    private String itemModel;

    @JsonProperty
    private String itemSize;

    @JsonProperty
    private String itemBrand;

    @JsonProperty
    @NotNull
    private String  currencyCode;

    @JsonProperty
    private String uom;

    @JsonProperty
    @NotNull
    private BigDecimal quotedUnitPrice;

    @JsonProperty
    private BigDecimal itemQuantity;

    @JsonProperty
    private BigDecimal awardedQty;

    @JsonProperty
    @NotNull
    private String taxCode;

    @JsonProperty
    @NotNull
    private BigDecimal taxRate;

    @JsonProperty
    private BigDecimal exchangeRate;

    @JsonProperty
    private String buyerNote;

    @JsonProperty
    private String quoteItemNote;

}
