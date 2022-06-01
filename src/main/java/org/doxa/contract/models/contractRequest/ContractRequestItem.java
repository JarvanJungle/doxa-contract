package org.doxa.contract.models.contractRequest;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.doxa.contract.DTO.contractRequest.CRItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="contract_request_item", schema = "public")
public class ContractRequestItem {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "item_name")
    private String itemName;
    
    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_model")
    private String itemModel;
    
    @Column(name = "item_size")
    private String itemSize;

    @Column(name = "item_brand")
    private String itemBrand;
    
    @Column(name = "trade")
    private String trade;
    
    @Column(name = "uom_code")
    private String uomCode;
    
    @Column(name = "item_quantity")
    private BigDecimal itemQuantity;

    @Column(name = "currency_code")
    private String currencyCode;
    
    @Column(name = "item_unit_price")
    private BigDecimal itemUnitPrice;
    
    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "tax_code_uuid")
    private String taxCodeUuid;

    @Column(name = "tax_percentage")
    private BigDecimal taxPercentage;
    
    @Column(name = "in_source_currency_before_tax")
    private BigDecimal inSourceCurrencyBeforeTax;
    
    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;
    
    @Column(name = "in_document_currency_before_tax")
    private BigDecimal inDocumentCurrencyBeforeTax;
    
    @Column(name = "in_document_currency_tax_amount")
    private BigDecimal inDocumentCurrencyTaxAmount;

    @Column(name = "in_document_currency_after_tax")
    private BigDecimal inDocumentCurrencyAfterTax;
    
    @Column(name = "delivery_address")
    private String deliveryAddress;
    
    @Column(name = "requested_delivery_date")
    private Instant requestedDeliveryDate;
    
    @Column(name = "gl_account_number")
    private String glAccountNumber;
    
    @Column(name = "gl_account_uuid")
    private String glAccountUuid;
    
    @Column(name = "note")
    private String note;
    
    @Column(name = "manual_item")
    private boolean manualItem;

	@ManyToOne()
	@JoinColumn(name = "contract_request_id")
	private ContractRequest contractRequest;
	
	public ContractRequestItem(CRItemDto itemDto) {
	    this.itemCode = itemDto.getItemCode();
	    this.itemName = itemDto.getItemName();
	    this.itemDescription = itemDto.getItemDescription();
	    this.itemModel = itemDto.getItemModel();
	    this.itemSize = itemDto.getItemSize();
	    this.itemBrand = itemDto.getItemBrand();
	    this.trade = itemDto.getTrade();
	    this.uomCode = itemDto.getUomCode();
	    this.itemQuantity = itemDto.getItemQuantity();
	    this.currencyCode = itemDto.getCurrencyCode();
	    this.itemUnitPrice= itemDto.getItemUnitPrice();
	    this.taxCode = itemDto.getTaxCode();
	    this.taxCodeUuid = itemDto.getTaxCodeUuid();
	    this.taxPercentage = itemDto.getTaxPercentage();
	    this.exchangeRate = itemDto.getExchangeRate();
	    this.deliveryAddress = itemDto.getDeliveryAddress();
	    this.glAccountNumber = itemDto.getGlAccountNumber();
	    this.glAccountUuid = itemDto.getGlAccountUuid();
	    this.note = itemDto.getNote();
	    this.manualItem = itemDto.isManualItem();
	}


}
