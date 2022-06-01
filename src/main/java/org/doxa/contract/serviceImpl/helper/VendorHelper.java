package org.doxa.contract.serviceImpl.helper;

import org.doxa.contract.exceptions.ObjectDoesNotExistException;
import org.doxa.contract.microservices.DTO.SupplierInfo;
import org.doxa.contract.microservices.DTO.entityService.AddressDetails;
import org.doxa.contract.microservices.DTO.entityService.EntitiesService;
import org.doxa.contract.microservices.DTO.oauth.CompanyDetails;
import org.doxa.contract.microservices.DTO.oauth.OauthService;
import org.doxa.contract.microservices.DTO.oauth.UserDetail;
import org.doxa.contract.models.common.Address;
import org.doxa.contract.models.common.BuyerInformation;
import org.doxa.contract.models.common.ContactInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorHelper {

    @Autowired
    private EntitiesService iEntitiesService;

    @Autowired
    private OauthService oauthService;

    public Address getBuyerAddressDetails(String companyUuid) throws ObjectDoesNotExistException {
        AddressDetails addressDetails = iEntitiesService.getDefaultAddress(companyUuid);
        Address address = new Address();
        address.setAddressLabel(addressDetails.getAddressLabel());
        address.setAddressFirstLine(addressDetails.getAddressFirstLine());
        address.setAddressSecondLine(addressDetails.getAddressSecondLine());
        address.setCity(addressDetails.getCity());
        address.setCountry(addressDetails.getCountry());
        address.setPostalCode(addressDetails.getPostalCode());
        address.setState(addressDetails.getState());
        return address;
    }


    public ContactInformation getContactPerson(String requesterUuid) throws ObjectDoesNotExistException {
        // Contact person is the person who creates PO
        UserDetail buyerContactPerson = oauthService.getUserDetails(requesterUuid);
        ContactInformation contactPerson = new ContactInformation();
        contactPerson.setContactEmail(buyerContactPerson.getEmail());
        contactPerson.setContactNumber(buyerContactPerson.getWorkNumber());
        contactPerson.setContactName(buyerContactPerson.getName());
        return contactPerson;
    }

    public BuyerInformation extractBuyerInformation(SupplierInfo buyer) {
        BuyerInformation buyerInformation = new BuyerInformation();
        buyerInformation.setCompanyName(buyer.getCompanyName());
        buyerInformation.setCompanyCountry(buyer.getCountryOfOrigin());
        buyerInformation.setTaxRegNo(buyer.getGstRegNo());
        buyerInformation.setBuyerCode(buyer.getCompanyCode());
        buyerInformation.setBuyerVendorConnectionUuid(buyer.getUuid());
        return buyerInformation;
    }

    public Address extractAddressFromExternalVendor(SupplierInfo buyer) {
        Address address = new Address();
        address.setAddressLabel(buyer.getAddressLabel());
        address.setAddressFirstLine(buyer.getAddressLine1());
        address.setAddressSecondLine(buyer.getAddressLine2());
        address.setCity(buyer.getCity());
        address.setCountry(buyer.getCountry());
        address.setPostalCode(buyer.getZipCode());
        address.setState(buyer.getState());
        return address;
    }

    public ContactInformation extractContactFromExternalVendor(SupplierInfo buyer) {
        ContactInformation contactPerson = new ContactInformation();
        contactPerson.setContactEmail(buyer.getContactPersonEmail());
        contactPerson.setContactNumber(buyer.getContactPersonNumber());
        contactPerson.setContactName(buyer.getContactPersonName());
        return contactPerson;
    }
}
