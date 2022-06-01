package org.doxa.contract.microservices.DTO.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.doxa.contract.DTO.common.AddressDto;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RfqDto {
    @JsonProperty
    private String uuid;

    @JsonProperty
    private boolean project;

    @JsonProperty
    private String currencyCode;

    @JsonProperty
    private String currencyName;

    @JsonProperty
    private String rfqNumber;

    @JsonProperty
    private String rfqType;

    @JsonProperty
    private String rfqStatus;

    @JsonProperty
    private String rfqTitle;

    @JsonProperty
    private String projectCode;

    @JsonProperty
    private String projectTitle;

    @JsonProperty
    private String projectUuid;

    @JsonProperty
    private String procurementType;

    @JsonProperty
    private String approvalRouteName;

    @JsonProperty
    private String approvalRouteSequence;

    @JsonProperty
    private String approvalRouteUuid;

    @JsonProperty
    private String nextApprover;

    @JsonProperty
    private String nextApprovalGroupUuid;

    @JsonProperty
    private String requesterUuid;

    @JsonProperty
    private String requesterName;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant submittedDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant updatedDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant validityStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant validityEndDate;

    @JsonProperty
    private String note;

    @JsonProperty
    private String pprUuid;

    @JsonProperty
    private String pprNumber;

    @JsonProperty
    private String prUuid;

    @JsonProperty
    private String prNumber;

    @JsonProperty
    private List<RfqDocumentMetaDataDto> rfqDocumentList;

    @JsonProperty
    private List<QuoteItemDto> quoteItemList;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant deliveryDate;

    @JsonProperty
    private AddressDto deliveryAddress;

    private String requisitionType;
}
