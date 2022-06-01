package org.doxa.contract.DTO.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.doxa.contract.DTO.common.AddressDto;
import org.doxa.contract.DTO.common.BuyerInformationDto;
import org.doxa.contract.DTO.common.SupplierInformationDto;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class ContractRequestDto {
    private String contractRequestNumber;
    private String contractRequestUuid;
    private String contractTitle;
    private String contractingEntity;
    private String contractingOwner;
    private String contractType;
    @JsonProperty
    private boolean isOutsourcingContract;
    private String currencyCode;
    private BigDecimal contractValue;
    private BigDecimal totalAmount;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant contractStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant contractEndDate;
    private String paymentTermName;
    private String paymentTermUuid;
    private String renewalOption;
    private String natureOfContract;
    private String projectName;
    private String projectUuid;
    private String projectRfqNo;

    private AddressDto deliveryAddress;
    private BigDecimal totalUsed;
    private String productServiceDescription;
    private String approvalRouteUuid;
    private String approvalRouteName;
    private String approvalRouteSequence;
    @JsonProperty
    private boolean eSignRouting;
    private String contractUuid;
    private BuyerInformationDto buyerInformation;
    private SupplierInformationDto supplierInformation;
    private List<ContractItemDto> items;
    private List<ContractDocumentDto> contractDocuments;
}
