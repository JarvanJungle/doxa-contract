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
@Table(name="contract_document", schema = "public")
public class ContractDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "guid")
    private String guid;
    @Column(name = "title")
    private String title;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "description")
    private String description;
    @Column(name = "upload_by")
    private String uploadBy;
    @Column(name = "uploader_uuid")
    private String uploaderUuid;
    @Column(name = "upload_on")
    private Instant uploadOn;
    @Column(name = "updated_on")
    private Instant updatedOn;
    @Column(name = "contract_id")
    private Long contractId;
    @Column(name = "external_document")
    private boolean externalDocument;
    @Column(name = "is_attachment")
    private boolean attachment;
    @Column(name = "is_ct_document")
    private boolean mainDocument;
}
