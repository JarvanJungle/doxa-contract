package org.doxa.contract.DTO.contractRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.doxa.contract.DTO.common.AddressDto;
import org.doxa.contract.DTO.common.SupplierInformationDto;
import org.doxa.contract.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCRDto {

    private String contractingEntity;

    private String contractingOwner;

    private String contractTitle;

    private String contractType;

    private boolean isOutSourcingContract;

    private String currencyCode;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant contractStartDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DEFAULT_DATE_TIME_FORMAT, timezone = AppConfig.DEFAULT_TIMEZONE)
    private Instant contractEndDate;

    private String paymentTermName;

    private String paymentTermUuid;
    
    //renewal option: yes, no
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

	private SupplierInformationDto supplierInformation;

    private boolean isConnected;
    
    private String contractRequestUuid;
    
	private List<CRDocumentDto> documentList;
	
	private List<CRItemDto> contractItemList;

	private String procurementType;

	private String note;
	
	public CreateCRDto(CreateCRDtoWithValidation submitDto, List<CRItemDto> contractItemList) {
		this.contractingEntity = submitDto.getContractingEntity();
		this.contractingOwner = submitDto.getContractingOwner();
		this.contractTitle = submitDto.getContractTitle();
		this.contractType = submitDto.getContractType();
		this.isOutSourcingContract = submitDto.isOutSourcingContract();
		this.currencyCode = submitDto.getCurrencyCode();
		this.contractStartDate = submitDto.getContractStartDate();
		this.contractEndDate = submitDto.getContractEndDate();
		this.paymentTermName = submitDto.getPaymentTermName();
		this.paymentTermUuid = submitDto.getPaymentTermUuid();
		this.renewalOption = submitDto.getRenewalOption();
		this.isProject = submitDto.isProject();
		this.projectName = submitDto.getProjectName();
		this.projectUuid = submitDto.getProjectUuid();
		this.projectRfqNo = submitDto.getProjectRfqNo();
		this.deliveryAddress = submitDto.getDeliveryAddress();
		this.totalUsedCurrencyCode =submitDto.getTotalUsedCurrencyCode();
		this.totalUsed = submitDto.getTotalUsed();
		this.productServiceDescription = submitDto.getProductServiceDescription();
		this.approvalRouteUuid = submitDto.getApprovalRouteUuid();
		this.supplierInformation = submitDto.getSupplierInformation();
		this.isConnected = submitDto.isConnected();
		this.contractRequestUuid = submitDto.getContractRequestUuid();
		this.documentList = submitDto.getDocumentList();
		this.contractItemList = contractItemList;
		this.procurementType = submitDto.getProcurementType();
		this.note = submitDto.getNote();
	}
	
	public CreateCRDto(EditCRDtoWithValidation submitDto, List<CRItemDto> contractItemList) {
		this.contractingEntity = submitDto.getContractingEntity();
		this.contractingOwner = submitDto.getContractingOwner();
		this.contractTitle = submitDto.getContractTitle();
		this.contractType = submitDto.getContractType();
		this.isOutSourcingContract = submitDto.isOutSourcingContract();
		this.currencyCode = submitDto.getCurrencyCode();
		this.contractStartDate = submitDto.getContractStartDate();
		this.contractEndDate = submitDto.getContractEndDate();
		this.paymentTermName = submitDto.getPaymentTermName();
		this.paymentTermUuid = submitDto.getPaymentTermUuid();
		this.renewalOption = submitDto.getRenewalOption();
		this.isProject = submitDto.isProject();
		this.projectName = submitDto.getProjectName();
		this.projectUuid = submitDto.getProjectUuid();
		this.projectRfqNo = submitDto.getProjectRfqNo();
		this.deliveryAddress = submitDto.getDeliveryAddress();
		this.totalUsedCurrencyCode =submitDto.getTotalUsedCurrencyCode();
		this.totalUsed = submitDto.getTotalUsed();
		this.productServiceDescription = submitDto.getProductServiceDescription();
		this.approvalRouteUuid = submitDto.getApprovalRouteUuid();
		this.isConnected = submitDto.isConnected();
		this.contractRequestUuid = submitDto.getContractRequestUuid();
		this.documentList = submitDto.getDocumentList();
		this.contractItemList = contractItemList;
		this.procurementType = submitDto.getProcurementType();
		this.note = submitDto.getNote();
	}
}
