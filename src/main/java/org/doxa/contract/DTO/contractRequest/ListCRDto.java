package org.doxa.contract.DTO.contractRequest;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ListCRDto {

	private String supplierName;
	
	private String contractRequestNumber;
	
	private String contractTitle;
	
	private String status;

	private String approvalRouteName;

	private String approvalRouteSequence;

	private String nextApprovalGroup;
	
	private String projectName;
	
	private BigDecimal totalAmount;
	
	private BigDecimal totalUsed;
	
	private String createdByName;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
	private Instant createdDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
	private Instant updatedDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
	private Instant approvedDate;
	
	private boolean isConnected;
	
	private String contractRequestUuid;

	private String procurementType;
}
