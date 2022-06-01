package org.doxa.contract.models.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="contract_audit_trail", schema = "public")
public class ContractAuditTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_uuid")
    private String userUuid;
    @Column(name = "role")
    private String role;
    @Column(name = "current_group")
    private String currentGroup;
    @Column(name = "approval_group_uuid")
    private String approvalGroupUuid;
    @Column(name = "action")
    private String action;
    @Column(name = "status")
    private String status;
    @Column(name = "date")
    private Instant date;
    @Column(name = "contract_id")
    private Long contractId;
}
