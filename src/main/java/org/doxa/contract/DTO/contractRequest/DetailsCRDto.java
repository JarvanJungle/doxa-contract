package org.doxa.contract.DTO.contractRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import org.doxa.contract.DTO.common.AddressDto;
import org.doxa.contract.DTO.common.SupplierInformationDto;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailsCRDto {

    private String contractRequestNumber;

    private String globalCRNumber;

    private String contractingEntity;

    private String contractingOwner;

    private String contractTitle;

    private String contractType;

    private boolean isOutSourcingContract;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant submittedDate;

    private String createdByName;

    private String createdByUuid;

    private boolean contractCreator;

    private boolean approverRole;

    private boolean firstApproved;

    private boolean hasApproved;

    private boolean isProject;

    private String projectName;

    private String projectCode;

    private String projectUuid;

    private String projectRfqNo;

    private AddressDto deliveryAddress;

    private String totalUsedCurrencyCode;

    private BigDecimal totalUsed;

    private String productServiceDescription;

    private String approvalRouteUuid;

    private String approvalRouteName;

    private String approvalRouteSequence;

    private String nextApprovalGroup;

    private String status;

    private BigDecimal totalAmount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant updatedDate;

    private SupplierInformationDto supplierInformation;

    private boolean isConnected;

    private boolean isConverted;

	private List<CRDocumentDto> documentList;
	
	private List<CRItemDto> contractItemList;
	
	private List<CRAuditTrailDto> auditTrailList;

    private String procurementType;

    private String note;
}
