package org.doxa.contract.DTO.contractRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.doxa.contract.DTO.common.AddressDto;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EditCRDtoWithValidation {

    private String contractingEntity;

    private String contractingOwner;

    @NotNull
    private String contractTitle;

    @NotNull
    private String contractType;

    private boolean isOutSourcingContract;

    @NotNull
    private String currencyCode;
    
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant contractStartDate;
    
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant contractEndDate;

    @NotNull
    private String paymentTermName;

    @NotNull
    private String paymentTermUuid;
    
    //renewal option: yes, no
    @NotNull
    private String renewalOption;

    private boolean isProject;

    private String projectName;

    private String projectUuid;

    private String projectRfqNo;

    private AddressDto deliveryAddress;
    
    private String totalUsedCurrencyCode;

    private BigDecimal totalUsed;

    private String productServiceDescription;

    @JsonProperty
    @NotNull
    private String approvalRouteUuid;

    private boolean isConnected;
    
    @NotNull
    private String contractRequestUuid;
    
	private List<CRDocumentDto> documentList;
	
	private List<CreateCRItemDtoWithValidation> contractItemList;

    private String procurementType;

    private String note;
}
