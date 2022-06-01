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
@Table(name="contact_information", schema = "public")
public class ContactInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contact_name")
    private String contactName;
    @Column(name = "contact_email")
    private String contactEmail;
    @Column(name = "contact_number")
    private String contactNumber;

}
