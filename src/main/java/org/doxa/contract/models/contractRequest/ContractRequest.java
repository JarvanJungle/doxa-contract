package org.doxa.contract.models.contractRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.*;

import org.doxa.contract.DTO.contractRequest.CreateCRDto;
import org.doxa.contract.enums.ContractRequestStatus;
import org.doxa.contract.models.common.Address;
import org.doxa.contract.models.common.SupplierInformation;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="contract_request", schema = "public")
public class ContractRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_request_number")
    private String contractRequestNumber;

    @Column(name = "global_cr_number")
    private String globalCRNumber;

    @Column(name = "contracting_entity")
    private String contractingEntity;

    @Column(name = "contracting_owner")
    private String contractingOwner;

    @Column(name = "contract_title")
    private String contractTitle;

    @Column(name = "contract_type")
    private String contractType;

    @Column(name = "is_outsourcing_contract")
    private boolean isOutSourcingContract;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "contract_value")
    private BigDecimal contractValue;

    @Column(name = "contract_start_date")
    private Instant contractStartDate;

    @Column(name = "contract_end_date")
    private Instant contractEndDate;

    @Column(name = "payment_term_name")
    private String paymentTermName;

    @Column(name = "payment_term_uuid")
    private String paymentTermUuid;

    //renewal option: yes, no
    @Column(name = "renewal_option")
    private String renewalOption;

    @Column(name = "created_date")
    @Generated(GenerationTime.INSERT)
    private Instant createdDate;

    @Column(name = "submitted_date")
    private Instant submittedDate;

    @Column(name = "created_by_name")
    private String createdByName;

    @Column(name = "created_by_uuid")
    private String createdByUuid;

    @Column(name = "is_project")
    private boolean isProject;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_uuid")
    private String projectUuid;

    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "project_rfq_no")
    private String projectRfqNo;

    @Column(name = "project_delivery_date")
    private Instant projectDeliveryDate;

    @ManyToOne()
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;

    @Column(name = "total_used_currency_code")
    private String totalUsedCurrencyCode;

    @Column(name = "total_used")
    private BigDecimal totalUsed;

    @Column(name = "product_service_description")
    private String productServiceDescription;

    @Column(name = "approval_route_sequence")
    private String approvalRouteSequence;

    @Column(name = "approval_route_name")
    private String approvalRouteName;

    @Column(name = "approval_route_uuid")
    private String approvalRouteUuid;

    @Column(name = "next_approver")
    private String nextApprover;

    @Column(name = "next_approval_group")
    private String nextApprovalGroup;

    @Column(name = "status")
    private String status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "updated_date")
    @Generated(GenerationTime.ALWAYS)
    private Instant updatedDate;

    @Column(name = "approved_date")
    private Instant approvedDate;

    @ManyToOne()
    @JoinColumn(name = "supplier_id")
    private SupplierInformation supplierInformation;

    @Column(name = "is_connected")
    private boolean isConnected;

    @Column(name = "is_converted")
    private boolean isConverted;

    @Column(name = "uuid")
    private String contractRequestUuid;

    @Column(name = "company_uuid")
    private String companyUuid;

    @Column(name = "procurement_type")
    private String procurementType;

    @Column(name = "note")
    private String note;


    public ContractRequest(CreateCRDto createDto, String companyUuid, String createdByUuid, String createdByName) {
        this.contractingEntity = createDto.getContractingEntity();
        this.contractingOwner = createDto.getContractingOwner();
        this.contractTitle = createDto.getContractTitle();
        this.contractType = createDto.getContractType();
        this.isOutSourcingContract = createDto.isOutSourcingContract();
        this.currencyCode = createDto.getCurrencyCode();
        this.contractStartDate = createDto.getContractStartDate();
        this.contractEndDate = createDto.getContractEndDate();
        this.paymentTermName = createDto.getPaymentTermName();
        this.paymentTermUuid = createDto.getPaymentTermUuid();
        this.renewalOption = createDto.getRenewalOption();
        this.createdByName = createdByName;
        this.createdByUuid = createdByUuid;
        this.isProject = createDto.isProject();
        this.productServiceDescription = createDto.getProductServiceDescription();
        this.isConnected = createDto.isConnected();
        this.contractRequestUuid = UUID.randomUUID().toString();
        this.companyUuid = companyUuid;
        this.approvalRouteUuid = createDto.getApprovalRouteUuid();
        this.procurementType = createDto.getProcurementType();
        this.note = createDto.getNote();
    }

    public void reject() {
        setStatus(ContractRequestStatus.REJECTED.getValue());
        setNextApprovalGroup(null);
        setNextApprover(null);
    }

    public void sendBack() {
        setStatus(ContractRequestStatus.SENT_BACK.getValue());
        setNextApprovalGroup(null);
        setNextApprover(null);
    }
}
