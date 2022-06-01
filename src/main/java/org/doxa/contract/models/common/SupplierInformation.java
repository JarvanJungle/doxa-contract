package org.doxa.contract.models.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="supplier_information", schema = "public")
public class SupplierInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "supplier_code")
    private String supplierCode;
    @Column(name = "supplier_vendor_connection_uuid")
    private String supplierVendorConnectionUuid;
    @Column(name = "supplier_company_uuid")
    private String supplierCompanyUuid;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "tax_reg_no")
    private String taxRegNo;
    @Column(name = "company_country")
    private String companyCountry;
    @ManyToOne()
    @JoinColumn(name = "address_id")
    private Address companyAddress;
    @ManyToOne()
    @JoinColumn(name = "contact_id")
    private ContactInformation contactInformation;

}
