package org.doxa.contract.models.contractRequest;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name="contract_request_audit_trail", schema = "public")
public class ContractRequestAuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid")
    private String userUuid;
    
    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "role")
    private String role;
    
    @Column(name = "action")
    private String action;

    @Column(name = "approval_group")
    private String approvalGroup;

    @Column(name = "approval_group_uuid")
    private String approvalGroupUuid;
    
    @Column(name ="created_date")
    @Generated(GenerationTime.INSERT)
    private Instant createdDate;
    
    @ManyToOne()
    @JoinColumn(name = "contract_request_id")
    private ContractRequest contractRequest;
    
    public ContractRequestAuditTrail(String userName, String userUuid, String role, String action, ContractRequest contractRequest) {
    	this.userUuid = userUuid;
    	this.userName = userName;
    	this.role = role;
    	this.action = action;
    	this.contractRequest = contractRequest;
    }
}
