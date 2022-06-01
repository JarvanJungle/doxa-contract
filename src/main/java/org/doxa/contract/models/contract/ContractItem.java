package org.doxa.contract.models.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.doxa.contract.models.common.Address;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="contract_item", schema = "public")
public class ContractItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_code")
    private String itemCode;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_description")
    private String itemDescription;
    @Column(name = "model")
    private String model;
    @Column(name = "size")
    private String size;
    @Column(name = "brand")
    private String brand;
    @Column(name = "trade")
    private String trade;
    @Column(name = "uom")
    private String uom;
    @Column(name = "currency")
    private String currency;
    @Column(name = "qty")
    private BigDecimal qty;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "tax_code")
    private String taxCode;
    @Column(name = "tax_code_uuid")
    private String taxCodeUuid;
    @Column(name = "tax_code_value")
    private BigDecimal taxCodeValue;
    @Column(name = "in_source_currency")
    private BigDecimal inSourceCurrency;
    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;
    @Column(name = "in_document_currency")
    private BigDecimal inDocumentCurrency;
    @Column(name = "tax_amount")
    private BigDecimal taxAmount;
    @Column(name = "in_document_currency_after")
    private BigDecimal inDocumentCurrencyAfterTax;
    @ManyToOne()
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;
    @Column(name = "delivery_date")
    private Instant deliveryDate;
    @Column(name = "gl_account")
    private String glAccount;
    @Column(name = "gl_account_uuid")
    private String glAccountUuid;
    @Column(name = "note")
    private String note;
    @Column(name="contract_id")
    private Long contractId;
}
