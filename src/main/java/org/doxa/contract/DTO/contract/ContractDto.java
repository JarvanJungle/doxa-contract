package org.doxa.contract.DTO.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.doxa.contract.DTO.common.AddressDto;
import org.doxa.contract.DTO.common.BuyerInformationDto;
import org.doxa.contract.DTO.common.SupplierInformationDto;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class ContractDto {
    private String contractNumber;
    private String contractRequestNumber;
    private String contractRequestUuid;
    private String globalContractNumber;
    private String contractTitle;
    private String contractingEntity;
    private String contractingOwner;
    private String contractType;
    @JsonProperty
    private boolean isOutsourcingContract;
    private String currencyCode;
    private BigDecimal contractValue;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant contractStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant contractEndDate;
    private String paymentTermName;
    private String paymentTermUuid;
    private String renewalOption;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant createdDate;
    private String createdByName;
    private String createdByUuid;
    private String natureOfContract;
    private String projectName;
    private String projectCode;
    private String projectUuid;
    private String projectRfqNo;

    private AddressDto deliveryAddress;
    private BigDecimal totalUsed;
    private BigDecimal subTotal;
    private BigDecimal taxTotal;
    private BigDecimal totalAmount;
    private String productServiceDescription;
    @JsonProperty
    private String approvalRouteUuid;

    @JsonProperty
    private String approvalRouteName;

    @JsonProperty
    private String approvalRouteSequence;

    @JsonProperty
    private String nextApprover;

    @JsonProperty
    private String nextApprovalGroup;

    private String contractStatus;
    @JsonProperty
    private boolean contractCreator;
    @JsonProperty
    private boolean approverRole;
    @JsonProperty
    private boolean firstApproved;
    @JsonProperty
    private boolean hasApproved;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant updatedDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant approvedDate;
    @JsonProperty
    private boolean eSignRouting;
    private String contractUuid;
    private BuyerInformationDto buyerInformation;
    private SupplierInformationDto supplierInformation;
    private List<ContractItemDto> items;
    private List<ContractAuditTrailDto> auditTrails;
    private List<ContractDocumentDto> contractDocuments;
    private String procurementType;
    private String note;
}
