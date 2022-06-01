package org.doxa.contract.models.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.doxa.contract.models.common.Address;
import org.doxa.contract.models.common.BuyerInformation;
import org.doxa.contract.models.common.SupplierInformation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="contract", schema = "public")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contract_number")
    private String contractNumber;
    @Column(name = "contract_request_number")
    private String contractRequestNumber;
    @Column(name = "contract_request_uuid")
    private String contractRequestUuid;
    @Column(name = "global_contract_number")
    private String globalContractNumber;
    @Column(name = "contract_title")
    private String contractTitle;
    @Column(name = "contracting_entity")
    private String contractingEntity;
    @Column(name = "contracting_owner")
    private String contractingOwner;
    @Column(name = "contract_type")
    private String contractType;
    @Column(name = "is_outsourcing_contract")
    private boolean isOutsourcingContract;
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
    @Column(name = "renewal_option")
    private String renewalOption;
    @Column(name = "created_date")
    private Instant createdDate;
    @Column(name = "created_by_name")
    private String createdByName;
    @Column(name = "created_by_uuid")
    private String createdByUuid;
    @Column(name = "nature_of_contract")
    private String natureOfContract;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "project_uuid")
    private String projectUuid;
    @Column(name = "project_code")
    private String projectCode;
    @Column(name = "project_rfq_no")
    private String projectRfqNo;
    @Column(name = "delivery_date")
    private Instant deliveryDate;
    @ManyToOne()
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;
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
    @Column(name = "contract_status")
    private String contractStatus;
    @Column(name = "updated_date")
    private Instant updatedDate;
    @Column(name = "approved_date")
    private Instant approvedDate;
    @Column(name = "eSignRouting")
    private boolean eSignRouting;
    @Column(name = "contract_uuid")
    private String contractUuid;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Column(name = "sub_total")
    private BigDecimal subTotal;
    @Column(name = "tax_total")
    private BigDecimal taxTotal;
    @Column(name = "issued_by")
    private String issuedBy;
    @Column(name = "issued_date")
    private Instant issuedDate;
    @Column(name = "acknowledge_date")
    private Instant acknowledgeDate;
    @Column(name = "connected_vendor")
    private boolean connectedVendor;
    @Column(name = "acknowledge_offline")
    private boolean acknowledgeOffline;
    @ManyToOne()
    @JoinColumn(name = "buyer_id")
    private BuyerInformation buyerInformation;
    @ManyToOne()
    @JoinColumn(name = "supplier_id")
    private SupplierInformation supplierInformation;
    @OneToMany (mappedBy="contractId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("contractId")
    private List<ContractItem> items;
    @OneToMany (mappedBy="contractId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("contractId")
    private List<ContractAuditTrail> auditTrails;
    @OneToMany (mappedBy="contractId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("contractId")
    private List<ContractDocument> contractDocuments;

    @Column(name = "rfq_uuid")
    private String rfqUuid;
    @Column(name = "rfq_number")
    private String rfqNumber;
    @Column(name = "procurement_type")
    private String procurementType;

    @Column(name = "note")
    private String note;
}
