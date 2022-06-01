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
@Table(name="contract_request_document_metadata", schema = "public")
public class ContractRequestDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid")
    private String guid;
    
    @Column(name = "file_label")
    private String fileLabel;
    
    @Column(name = "file_description")
    private String fileDescription;
    
    @Column(name ="uploaded_on")
    @Generated(GenerationTime.INSERT)
    private Instant uploadedOn;
    
    @Column(name = "uploaded_by_name")
    private String uploadedByName;
    
    @Column(name = "uploaded_by_uuid")
    private String uploadedByUuid;
    
    @ManyToOne()
    @JoinColumn(name = "contract_request_id")
    private ContractRequest contractRequest;

    @Column(name = "external_document")
    private boolean externalDocument;
    
    public ContractRequestDocument(String guid, String fileLabel, String fileDescription, String uploadedByName, String uploadedByUuid, ContractRequest contractRequest, boolean externalDocument) {
    	this.guid = guid;
    	this.fileLabel = fileLabel;
    	this.fileDescription = fileDescription;
    	this.uploadedByName = uploadedByName;
    	this.uploadedByUuid = uploadedByUuid;
    	this.contractRequest = contractRequest;
    	this.externalDocument = externalDocument;
    }
}
