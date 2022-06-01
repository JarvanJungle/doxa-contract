package org.doxa.contract.utils;

import org.doxa.contract.models.common.Address;
import org.doxa.contract.models.common.BuyerInformation;
import org.doxa.contract.models.common.ContactInformation;
import org.doxa.contract.models.common.SupplierInformation;
import org.doxa.contract.models.contract.Contract;
import org.doxa.contract.models.contract.ContractItem;
import org.doxa.contract.repositories.common.AddressRepository;
import org.doxa.contract.repositories.common.BuyerInformationRepository;
import org.doxa.contract.repositories.common.ContactInformationRepository;
import org.doxa.contract.repositories.common.SupplierInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ObjectService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BuyerInformationRepository buyerInformationRepository;

    @Autowired
    private ContactInformationRepository contactInformationRepository;

    @Autowired
    private SupplierInformationRepository supplierInformationRepository;

    private Optional<Address> getAddress(Address address){
        return addressRepository.findByBusinessDomain(address.getAddressLabel(), address.getAddressFirstLine(), address.getAddressSecondLine(), address.getCity(), address.getState(), address.getCountry(), address.getPostalCode());
    }

    public Address saveAddress(Address address){
        Optional<Address> add = getAddress(address);
        if (add.isEmpty()){
            return(addressRepository.save(address));
        }
        return (add.get());
    }

    private Optional<BuyerInformation> getBuyerInformation(BuyerInformation b){
        return buyerInformationRepository.findByBusinessDomain(b.getBuyerCode(),b.getBuyerVendorConnectionUuid(),b.getBuyerCompanyUuid(), b.getCompanyName(), b.getTaxRegNo(), b.getCompanyAddress().getAddressLabel(),
                b.getCompanyAddress().getAddressFirstLine(), b.getCompanyAddress().getAddressSecondLine(), b.getCompanyAddress().getCity(), b.getCompanyAddress().getState(),
                b.getCompanyAddress().getCountry(), b.getCompanyAddress().getPostalCode(), b.getContactInformation().getContactName(), b.getContactInformation().getContactEmail(), b.getContactInformation().getContactNumber(), b.getCompanyCountry());
    }

    public BuyerInformation saveBuyerInformation(BuyerInformation b){
        Optional<BuyerInformation> buyerInformation = getBuyerInformation(b);
        BuyerInformation buyer;
        if (buyerInformation.isEmpty()){
            //create a new Address
            b.setCompanyAddress(saveAddress(b.getCompanyAddress()));
            //create a new contact
            b.setContactInformation(saveContactInformation(b.getContactInformation()));
            return(buyerInformationRepository.save(b));
        }
        return (buyerInformation.get());
    }

    private Optional<SupplierInformation> getSupplierInformation(SupplierInformation s) {
        return supplierInformationRepository.findByBusinessDomain(s.getSupplierCode(), s.getSupplierVendorConnectionUuid(),s.getSupplierCompanyUuid(), s.getCompanyName(), s.getTaxRegNo(), s.getCompanyAddress().getAddressLabel(), s.getCompanyAddress().getAddressFirstLine(), s.getCompanyAddress().getAddressSecondLine(), s.getCompanyAddress().getCity(), s.getCompanyAddress().getState(),
                s.getCompanyAddress().getCountry(), s.getCompanyAddress().getPostalCode(), s.getContactInformation().getContactName(), s.getContactInformation().getContactEmail(), s.getContactInformation().getContactNumber(),s.getCompanyCountry());
    }

    public SupplierInformation saveSupplierInformation(SupplierInformation s){
        Optional<SupplierInformation> supplierInformation = getSupplierInformation(s);
        SupplierInformation supplier;
        if (supplierInformation.isEmpty()){
            //create a new Address
            s.setCompanyAddress(saveAddress(s.getCompanyAddress()));
            //create a new contact
            s.setContactInformation(saveContactInformation(s.getContactInformation()));
            return(supplierInformationRepository.save(s));
        }
        return (supplierInformation.get());
    }

    private Optional<ContactInformation> getContactInformation(ContactInformation c){
        return contactInformationRepository.findByBusinessDomain(c.getContactName(), c.getContactEmail(), c.getContactNumber());
    }

    public ContactInformation saveContactInformation(ContactInformation c){
        //if the contact information exist then return it
        Optional<ContactInformation> contactInformation = getContactInformation(c);
        if (contactInformation.isEmpty()){
            return(contactInformationRepository.save(c));
        }
        return(contactInformation.get());
    }

    public ContractItem calculateAmountInItems(ContractItem item){
        item.setInSourceCurrency(item.getQty().multiply(item.getUnitPrice()));
        item.setInDocumentCurrency(item.getInSourceCurrency().multiply(item.getExchangeRate()));
        if(item.getTaxCodeValue()!=null)
        item.setTaxAmount(item.getInDocumentCurrency().multiply(item.getTaxCodeValue().divide(new BigDecimal("100"))));
        item.setInDocumentCurrencyAfterTax(item.getTaxAmount().add(item.getInDocumentCurrency()));
        return item;
    }




}
