package org.doxa.contract.controllers.contract;

import lombok.extern.slf4j.Slf4j;
import org.doxa.contract.DTO.contract.ContractSubmissionDto;
import org.doxa.contract.authorization.ContractAuthorization;
import org.doxa.contract.config.ControllerPath;
import org.doxa.contract.enums.ContractActionEnum;
import org.doxa.contract.enums.ContractStatusEnum;
import org.doxa.contract.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping(ControllerPath.CONTRACT)
@Slf4j
public class ContractController {

    @Autowired
    private ContractAuthorization contractService;

    /**
     * @api {post} /:companyUuid/contract/buyer/convert/:contractRequestUuid Convert CR to Contract
     * @apiDescription Convert Contract Request to Contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     *
     * @apiParam {String} companyUuid company uuid
     * @apiParam {String} contractRequestUuid contract request uuid

     * @apiHeader {String} Authorization JWT token
     *
     * @apiSuccessExample Success-Response Example:
     * {
     *     "status": "OK",
     *     "message": "Pending contract for submission created successfully: cdebc9f5-c397-4f97-aafb-c9d54bcc99d2",
     *     "timestamp": 1626459723739,
     *     "statusCode": 0
     * }
     *
     */
    @PostMapping(ControllerPath.BUYER+ControllerPath.CONVERT+ControllerPath.CONTRACT_REQUEST_UUID)
    public ResponseEntity<ApiResponse> convertToContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractRequestUuid") String contractRequestUuid)throws Exception  {
        ApiResponse apiResponse = contractService.convertToContract(companyUuid, contractRequestUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {get} /:companyUuid/contract/buyer/get/contract-uuid/:contractUuid Get the contract details
     * @apiDescription Get Contract Details From Buyer Side
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     *
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     * @apiSuccess {String} contractRequestNumber contract request number
     * @apiSuccess {String} contractRequestUuid contract request uuid
     * @apiSuccess {String} globalContractNumber global contract number to uniquely identify the contract sequentially
     * @apiSuccess {String} contractTitle contract title
     * @apiSuccess {String} contractingEntity contract entity
     * @apiSuccess {String} contractingOwner contract owner
     * @apiSuccess {String} contractType contract type
     * @apiSuccess {boolean} isOutsourcingContract is this outsourcing contract
     * @apiSuccess {String} currencyCode currency code
     * @apiSuccess {BigDecimal} contractValue contract value
     * @apiSuccess {Instant} contractStartDate contract start date
     * @apiSuccess {Instant} contractEndDate contract end date
     * @apiSuccess {String} paymentTermName name of payment term
     * @apiSuccess {String} paymentTermUuid uuid of payment term
     * @apiSuccess {String} createdDate the creation date of the contract
     * @apiSuccess {String} createdByName the creator's name
     * @apiSuccess {String} createdByUuid the creator's uuid
     * @apiSuccess {String} renewalOption renewal option
     * @apiSuccess {String} natureOfContract nature of contract
     * @apiSuccess {String} projectName name of project
     * @apiSuccess {String} projectUuid uuid of project
     * @apiSuccess {String} projectRfqNo Request for Quote Number for project
     * @apiSuccess {Object} deliveryAddress contact address information
     * @apiSuccess {String} deliveryAddress.addressLabel label for the address
     * @apiSuccess {String} deliveryAddress.addressFirstLine the first line of the address
     * @apiSuccess {String} deliveryAddress.addressSecondLine the second line of the address
     * @apiSuccess {String} deliveryAddress.city the city of the address
     * @apiSuccess {String} deliveryAddress.state the state of the address
     * @apiSuccess {String} deliveryAddress.country the country of the address
     * @apiSuccess {String} deliveryAddress.postalCode the postal code or zip code of the address
     * @apiSuccess {BigDecimal} totalUsed total used value
     * @apiSuccess {BigDecimal} subTotal sub total of the contract value
     * @apiSuccess {BigDecimal} taxTotal tax total of the contract value
     * @apiSuccess {BigDecimal} totalAmount total contract value
     * @apiSuccess {String} productServiceDescription uuid of project
     * @apiSuccess {String} approvalRouteName Name of approval route (cost center)
     * @apiSuccess {String} approvalRouteSequence Approval sequence of approval route
     * @apiSuccess {String} approvalRouteUuid Uuid of approval route (cost center)
     * @apiSuccess {String} nextApprovalGroup next approval group
     * @apiSuccess {String} contractStatus the status of the contract
     * @apiSuccess {boolean} contractCreator True if user is creator of the contract
     * @apiSuccess {boolean} approverRole If user has approval role for the contract
     * @apiSuccess {boolean} firstApproved If the contract has been approved for the first time
     * @apiSuccess {boolean} hasApproved If the user already approved the contract
     * @apiSuccess {Instant} updatedDate the last modifition date
     * @apiSuccess {Instant} approvedDate the approval date
     * @apiSuccess {String} contractUuid contract uuid
     * @apiSuccess {boolean} eSignRouting are the vendors connected
     * @apiSuccess {Object} buyerInformation buyer information
     * @apiSuccess {String} buyerInformation.buyerCode buyer code
     * @apiSuccess {String} [buyerInformation.buyerVendorConnectionUuid] buyer uuid that's from the supplier table
     * @apiSuccess {String} buyerInformation.buyerCompanyUuid buyer companyUuid
     * @apiSuccess {String} buyerInformation.companyName the company name
     * @apiSuccess {String} buyerInformation.taxRegNo the company's UEN
     * @apiSuccess {Object} buyerInformation.companyAddress contact address information
     * @apiSuccess {String} buyerInformation.companyAddress.addressLabel label for the address
     * @apiSuccess {String} buyerInformation.companyAddress.addressFirstLine the first line of the address
     * @apiSuccess {String} buyerInformation.companyAddress.addressSecondLine the second line of the address
     * @apiSuccess {String} buyerInformation.companyAddress.city the city of the address
     * @apiSuccess {String} buyerInformation.companyAddress.state the state of the address
     * @apiSuccess {String} buyerInformation.companyAddress.country the country of the address
     * @apiSuccess {String} buyerInformation.companyAddress.postalCode the postal code or zip code of the address
     * @apiSuccess {Object} supplierInformation supplier information
     * @apiSuccess {String} supplierInformation.supplierCode supplier code
     * @apiSuccess {String} supplierInformation.supplierVendorConnectionUuid supplier uuid that's from the supplier table
     * @apiSuccess {String} [supplierInformation.supplierCompanyUuid] supplier companyUuid
     * @apiSuccess {String} supplierInformation.companyName the company name
     * @apiSuccess {String} supplierInformation.taxRegNo the company's UEN
     * @apiSuccess {Object} supplierInformation.companyAddress contact address information
     * @apiSuccess {String} supplierInformation.companyAddress.addressLabel label for the address
     * @apiSuccess {String} supplierInformation.companyAddress.addressFirstLine the first line of the address
     * @apiSuccess {String} supplierInformation.companyAddress.addressSecondLine the second line of the address
     * @apiSuccess {String} supplierInformation.companyAddress.city the city of the address
     * @apiSuccess {String} supplierInformation.companyAddress.state the state of the address
     * @apiSuccess {String} supplierInformation.companyAddress.country the country of the address
     * @apiSuccess {String} supplierInformation.companyAddress.postalCode the postal code or zip code of the address
     * @apiSuccess {Object} supplierInformation.contactInformation contact information
     * @apiSuccess {String} supplierInformation.contactInformation.contactName contact name
     * @apiSuccess {String} supplierInformation.contactInformation.contactEmail contact email
     * @apiSuccess {String} supplierInformation.contactInformation.contactNumber contact number
     * @apiSuccess {Object[]} items contract items
     * @apiSuccess {String} items.itemCode item code
     * @apiSuccess {String} items.itemName the name of the item
     * @apiSuccess {String} items.itemDescription the file name of the item
     * @apiSuccess {String} items.model The model of the item
     * @apiSuccess {String} items.size the size of the item
     * @apiSuccess {String} items.brand the name of the item
     * @apiSuccess {String} items.trade the name of the item
     * @apiSuccess {String} items.uom the unit of measurement used by the item
     * @apiSuccess {String} items.currency the currency the item is expressed in
     * @apiSuccess {BigDecimal} items.qty the name of the document
     * @apiSuccess {BigDecimal} items.unitPrice the name of the document
     * @apiSuccess {String} items.taxCode the name of the document
     * @apiSuccess {String} items.taxCodeUuid the name of the document
     * @apiSuccess {BigDecimal} items.taxCodeValue the name of the document
     * @apiSuccess {BigDecimal} items.inSourceCurrency the name of the document
     * @apiSuccess {BigDecimal} items.exchangeRate the name of the document
     * @apiSuccess {BigDecimal} items.inDocumentCurrency the name of the document
     * @apiSuccess {Object} items.deliveryAddress contact address information
     * @apiSuccess {String} items.deliveryAddress.addressLabel label for the address
     * @apiSuccess {String} items.deliveryAddress.addressFirstLine the first line of the address
     * @apiSuccess {String} items.deliveryAddress.addressSecondLine the second line of the address
     * @apiSuccess {String} items.deliveryAddress.city the city of the address
     * @apiSuccess {String} items.deliveryAddress.state the state of the address
     * @apiSuccess {String} items.deliveryAddress.country the country of the address
     * @apiSuccess {String} items.deliveryAddress.postalCode the postal code or zip code of the address
     * @apiSuccess {String} items.glAccount the name of the document
     * @apiSuccess {String} items.note the name of the document
     * @apiSuccess {Object[]} contractDocuments contract documents
     * @apiSuccess {String} contractDocuments.guid unique identifier for the documents
     * @apiSuccess {String} contractDocuments.title the name of the document
     * @apiSuccess {String} contractDocuments.fileName the file name of the document
     * @apiSuccess {String} contractDocuments.description The description of the document
     * @apiSuccess {String} contractDocuments.uploadBy The user who upload the document
     * @apiSuccess {String} contractDocuments.uploaderUuid The user uuid who upload the document
     * @apiSuccess {Instant} contractDocuments.uploadOn The date the document upload on
     * @apiSuccess {Instant} contractDocuments.updatedOn The date the document updated on
     * @apiSuccess {boolean} externalDocument is external or internal
     * @apiSuccess {boolean} attachment is attachment or contract document
     *
     *
     * {
     *   "status": "OK",
     *   "data": {
     *       "contractNumber": "CT-00000011",
     *       "contractRequestNumber": "CR-005",
     *       "contractRequestUuid": "CR1001240",
     *       "globalContractNumber": null,
     *       "contractTitle": "Contract with NS",
     *       "contractingEntity": "Company 1",
     *       "contractingOwner": "Sarah Han",
     *       "contractType": "General",
     *       "isOutsourcingContract": false,
     *       "currencyCode": "SGD",
     *       "contractValue": 167000,
     *       "contractStartDate": "2021-07-17 02:20:33",
     *       "contractEndDate": "2021-07-17 02:20:33",
     *       "paymentTermName": "30 days",
     *       "paymentTermUuid": "2222",
     *       "renewalOption": "N/A",
     *       "createdDate": "2021-07-17 02:20:33",
     *       "createdByName": "Entity Admin",
     *       "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *       "natureOfContract": "PROJECT",
     *       "projectName": "Project Test 1",
     *       "projectUuid": "32342jk3",
     *       "projectRfqNo": "3232",
     *       "deliveryAddress": {
     *           "addressLabel": "HQ",
     *           "addressFirstLine": "12 Leng Kee Road",
     *           "addressSecondLine": "#12-07 ICA Building",
     *           "city": "Singapore",
     *           "state": "Singapore",
     *           "country": "Singapore",
     *           "postalCode": "353323"
     *       },
     *       "totalUsed": 100,
     *       "subTotal": 181760,
     *       "taxTotal": 12723.2,
     *       "totalAmount": 194483.2,
     *       "productServiceDescription": "Bondex Materials",
     * 		 "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 		 "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 		 "approvalRouteUuid": "11111",
     *       "nextApprovalGroup": null,
     *       "contractStatus": "TERMINATED",
     *       "prCreator": true,
     *       "approverRole": false,
     *       "firstApproved": false,
     *       "hasApproved" : false,
     *       "updatedDate": "2021-07-17 02:20:33",
     *       "approvedDate": null,
     *       "eSignRouting": false,
     *       "contractUuid": "957b84e0-1880-4468-bd7b-3f734c6ce7ca",
     *       "buyerInformation": {
     *           "buyerCode": "123",
     *           "buyerVendorConnectionUuid": "111",
     *           "buyerCompanyUuid": "11111",
     *           "companyName": "BlueStone Pte Ltd",
     *           "taxRegNo": "HJ8768 7679",
     *           "companyCountry": null,
     *           "companyAddress": {
     *               "addressLabel": "Blk 35",
     *               "addressFirstLine": "Blk 35 Mandalay Road",
     *               "addressSecondLine": "#13-37 Mandalay Towers",
     *               "city": "Singapore",
     *               "state": "Singapore",
     *               "country": "Singapore",
     *               "postalCode": "308215"
     *           },
     *           "contactInformation": {
     *               "contactName": "Sarah Lee",
     *               "contactEmail": "SarahLee@getnada.com",
     *               "contactNumber": "8768 7679"
     *           }
     *       },
     *       "supplierInformation": {
     *           "supplierCode": "321",
     *           "supplierVendorConnectionUuid": "222",
     *           "supplierCompanyUuid": "1111",
     *           "companyName": "NetSteel Holdings Pte Ltd",
     *           "taxRegNo": "K9NB9889",
     *           "companyCountry": null,
     *           "companyAddress": {
     *               "id": 2,
     *               "addressLabel": "FDK Singapore",
     *               "addressFirstLine": "4 Leng Kee Road",
     *               "addressSecondLine": "#06-07 SIS Building",
     *               "city": "Singapore",
     *               "state": "Singapore",
     *               "country": "Singapore",
     *               "postalCode": "159088"
     *           },
     *           "contactInformation": {
     *               "id": 1,
     *               "contactName": "Alice Grace",
     *               "contactEmail": "AliceGrace@getnada.com",
     *               "contactNumber": "8709 6437"
     *           }
     *       },
     *       "items": [
     *           {
     *               "itemCode": "RB-002",
     *               "itemName": "Rebar",
     *               "itemDescription": "40mm",
     *               "model": "Reebar",
     *               "size": "40mm",
     *               "brand": "Liebherr",
     *               "trade": "Builder Workes",
     *               "uom": "Ton",
     *               "currency": "SGD",
     *               "qty": 10,
     *               "unitPrice": 1000,
     *               "taxCode": "GST7",
     *               "taxCodeUuid": "abc",
     *               "taxCodeValue": 7,
     *               "inSourceCurrency": 10000,
     *               "exchangeRate": 1,
     *               "taxAmount": 700,
     *               "inDocumentCurrency": 10000,
     *               "inDocumentCurrencyAfterTax": 10700,
     *               "deliveryAddress": {
     *                   "addressLabel": "HQ",
     *                   "addressFirstLine": "12 Leng Kee Road",
     *                   "addressSecondLine": "#12-07 ICA Building",
     *                   "city": "Singapore",
     *                   "state": "Singapore",
     *                   "country": "Singapore",
     *                   "postalCode": "353323"
     *               },
     *               "deliveryDate": "2021-07-17 02:20:33",
     *               "glAccount": "123abc",
     *               "note": "from singapore"
     *           },
     *           {
     *               "itemCode": "RB-002",
     *               "itemName": "Rebar",
     *               "itemDescription": "30mm",
     *               "model": "Reebar",
     *               "size": "30mm",
     *               "brand": "Liebherr",
     *               "trade": "Builder Workes",
     *               "uom": "Ton",
     *               "currency": "MYR",
     *               "qty": 10,
     *               "unitPrice": 900,
     *               "taxCode": "GST7",
     *               "taxCodeUuid": "abc",
     *               "taxCodeValue": 7,
     *               "inSourceCurrency": 9000,
     *               "exchangeRate": 0.32,
     *               "taxAmount": 201.6,
     *               "inDocumentCurrency": 2880,
     *               "inDocumentCurrencyAfterTax": 3081.6,
     *               "deliveryAddress": {
     *                   "addressLabel": "HQ",
     *                   "addressFirstLine": "12 Leng Kee Road",
     *                   "addressSecondLine": "#12-07 ICA Building",
     *                   "city": "Singapore",
     *                   "state": "Singapore",
     *                   "country": "Singapore",
     *                   "postalCode": "353323"
     *               },
     *               "deliveryDate": "2021-07-17 02:20:33",
     *               "glAccount": "123abc",
     *               "note": "from malaysia"
     *           },
     *           {
     *               "itemCode": "RB-003",
     *               "itemName": "Rebar",
     *               "itemDescription": "50mm",
     *               "model": "Reebar",
     *               "size": "50mm",
     *               "brand": "Liebherr",
     *               "trade": "Builder Workes",
     *               "uom": "Ton",
     *               "currency": "USD",
     *               "qty": 10,
     *               "unitPrice": 6000,
     *               "taxCode": "GST7",
     *               "taxCodeUuid": "abc",
     *               "taxCodeValue": 7,
     *               "inSourceCurrency": 60000,
     *               "exchangeRate": 1.3,
     *               "taxAmount": 5460,
     *               "inDocumentCurrency": 78000,
     *               "inDocumentCurrencyAfterTax": 83460,
     *               "deliveryAddress": {
     *                   "addressLabel": "HQ",
     *                   "addressFirstLine": "12 Leng Kee Road",
     *                   "addressSecondLine": "#12-07 ICA Building",
     *                   "city": "Singapore",
     *                   "state": "Singapore",
     *                   "country": "Singapore",
     *                   "postalCode": "353323"
     *               },
     *               "deliveryDate": "2021-07-17 02:20:33",
     *               "glAccount": "123abc",
     *               "note": "from USA"
     *           },
     *           {
     *               "itemCode": "RB-002",
     *               "itemName": "Rebar",
     *               "itemDescription": "40mm",
     *               "model": "Reebar",
     *               "size": "40mm",
     *               "brand": "Liebherr",
     *               "trade": "Builder Workes",
     *               "uom": "Ton",
     *               "currency": "SGD",
     *               "qty": 10,
     *               "unitPrice": 1000,
     *               "taxCode": "GST7",
     *               "taxCodeUuid": "abc",
     *               "taxCodeValue": 7,
     *               "inSourceCurrency": 10000,
     *               "exchangeRate": 1,
     *               "taxAmount": 700,
     *               "inDocumentCurrency": 10000,
     *               "inDocumentCurrencyAfterTax": 10700,
     *               "deliveryAddress": {
     *                   "addressLabel": "HQ",
     *                   "addressFirstLine": "12 Leng Kee Road",
     *                   "addressSecondLine": "#12-07 ICA Building",
     *                   "city": "Singapore",
     *                   "state": "Singapore",
     *                   "country": "Singapore",
     *                   "postalCode": "353323"
     *               },
     *               "deliveryDate": "2021-07-17 02:20:33",
     *               "glAccount": "123abc",
     *               "note": "from singapore"
     *           },
     *           {
     *               "itemCode": "RB-002",
     *               "itemName": "Rebar",
     *               "itemDescription": "30mm",
     *               "model": "Reebar",
     *               "size": "30mm",
     *               "brand": "Liebherr",
     *               "trade": "Builder Workes",
     *               "uom": "Ton",
     *               "currency": "MYR",
     *               "qty": 10,
     *               "unitPrice": 900,
     *               "taxCode": "GST7",
     *               "taxCodeUuid": "abc",
     *               "taxCodeValue": 7,
     *               "inSourceCurrency": 9000,
     *               "exchangeRate": 0.32,
     *               "taxAmount": 201.6,
     *               "inDocumentCurrency": 2880,
     *               "inDocumentCurrencyAfterTax": 3081.6,
     *               "deliveryAddress": {
     *                   "addressLabel": "HQ",
     *                   "addressFirstLine": "12 Leng Kee Road",
     *                   "addressSecondLine": "#12-07 ICA Building",
     *                   "city": "Singapore",
     *                   "state": "Singapore",
     *                   "country": "Singapore",
     *                   "postalCode": "353323"
     *               },
     *               "deliveryDate": "2021-07-17 02:20:33",
     *               "glAccount": "123abc",
     *               "note": "from malaysia"
     *           },
     *           {
     *               "itemCode": "RB-003",
     *               "itemName": "Rebar",
     *               "itemDescription": "50mm",
     *               "model": "Reebar",
     *               "size": "50mm",
     *               "brand": "Liebherr",
     *               "trade": "Builder Workes",
     *               "uom": "Ton",
     *               "currency": "USD",
     *               "qty": 10,
     *               "unitPrice": 6000,
     *               "taxCode": "GST7",
     *               "taxCodeUuid": "abc",
     *               "taxCodeValue": 7,
     *               "inSourceCurrency": 60000,
     *               "exchangeRate": 1.3,
     *               "taxAmount": 5460,
     *               "inDocumentCurrency": 78000,
     *               "inDocumentCurrencyAfterTax": 83460,
     *               "deliveryAddress": {
     *                   "addressLabel": "HQ",
     *                   "addressFirstLine": "12 Leng Kee Road",
     *                   "addressSecondLine": "#12-07 ICA Building",
     *                   "city": "Singapore",
     *                   "state": "Singapore",
     *                   "country": "Singapore",
     *                   "postalCode": "353323"
     *               },
     *               "deliveryDate": "2021-07-17 02:20:33",
     *               "glAccount": "123abc",
     *               "note": "from USA"
     *           }
     *       ],
     *       "auditTrails": [
     *           {
     *               "userName": "Entity Admin",
     *               "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *               "role": "manager",
     *               "action": "CONVERT_TO_CONTRACT",
     *               "date": "2021-07-17 02:20:33"
     *           },
     *           {
     *               "userName": "Entity Admin",
     *               "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *               "role": "manager",
     *               "action": "SUBMIT",
     *               "date": "2021-07-17 02:20:33"
     *           },
     *           {
     *               "userName": "Entity Admin",
     *               "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *               "role": "manager",
     *               "action": "APPROVE",
     *               "date": "2021-07-17 02:20:33"
     *           },
     *           {
     *               "userName": "Entity Admin",
     *               "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *               "role": "manager",
     *               "action": "APPROVE",
     *               "date": "2021-07-17 02:20:33"
     *           },
     *           {
     *               "userName": "Entity Admin",
     *               "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *               "role": "manager",
     *               "action": "APPROVE",
     *               "date": "2021-07-17 02:20:33"
     *           },
     *           {
     *               "userName": "Entity Admin",
     *               "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *               "role": "manager",
     *               "action": "ISSUE",
     *               "date": "2021-07-17 02:20:33"
     *           },
     *           {
     *               "userName": "Entity Admin",
     *               "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *               "role": "manager",
     *               "action": "ACKNOWLEDGE",
     *               "date": "2021-07-17 02:20:33"
     *           },
     *           {
     *               "userName": "Entity Admin",
     *               "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *               "role": "manager",
     *               "action": "TERMINATE",
     *               "date": "2021-07-17 02:20:33"
     *           }
     *       ],
     *       "contractDocuments": [
     *           {
     *               "guid": "333",
     *               "title": "Terms & Conditions",
     *               "fileName": "Terms & Conditions.pdf",
     *               "description": "Contract Terms & Conditions",
     *               "uploadBy": "John Smith",
     *               "uploaderUuid": "1212",
     *               "uploadOn": "2021-07-17 02:20:33",
     *               "updatedOn": "2021-07-17 02:20:33",
     *               "attachment": false
     *           },
     *           {
     *               "guid": "334",
     *               "title": "NDA",
     *               "fileName": "NDA.pdf",
     *               "description": "Non Disclosure Agreement",
     *               "uploadBy": "John Smith",
     *               "uploaderUuid": "1212",
     *               "uploadOn": "2021-07-17 02:20:33",
     *               "updatedOn": "2021-07-17 02:20:33",
     *               "attachment": false
     *           }
     *       ],
     *       "outsourcingContract": false,
     *       "esignRouting": false
     *   },
     *   "timestamp": 1626460870623,
     *   "statusCode": 0
     * }
     */
    @GetMapping(ControllerPath.BUYER+ControllerPath.GET+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> getBuyerContractDetail(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.getBuyerContractDetail(companyUuid, contractUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {get} /:companyUuid/contract/supplier/get/contract-uuid/:contractUuid Get the contract details from supplier side
     * @apiDescription Get Contract Details From Supplier Side
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     *
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     * @apiSuccess {String} contractRequestNumber contract request number
     * @apiSuccess {String} contractRequestUuid contract request uuid
     * @apiSuccess {String} globalContractNumber global contract number to uniquely identify the contract sequentially
     * @apiSuccess {String} contractTitle contract title
     * @apiSuccess {String} contractingEntity contract entity
     * @apiSuccess {String} contractingOwner contract owner
     * @apiSuccess {String} contractType contract type
     * @apiSuccess {boolean} isOutsourcingContract is this outsourcing contract
     * @apiSuccess {String} currencyCode currency code
     * @apiSuccess {BigDecimal} contractValue contract value
     * @apiSuccess {Instant} contractStartDate contract start date
     * @apiSuccess {Instant} contractEndDate contract end date
     * @apiSuccess {String} paymentTermName name of payment term
     * @apiSuccess {String} paymentTermUuid uuid of payment term
     * @apiSuccess {String} createdDate the creation date of the contract
     * @apiSuccess {String} createdByName the creator's name
     * @apiSuccess {String} createdByUuid the creator's uuid
     * @apiSuccess {String} renewalOption renewal option
     * @apiSuccess {String} natureOfContract nature of contract
     * @apiSuccess {String} projectName name of project
     * @apiSuccess {String} projectUuid uuid of project
     * @apiSuccess {String} projectRfqNo Request for Quote Number for project
     * @apiSuccess {Object} deliveryAddress contact address information
     * @apiSuccess {String} deliveryAddress.addressLabel label for the address
     * @apiSuccess {String} deliveryAddress.addressFirstLine the first line of the address
     * @apiSuccess {String} deliveryAddress.addressSecondLine the second line of the address
     * @apiSuccess {String} deliveryAddress.city the city of the address
     * @apiSuccess {String} deliveryAddress.state the state of the address
     * @apiSuccess {String} deliveryAddress.country the country of the address
     * @apiSuccess {String} deliveryAddress.postalCode the postal code or zip code of the address
     * @apiSuccess {BigDecimal} totalUsed total used value
     * @apiSuccess {BigDecimal} subTotal sub total of the contract value
     * @apiSuccess {BigDecimal} taxTotal tax total of the contract value
     * @apiSuccess {BigDecimal} totalAmount total contract value
     * @apiSuccess {String} productServiceDescription uuid of project
     * @apiSuccess {String} approvalRouteName Name of approval route (cost center)
     * @apiSuccess {String} approvalRouteSequence Approval sequence of approval route
     * @apiSuccess {String} approvalRouteUuid Uuid of approval route (cost center)
     * @apiSuccess {String} nextApprovalGroup next approval group
     * @apiSuccess {String} contractStatus the status of the contract
     * @apiSuccess {boolean} prCreator True if user is creator of the purchase requisition
     * @apiSuccess {boolean} approverRole If user has approval role for the purchase requisition
     * @apiSuccess {boolean} firstApproved If the contract  has been approved for the first time
     * @apiSuccess {boolean} hasApproved If the user already approved the contract
     * @apiSuccess {Instant} updatedDate the last modifition date
     * @apiSuccess {Instant} approvedDate the approval date
     * @apiSuccess {String} contractUuid contract uuid
     * @apiSuccess {boolean} eSignRouting are the vendors connected
     * @apiSuccess {Object} buyerInformation buyer information
     * @apiSuccess {String} buyerInformation.buyerCode buyer code
     * @apiSuccess {String} [buyerInformation.buyerVendorConnectionUuid] buyer uuid that's from the supplier table
     * @apiSuccess {String} buyerInformation.buyerCompanyUuid buyer companyUuid
     * @apiSuccess {String} buyerInformation.companyName the company name
     * @apiSuccess {String} buyerInformation.taxRegNo the company's UEN
     * @apiSuccess {Object} buyerInformation.companyAddress contact address information
     * @apiSuccess {String} buyerInformation.companyAddress.addressLabel label for the address
     * @apiSuccess {String} buyerInformation.companyAddress.addressFirstLine the first line of the address
     * @apiSuccess {String} buyerInformation.companyAddress.addressSecondLine the second line of the address
     * @apiSuccess {String} buyerInformation.companyAddress.city the city of the address
     * @apiSuccess {String} buyerInformation.companyAddress.state the state of the address
     * @apiSuccess {String} buyerInformation.companyAddress.country the country of the address
     * @apiSuccess {String} buyerInformation.companyAddress.postalCode the postal code or zip code of the address
     * @apiSuccess {Object} supplierInformation supplier information
     * @apiSuccess {String} supplierInformation.supplierCode supplier code
     * @apiSuccess {String} supplierInformation.supplierVendorConnectionUuid supplier uuid that's from the supplier table
     * @apiSuccess {String} [supplierInformation.supplierCompanyUuid] supplier companyUuid
     * @apiSuccess {String} supplierInformation.companyName the company name
     * @apiSuccess {String} supplierInformation.taxRegNo the company's UEN
     * @apiSuccess {Object} supplierInformation.companyAddress contact address information
     * @apiSuccess {String} supplierInformation.companyAddress.addressLabel label for the address
     * @apiSuccess {String} supplierInformation.companyAddress.addressFirstLine the first line of the address
     * @apiSuccess {String} supplierInformation.companyAddress.addressSecondLine the second line of the address
     * @apiSuccess {String} supplierInformation.companyAddress.city the city of the address
     * @apiSuccess {String} supplierInformation.companyAddress.state the state of the address
     * @apiSuccess {String} supplierInformation.companyAddress.country the country of the address
     * @apiSuccess {String} supplierInformation.companyAddress.postalCode the postal code or zip code of the address
     * @apiSuccess {Object} supplierInformation.contactInformation contact information
     * @apiSuccess {String} supplierInformation.contactInformation.contactName contact name
     * @apiSuccess {String} supplierInformation.contactInformation.contactEmail contact email
     * @apiSuccess {String} supplierInformation.contactInformation.contactNumber contact number
     * @apiSuccess {Object[]} items contract items
     * @apiSuccess {String} items.itemCode item code
     * @apiSuccess {String} items.itemName the name of the item
     * @apiSuccess {String} items.itemDescription the file name of the item
     * @apiSuccess {String} items.model The model of the item
     * @apiSuccess {String} items.size the size of the item
     * @apiSuccess {String} items.brand the name of the item
     * @apiSuccess {String} items.trade the name of the item
     * @apiSuccess {String} items.uom the unit of measurement used by the item
     * @apiSuccess {String} items.currency the currency the item is expressed in
     * @apiSuccess {BigDecimal} items.qty the name of the document
     * @apiSuccess {BigDecimal} items.unitPrice the name of the document
     * @apiSuccess {String} items.taxCode the name of the document
     * @apiSuccess {String} items.taxCodeUuid the name of the document
     * @apiSuccess {BigDecimal} items.taxCodeValue the name of the document
     * @apiSuccess {BigDecimal} items.inSourceCurrency the name of the document
     * @apiSuccess {BigDecimal} items.exchangeRate the name of the document
     * @apiSuccess {BigDecimal} items.inDocumentCurrency the name of the document
     * @apiSuccess {Instant} items.deliveryDate the name of the document
     * @apiSuccess {Object} items.deliveryAddress contact address information
     * @apiSuccess {String} items.deliveryAddress.addressLabel label for the address
     * @apiSuccess {String} items.deliveryAddress.addressFirstLine the first line of the address
     * @apiSuccess {String} items.deliveryAddress.addressSecondLine the second line of the address
     * @apiSuccess {String} items.deliveryAddress.city the city of the address
     * @apiSuccess {String} items.deliveryAddress.state the state of the address
     * @apiSuccess {String} items.deliveryAddress.country the country of the address
     * @apiSuccess {String} items.deliveryAddress.postalCode the postal code or zip code of the address
     * @apiSuccess {String} items.glAccount the name of the document
     * @apiSuccess {String} items.note the name of the document
     * @apiSuccess {Object[]} contractDocuments contract documents
     * @apiSuccess {String} contractDocuments.guid unique identifier for the documents
     * @apiSuccess {String} contractDocuments.title the name of the document
     * @apiSuccess {String} contractDocuments.fileName the file name of the document
     * @apiSuccess {String} contractDocuments.description The description of the document
     * @apiSuccess {String} contractDocuments.uploadBy The user who upload the document
     * @apiSuccess {String} contractDocuments.uploaderUuid The user uuid who upload the document
     * @apiSuccess {Instant} contractDocuments.uploadOn The date the document upload on
     * @apiSuccess {Instant} contractDocuments.updatedOn The date the document updated on
     * @apiSuccess {boolean} externalDocument is external or internal
     * @apiSuccess {boolean} attachment is attachment or contract document
     *
     * "status": "OK",
     *     "data": {
     *         "contractNumber": "CT-00000012",
     *         "contractRequestNumber": "CR-005",
     *         "contractRequestUuid": "CR1001241",
     *         "globalContractNumber": null,
     *         "contractTitle": "Contract with NS",
     *         "contractingEntity": "Company 1",
     *         "contractingOwner": "Sarah Han",
     *         "contractType": "General",
     *         "isOutsourcingContract": false,
     *         "currencyCode": "SGD",
     *         "contractValue": 167000,
     *         "contractStartDate": "2021-07-17 02:20:33",
     *         "contractEndDate": "2021-07-17 02:20:33",
     *         "paymentTermName": "30 days",
     *         "paymentTermUuid": "2222",
     *         "renewalOption": "N/A",
     *         "createdDate": "2021-07-17 02:20:33",
     *         "createdByName": "Entity Admin",
     *         "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *         "natureOfContract": "PROJECT",
     *         "projectName": "Project Test 1",
     *         "projectUuid": "32342jk3",
     *         "projectRfqNo": "3232",
     *         "deliveryAddress": {
     *             "addressLabel": "HQ",
     *             "addressFirstLine": "12 Leng Kee Road",
     *             "addressSecondLine": "#12-07 ICA Building",
     *             "city": "Singapore",
     *             "state": "Singapore",
     *             "country": "Singapore",
     *             "postalCode": "353323"
     *         },
     *         "totalUsed": 100,
     *         "subTotal": 181760,
     *         "taxTotal": 12723.2,
     *         "totalAmount": 194483.2,
     *         "productServiceDescription": "Bondex Materials",
     *         "approvalMatrixUuid": "3232",
     *         "approvalMatrixName": "Contract Approval",
     *         "approvalSequence": "Group2(1) > Group1(1) > Group4(1)",
     *         "nextApprovalGroup": null,
     *         "contractStatus": "REJECTED",
     *         "prCreator": true,
     *         "approverRole": false,
     *         "firstApproved"	: false,
     *         "hasApproved" : false,
     *         "updatedDate": "2021-07-17 02:20:33",
     *         "approvedDate": null,
     *         "eSignRouting": false,
     *         "contractUuid": "cdebc9f5-c397-4f97-aafb-c9d54bcc99d2",
     *         "buyerInformation": {
     *             "buyerCode": "123",
     *             "buyerVendorConnectionUuid": "111",
     *             "buyerCompanyUuid": "11111",
     *             "companyName": "BlueStone Pte Ltd",
     *             "taxRegNo": "HJ8768 7679",
     *             "companyCountry": null,
     *             "companyAddress": {
     *                 "addressLabel": "Blk 35",
     *                 "addressFirstLine": "Blk 35 Mandalay Road",
     *                 "addressSecondLine": "#13-37 Mandalay Towers",
     *                 "city": "Singapore",
     *                 "state": "Singapore",
     *                 "country": "Singapore",
     *                 "postalCode": "308215"
     *             },
     *             "contactInformation": {
     *                 "contactName": "Sarah Lee",
     *                 "contactEmail": "SarahLee@getnada.com",
     *                 "contactNumber": "8768 7679"
     *             }
     *         },
     *         "supplierInformation": {
     *             "supplierCode": "321",
     *             "supplierVendorConnectionUuid": "222",
     *             "supplierCompanyUuid": "1111",
     *             "companyName": "NetSteel Holdings Pte Ltd",
     *             "taxRegNo": "K9NB9889",
     *             "companyCountry": null,
     *             "companyAddress": {
     *                 "id": 2,
     *                 "addressLabel": "FDK Singapore",
     *                 "addressFirstLine": "4 Leng Kee Road",
     *                 "addressSecondLine": "#06-07 SIS Building",
     *                 "city": "Singapore",
     *                 "state": "Singapore",
     *                 "country": "Singapore",
     *                 "postalCode": "159088"
     *             },
     *             "contactInformation": {
     *                 "id": 1,
     *                 "contactName": "Alice Grace",
     *                 "contactEmail": "AliceGrace@getnada.com",
     *                 "contactNumber": "8709 6437"
     *             }
     *         },
     *         "items": [
     *             {
     *                 "itemCode": "RB-002",
     *                 "itemName": "Rebar",
     *                 "itemDescription": "40mm",
     *                 "model": "Reebar",
     *                 "size": "40mm",
     *                 "brand": "Liebherr",
     *                 "trade": "Builder Workes",
     *                 "uom": "Ton",
     *                 "currency": "SGD",
     *                 "qty": 10,
     *                 "unitPrice": 1000,
     *                 "taxCode": "GST7",
     *                 "taxCodeUuid": "abc",
     *                 "taxCodeValue": 7,
     *                 "inSourceCurrency": 10000,
     *                 "exchangeRate": 1,
     *                 "taxAmount": 700,
     *                 "inDocumentCurrency": 10000,
     *                 "inDocumentCurrencyAfterTax": 10700,
     *                 "deliveryAddress": {
     *                     "addressLabel": "HQ",
     *                     "addressFirstLine": "12 Leng Kee Road",
     *                     "addressSecondLine": "#12-07 ICA Building",
     *                     "city": "Singapore",
     *                     "state": "Singapore",
     *                     "country": "Singapore",
     *                     "postalCode": "353323"
     *                 },
     *                 "deliveryDate": "2021-07-17 02:20:33",
     *                 "glAccount": "123abc",
     *                 "note": "from singapore"
     *             },
     *             {
     *                 "itemCode": "RB-002",
     *                 "itemName": "Rebar",
     *                 "itemDescription": "30mm",
     *                 "model": "Reebar",
     *                 "size": "30mm",
     *                 "brand": "Liebherr",
     *                 "trade": "Builder Workes",
     *                 "uom": "Ton",
     *                 "currency": "MYR",
     *                 "qty": 10,
     *                 "unitPrice": 900,
     *                 "taxCode": "GST7",
     *                 "taxCodeUuid": "abc",
     *                 "taxCodeValue": 7,
     *                 "inSourceCurrency": 9000,
     *                 "exchangeRate": 0.32,
     *                 "taxAmount": 201.6,
     *                 "inDocumentCurrency": 2880,
     *                 "inDocumentCurrencyAfterTax": 3081.6,
     *                 "deliveryAddress": {
     *                     "addressLabel": "HQ",
     *                     "addressFirstLine": "12 Leng Kee Road",
     *                     "addressSecondLine": "#12-07 ICA Building",
     *                     "city": "Singapore",
     *                     "state": "Singapore",
     *                     "country": "Singapore",
     *                     "postalCode": "353323"
     *                 },
     *                 "deliveryDate": "2021-07-17 02:20:33",
     *                 "glAccount": "123abc",
     *                 "note": "from malaysia"
     *             },
     *             {
     *                 "itemCode": "RB-003",
     *                 "itemName": "Rebar",
     *                 "itemDescription": "50mm",
     *                 "model": "Reebar",
     *                 "size": "50mm",
     *                 "brand": "Liebherr",
     *                 "trade": "Builder Workes",
     *                 "uom": "Ton",
     *                 "currency": "USD",
     *                 "qty": 10,
     *                 "unitPrice": 6000,
     *                 "taxCode": "GST7",
     *                 "taxCodeUuid": "abc",
     *                 "taxCodeValue": 7,
     *                 "inSourceCurrency": 60000,
     *                 "exchangeRate": 1.3,
     *                 "taxAmount": 5460,
     *                 "inDocumentCurrency": 78000,
     *                 "inDocumentCurrencyAfterTax": 83460,
     *                 "deliveryAddress": {
     *                     "addressLabel": "HQ",
     *                     "addressFirstLine": "12 Leng Kee Road",
     *                     "addressSecondLine": "#12-07 ICA Building",
     *                     "city": "Singapore",
     *                     "state": "Singapore",
     *                     "country": "Singapore",
     *                     "postalCode": "353323"
     *                 },
     *                 "deliveryDate": "2021-07-17 02:20:33",
     *                 "glAccount": "123abc",
     *                 "note": "from USA"
     *             },
     *             {
     *                 "itemCode": "RB-002",
     *                 "itemName": "Rebar",
     *                 "itemDescription": "40mm",
     *                 "model": "Reebar",
     *                 "size": "40mm",
     *                 "brand": "Liebherr",
     *                 "trade": "Builder Workes",
     *                 "uom": "Ton",
     *                 "currency": "SGD",
     *                 "qty": 10,
     *                 "unitPrice": 1000,
     *                 "taxCode": "GST7",
     *                 "taxCodeUuid": "abc",
     *                 "taxCodeValue": 7,
     *                 "inSourceCurrency": 10000,
     *                 "exchangeRate": 1,
     *                 "taxAmount": 700,
     *                 "inDocumentCurrency": 10000,
     *                 "inDocumentCurrencyAfterTax": 10700,
     *                 "deliveryAddress": {
     *                     "addressLabel": "HQ",
     *                     "addressFirstLine": "12 Leng Kee Road",
     *                     "addressSecondLine": "#12-07 ICA Building",
     *                     "city": "Singapore",
     *                     "state": "Singapore",
     *                     "country": "Singapore",
     *                     "postalCode": "353323"
     *                 },
     *                 "deliveryDate": "2021-07-17 02:20:33",
     *                 "glAccount": "123abc",
     *                 "note": "from singapore"
     *             },
     *             {
     *                 "itemCode": "RB-002",
     *                 "itemName": "Rebar",
     *                 "itemDescription": "30mm",
     *                 "model": "Reebar",
     *                 "size": "30mm",
     *                 "brand": "Liebherr",
     *                 "trade": "Builder Workes",
     *                 "uom": "Ton",
     *                 "currency": "MYR",
     *                 "qty": 10,
     *                 "unitPrice": 900,
     *                 "taxCode": "GST7",
     *                 "taxCodeUuid": "abc",
     *                 "taxCodeValue": 7,
     *                 "inSourceCurrency": 9000,
     *                 "exchangeRate": 0.32,
     *                 "taxAmount": 201.6,
     *                 "inDocumentCurrency": 2880,
     *                 "inDocumentCurrencyAfterTax": 3081.6,
     *                 "deliveryAddress": {
     *                     "addressLabel": "HQ",
     *                     "addressFirstLine": "12 Leng Kee Road",
     *                     "addressSecondLine": "#12-07 ICA Building",
     *                     "city": "Singapore",
     *                     "state": "Singapore",
     *                     "country": "Singapore",
     *                     "postalCode": "353323"
     *                 },
     *                 "deliveryDate": "2021-07-17 02:20:33",
     *                 "glAccount": "123abc",
     *                 "note": "from malaysia"
     *             },
     *             {
     *                 "itemCode": "RB-003",
     *                 "itemName": "Rebar",
     *                 "itemDescription": "50mm",
     *                 "model": "Reebar",
     *                 "size": "50mm",
     *                 "brand": "Liebherr",
     *                 "trade": "Builder Workes",
     *                 "uom": "Ton",
     *                 "currency": "USD",
     *                 "qty": 10,
     *                 "unitPrice": 6000,
     *                 "taxCode": "GST7",
     *                 "taxCodeUuid": "abc",
     *                 "taxCodeValue": 7,
     *                 "inSourceCurrency": 60000,
     *                 "exchangeRate": 1.3,
     *                 "taxAmount": 5460,
     *                 "inDocumentCurrency": 78000,
     *                 "inDocumentCurrencyAfterTax": 83460,
     *                 "deliveryAddress": {
     *                     "addressLabel": "HQ",
     *                     "addressFirstLine": "12 Leng Kee Road",
     *                     "addressSecondLine": "#12-07 ICA Building",
     *                     "city": "Singapore",
     *                     "state": "Singapore",
     *                     "country": "Singapore",
     *                     "postalCode": "353323"
     *                 },
     *                 "deliveryDate": "2021-07-17 02:20:33",
     *                 "glAccount": "123abc",
     *                 "note": "from USA"
     *             }
     *         ],
     *         "auditTrails": [
     *             {
     *                 "userName": "Entity Admin",
     *                 "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *                 "role": "manager",
     *                 "action": "CONVERT_TO_CONTRACT",
     *                 "date": "2021-07-17 02:20:33"
     *             },
     *             {
     *                 "userName": "Entity Admin",
     *                 "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *                 "role": "manager",
     *                 "action": "SUBMIT",
     *                 "date": "2021-07-17 02:20:33"
     *             },
     *             {
     *                 "userName": "Entity Admin",
     *                 "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *                 "role": "manager",
     *                 "action": "APPROVE",
     *                 "date": "2021-07-17 02:20:33"
     *             },
     *             {
     *                 "userName": "Entity Admin",
     *                 "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *                 "role": "manager",
     *                 "action": "APPROVE",
     *                 "date": "2021-07-17 02:20:33"
     *             },
     *             {
     *                 "userName": "Entity Admin",
     *                 "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *                 "role": "manager",
     *                 "action": "APPROVE",
     *                 "date": "2021-07-17 02:20:33"
     *             },
     *             {
     *                 "userName": "Entity Admin",
     *                 "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *                 "role": "manager",
     *                 "action": "ISSUE",
     *                 "date": "2021-07-17 02:20:33"
     *             },
     *             {
     *                 "userName": "Entity Admin",
     *                 "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *                 "role": "manager",
     *                 "action": "REJECT",
     *                 "date": "2021-07-17 02:20:33"
     *             }
     *         ],
     *         "contractDocuments": [
     *             {
     *                 "guid": "333",
     *                 "title": "Terms & Conditions",
     *                 "fileName": "Terms & Conditions.pdf",
     *                 "description": "Contract Terms & Conditions",
     *                 "uploadBy": "John Smith",
     *                 "uploaderUuid": "1212",
     *                 "uploadOn": "2021-07-17 02:20:33",
     *                 "updatedOn": "2021-07-17 02:20:33",
     *                 "attachment": true
     *             },
     *             {
     *                 "guid": "334",
     *                 "title": "NDA",
     *                 "fileName": "NDA.pdf",
     *                 "description": "Non Disclosure Agreement",
     *                 "uploadBy": "John Smith",
     *                 "uploaderUuid": "1212",
     *                 "uploadOn": "2021-07-17 02:20:33",
     *                 "updatedOn": "2021-07-17 02:20:33",
     *                 "attachment": true
     *             }
     *         ],
     *         "outsourcingContract": false,
     *         "esignRouting": false
     *     },
     *     "timestamp": 1626459823157,
     *     "statusCode": 0
     * }
     */
    @GetMapping(ControllerPath.SUPPLIER+ControllerPath.GET+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> getSupplierContractDetail(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.getSupplierContractDetail(companyUuid, contractUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {get} /:companyUuid/contract/buyer/list list the contracts of the buyers
     * @apiDescription list the contracts of the buyers
     * @apiDescription @author Andrew
     * @apiGroup Manage Contracts
     * @apiVersion 0.0.1
     *
     * @apiParam {String} companyUuid company's unique ID
     *
     * @apiSuccess {String} supplierCompanyName supplier company name
     * @apiSuccess {String} contractNumber contract number
     * @apiSuccess {String} contractUuid contract uuid
     * @apiSuccess {String} contractTitle contract title
     * @apiSuccess {String} contractStatus contract status
     * @apiSuccess {String} approvalRouteName Name of approval route (cost center)
     * @apiSuccess {String} approvalRouteSequence Approval sequence of approval route
     * @apiSuccess {String} nextApprover Approval group for the current allocated approvers
     * @apiSuccess {String} projectName the name of the project
     * @apiSuccess {BigDecimal} totalAmount the total amount of the contract
     * @apiSuccess {BigDecimal} totalUsed the total used amount
     * @apiSuccess {String} issuedBy the user who issue the contract
     * @apiSuccess {String} createdByName the name of the creator
     * @apiSuccess {String} createdByUuid the uuid of the creator
     * @apiSuccess {Instant} issuedDate the date the contract was issued
     * @apiSuccess {Instant} createdDate the date the contract was created
     * @apiSuccess {Instant} updatedDate the date the contract was last updated
     * @apiSuccess {Instant} approvedDate the date the contract was approved
     * @apiSuccess {Instant} acknowledgeDate the date the contract was acknowledge
     * @apiSuccess {boolean} acknowledgeOffline was the contract acknowledge offline
     *
     * @apiSuccessExample Success-Response Example:
     * "status": "OK",
     *     "data": [
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000001",
     *             "contractUuid": "ff1ba6ab-0a49-40b6-a469-f00f92ea9c4b",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "PENDING_SUBMISSION",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": null,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000002",
     *             "contractUuid": "41b99d3b-fe18-45bd-a4d0-7c2f1fd37e08",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "PENDING_SUBMISSION",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": null,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000003",
     *             "contractUuid": "b2bb206f-03ef-4bf2-856d-293562534ff6",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "PENDING_SUBMISSION",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": null,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000005",
     *             "contractUuid": "3e4b4c6e-b3a0-4c85-8217-75d7983d1a22",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "PENDING_APPROVAL",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 388966.4,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000006",
     *             "contractUuid": "78bcc970-5104-4b12-82d0-ef4c4c5472aa",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "PENDING_ISSUE",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 486208,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000006",
     *             "contractUuid": "27ae3e68-c488-40fa-b37c-50fa812f0910",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "REJECTED",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 194483.2,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000008",
     *             "contractUuid": "4d262a41-be18-44f3-8278-22999edc21b0",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "REJECTED",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 291724.8,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000008",
     *             "contractUuid": "09c4d819-5cdc-4126-b4ab-41faec24fd2a",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "PENDING_SUBMISSION",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 194483.2,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000009",
     *             "contractUuid": "f905442c-df0f-41cc-8f53-77a817796369",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "COMPLETE",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 194483.2,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000010",
     *             "contractUuid": "850f102f-da9b-48b5-a8ea-a880b4d1fcf8",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "COMPLETE",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 194483.2,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000011",
     *             "contractUuid": "957b84e0-1880-4468-bd7b-3f734c6ce7ca",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "TERMINATED",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 194483.2,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         },
     *         {
     *             "supplierCompanyName": "NetSteel Holdings Pte Ltd",
     *             "contractNumber": "CT-00000012",
     *             "contractUuid": "cdebc9f5-c397-4f97-aafb-c9d54bcc99d2",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "REJECTED",
     * 			   "approvalRouteName": "PEARL BANK DRAWDOWN01",
     * 			   "approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
     * 			   "nextApprover": "Finance Group (1)",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 194483.2,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null,
     *             "acknowledgeOffline": false
     *         }
     *     ],
     *     "timestamp": 1626865283805,
     *     "statusCode": 0
     * }
     */
    @GetMapping(ControllerPath.BUYER+ControllerPath.LIST)
    public ResponseEntity<ApiResponse> listBuyerContracts(@PathVariable("companyUuid") @NotEmpty String companyUuid) throws Exception {
        ApiResponse apiResponse = contractService.listBuyerContracts(companyUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {get} /:companyUuid/contract/supplier/list list the contracts of the supplier
     * @apiDescription list the contracts of the supplier
     * @apiDescription @author Andrew
     * @apiGroup Manage Contracts
     * @apiVersion 0.0.1
     *
     * @apiParam {String} companyUuid company's unique ID
     * @apiSuccess {String} buyerCompanyName buyer company name
     * @apiSuccess {String} contractNumber the contract number
     * @apiSuccess {String} contractUuid the uuid of the contract
     * @apiSuccess {String} contractTitle the title of the contract
     * @apiSuccess {String} contractStatus the status of the contract
     * @apiSuccess {String} projectName the name of the project
     * @apiSuccess {BigDecimal} totalAmount the total value of the contract
     * @apiSuccess {BigDecimal} totalUsed the total amount used
     * @apiSuccess {String} issuedBy the user that issued the contract
     * @apiSuccess {String} createdByName the user that created the contract
     * @apiSuccess {String} createdByUuid the user uuid that created the contract
     * @apiSuccess {Instant} issuedDate the date the contract is issued
     * @apiSuccess {Instant} createdDate the date the contract is created
     * @apiSuccess {Instant} updatedDate the last modified date of the contract
     * @apiSuccess {Instant} approvedDate the approval date of the contract
     * @apiSuccess {Instant} acknowledgeDate the acknowledge date of the contract
     *
     * {
     *     "status": "OK",
     *     "data": [
     *         {
     *             "buyerCompanyName": "BlueStone Pte Ltd",
     *             "contractNumber": "CT-00000009",
     *             "contractUuid": "f905442c-df0f-41cc-8f53-77a817796369",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "COMPLETE",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 194483.2,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null
     *         },
     *         {
     *             "buyerCompanyName": "BlueStone Pte Ltd",
     *             "contractNumber": "CT-00000010",
     *             "contractUuid": "850f102f-da9b-48b5-a8ea-a880b4d1fcf8",
     *             "contractTitle": "Contract with NS",
     *             "contractStatus": "COMPLETE",
     *             "projectName": "Project Test 1",
     *             "totalAmount": 194483.2,
     *             "totalUsed": 100,
     *             "issuedBy": null,
     *             "createdByName": "Entity Admin",
     *             "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
     *             "issuedDate": null,
     *             "createdDate": "2021-07-17 02:20:33",
     *             "updatedDate": "2021-07-17 02:20:33",
     *             "approvedDate": null,
     *             "acknowledgeDate": null
     *         }
     *     ],
     *     "timestamp": 1626463338279,
     *     "statusCode": 0
     * }
     */
    @GetMapping(ControllerPath.SUPPLIER+ControllerPath.LIST)
    public ResponseEntity<ApiResponse> listSupplierContracts(@PathVariable("companyUuid") @NotEmpty String companyUuid) throws Exception {
        ApiResponse apiResponse = contractService.listSupplierContracts(companyUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {put} /:companyUuid/contract/buyer/submit/contract-uuid/:contractUuid  Submit contract
     * @apiDescription Submit contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     * @apiParam {String} contractTitle contract title
     * @apiParam {String} contractingEntity contract entity
     * @apiParam {String} contractingOwner contract owner
     * @apiParam {String} contractType contract type
     * @apiParam {boolean} isOutsourcingContract is this outsourcing contract
     * @apiParam {String} currencyCode currency code
     * @apiParam {BigDecimal} contractValue contract value
     * @apiParam {Instant} contractStartDate contract start date
     * @apiParam {Instant} contractEndDate contract end date
     * @apiParam {String} paymentTermName name of payment term
     * @apiParam {String} paymentTermUuid uuid of payment term
     * @apiParam {String} renewalOption renewal option
     * @apiParam {String} natureOfContract nature of contract
     * @apiParam {String} projectName name of project
     * @apiParam {String} projectUuid uuid of project
     * @apiParam {String} projectRfqNo Request for Quote Number for project
     * @apiParam {Object} deliveryAddress contact address information
     * @apiParam {String} deliveryAddress.addressLabel label for the address
     * @apiParam {String} deliveryAddress.addressFirstLine the first line of the address
     * @apiParam {String} deliveryAddress.addressSecondLine the second line of the address
     * @apiParam {String} deliveryAddress.city the city of the address
     * @apiParam {String} deliveryAddress.state the state of the address
     * @apiParam {String} deliveryAddress.country the country of the address
     * @apiParam {String} deliveryAddress.postalCode the postal code or zip code of the address
     * @apiParam {BigDecimal} totalUsed total used value
     * @apiParam {String} productServiceDescription uuid of project
     * @apiParam {String} approvalRouteUuid Approval sequence of approval route (cost center)
     * @apiParam {boolean} eSignRouting are the vendors connected
     * @apiParam {Object} buyerInformation buyer information
     * @apiParam {String} buyerInformation.buyerCode buyer code
     * @apiParam {String} [buyerInformation.buyerVendorConnectionUuid] buyer uuid that's from the supplier table
     * @apiParam {String} buyerInformation.buyerCompanyUuid buyer companyUuid
     * @apiParam {String} buyerInformation.companyName the company name
     * @apiParam {String} buyerInformation.taxRegNo the company's UEN
     * @apiParam {Object} buyerInformation.companyAddress contact address information
     * @apiParam {String} buyerInformation.companyAddress.addressLabel label for the address
     * @apiParam {String} buyerInformation.companyAddress.addressFirstLine the first line of the address
     * @apiParam {String} buyerInformation.companyAddress.addressSecondLine the second line of the address
     * @apiParam {String} buyerInformation.companyAddress.city the city of the address
     * @apiParam {String} buyerInformation.companyAddress.state the state of the address
     * @apiParam {String} buyerInformation.companyAddress.country the country of the address
     * @apiParam {String} buyerInformation.companyAddress.postalCode the postal code or zip code of the address
     * @apiParam {Object} supplierInformation supplier information
     * @apiParam {String} supplierInformation.supplierCode supplier code
     * @apiParam {String} supplierInformation.supplierVendorConnectionUuid supplier uuid that's from the supplier table
     * @apiParam {String} [supplierInformation.supplierCompanyUuid] supplier companyUuid
     * @apiParam {String} supplierInformation.companyName the company name
     * @apiParam {String} supplierInformation.taxRegNo the company's UEN
     * @apiParam {Object} supplierInformation.companyAddress contact address information
     * @apiParam {String} supplierInformation.companyAddress.addressLabel label for the address
     * @apiParam {String} supplierInformation.companyAddress.addressFirstLine the first line of the address
     * @apiParam {String} supplierInformation.companyAddress.addressSecondLine the second line of the address
     * @apiParam {String} supplierInformation.companyAddress.city the city of the address
     * @apiParam {String} supplierInformation.companyAddress.state the state of the address
     * @apiParam {String} supplierInformation.companyAddress.country the country of the address
     * @apiParam {String} supplierInformation.companyAddress.postalCode the postal code or zip code of the address
     * @apiParam {Object} supplierInformation.contactInformation contact information
     * @apiParam {String} supplierInformation.contactInformation.contactName contact name
     * @apiParam {String} supplierInformation.contactInformation.contactEmail contact email
     * @apiParam {String} supplierInformation.contactInformation.contactNumber contact number
     * @apiParam {Object[]} items contract items
     * @apiParam {String} items.itemCode item code
     * @apiParam {String} items.itemName the name of the item
     * @apiParam {String} items.itemDescription the file name of the item
     * @apiParam {String} items.model The model of the item
     * @apiParam {String} items.size the size of the item
     * @apiParam {String} items.brand the name of the item
     * @apiParam {String} items.trade the name of the item
     * @apiParam {String} items.uom the unit of measurement used by the item
     * @apiParam {String} items.currency the currency the item is expressed in
     * @apiParam {BigDecimal} items.qty the name of the document
     * @apiParam {BigDecimal} items.unitPrice the name of the document
     * @apiParam {String} items.taxCode the name of the document
     * @apiParam {String} items.taxCodeUuid the name of the document
     * @apiParam {BigDecimal} items.taxCodeValue the name of the document
     * @apiParam {BigDecimal} items.inSourceCurrency the name of the document
     * @apiParam {BigDecimal} items.exchangeRate the name of the document
     * @apiParam {BigDecimal} items.inDocumentCurrency the name of the document
     * @apiParam {Instant} items.deliveryDate the name of the document
     * @apiParam {Object} items.deliveryAddress contact address information
     * @apiParam {String} items.deliveryAddress.addressLabel label for the address
     * @apiParam {String} items.deliveryAddress.addressFirstLine the first line of the address
     * @apiParam {String} items.deliveryAddress.addressSecondLine the second line of the address
     * @apiParam {String} items.deliveryAddress.city the city of the address
     * @apiParam {String} items.deliveryAddress.state the state of the address
     * @apiParam {String} items.deliveryAddress.country the country of the address
     * @apiParam {String} items.deliveryAddress.postalCode the postal code or zip code of the address
     * @apiParam {String} items.glAccount the name of the document
     * @apiParam {String} items.note the name of the document
     * @apiParam {Object[]} contractDocuments contract documents
     * @apiParam {String} contractDocuments.guid unique identifier for the documents
     * @apiParam {String} contractDocuments.title the name of the document
     * @apiParam {String} contractDocuments.fileName the file name of the document
     * @apiParam {String} contractDocuments.description The description of the document
     * @apiParam {String} contractDocuments.uploadBy The user who upload the document
     * @apiParam {String} contractDocuments.uploaderUuid The user uuid who upload the document
     * @apiParam {Instant} contractDocuments.uploadOn The date the document upload on
     * @apiParam {Instant} contractDocuments.updatedOn The date the document updated on
     * @apiParam {boolean} externalDocument is external or internal
     * @apiHeader {String} Authorization JWT token
     *
     * @apiParamExample {json} Request-Example:
     * {
     *   "contractTitle":"Contract with NS",
     *   "contractingEntity":"Company 1",
     *   "contractingOwner":"Sarah Han",
     *   "contractType": "General",
     *   "isOutsourcingContract":false,
     *   "currencyCode":"SGD",
     *   "contractValue":167000.00,
     *   "contractStartDate":"2021-07-17 02:20:33",
     *   "contractEndDate":"2021-07-17 02:20:33",
     *   "paymentTermName":"30 days",
     *   "paymentTermUuid":"2222",
     *   "renewalOption":"N/A",
     *   "natureOfContract":"PROJECT",
     *   "projectName":"Project Test 1",
     *   "projectUuid":"32342jk3",
     *   "projectRfqNo":"3232",
     *   "deliveryAddress":{
     *     "addressLabel":"HQ",
     *     "addressFirstLine":"12 Leng Kee Road",
     *     "addressSecondLine":"#12-07 ICA Building",
     *     "city":"Singapore",
     *     "state":"Singapore",
     *     "country":"Singapore",
     *     "postalCode":"353323"
     *   },
     *   "totalUsed":100.00,
     *   "productServiceDescription":"Bondex Materials",
     * 	 "approvalRouteUuid": "11111",
     *   "eSignRouting":false,
     *   "buyerInformation":{
     *     "buyerCode":"123",
     *     "buyerVendorConnectionUuid":"111",
     *     "buyerCompanyUuid":"11111",
     *     "companyName":"BlueStone Pte Ltd",
     *     "taxRegNo":"HJ8768 7679",
     *     "country":"Singapore",
     *     "companyAddress":{
     *       "addressLabel":"Blk 35",
     *       "addressFirstLine":"Blk 35 Mandalay Road",
     *       "addressSecondLine":"#13-37 Mandalay Towers",
     *       "city":"Singapore",
     *       "state":"Singapore",
     *       "country":"Singapore",
     *       "postalCode":"308215"
     *     },
     *     "contactInformation":{
     *       "contactName":"Sarah Lee",
     *       "contactEmail":"SarahLee@getnada.com",
     *       "contactNumber":"8768 7679"
     *     }
     *   },
     *   "supplierInformation":{
     *     "supplierCode":"321",
     *     "supplierVendorConnectionUuid":"222",
     *     "supplierCompanyUuid":"1111",
     *     "companyName":"NetSteel Holdings Pte Ltd",
     *     "taxRegNo":"K9NB9889",
     *     "country":"Singapore",
     *     "companyAddress":{
     *       "addressLabel":"FDK Singapore",
     *       "addressFirstLine":"4 Leng Kee Road",
     *       "addressSecondLine":"#06-07 SIS Building",
     *       "city":"Singapore",
     *       "state":"Singapore",
     *       "country":"Singapore",
     *       "postalCode":"159088"
     *     },
     *     "contactInformation":{
     *       "contactName":"Alice Grace",
     *       "contactEmail":"AliceGrace@getnada.com",
     *       "contactNumber":"8709 6437"
     *     }
     *   },
     *   "items":[
     *     {
     *       "itemCode":"RB-002",
     *       "itemName":"Rebar",
     *       "itemDescription":"40mm",
     *       "model":"Reebar",
     *       "size":"40mm",
     *       "brand":"Liebherr",
     *       "trade":"Builder Workes",
     *       "uom":"Ton",
     *       "currency":"SGD",
     *       "qty":10.00,
     *       "unitPrice":1000.00,
     *       "taxCode":"GST7",
     *       "taxCodeUuid":"abc",
     *       "taxCodeValue":7,
     *       "inSourceCurrency":10000,
     *       "exchangeRate":1,
     *       "inDocumentCurrency":10000,
     *       "deliveryAddress":{
     *         "addressLabel":"HQ",
     *         "addressFirstLine":"12 Leng Kee Road",
     *         "addressSecondLine":"#12-07 ICA Building",
     *         "city":"Singapore",
     *         "state":"Singapore",
     *         "country":"Singapore",
     *         "postalCode":"353323"
     *       },
     *       "deliveryDate":"2021-07-17 02:20:33",
     *       "glAccount":"123abc",
     *       "note":"from singapore"
     *     },
     *     {
     *       "itemCode":"RB-002",
     *       "itemName":"Rebar",
     *       "itemDescription":"30mm",
     *       "model":"Reebar",
     *       "size":"30mm",
     *       "brand":"Liebherr",
     *       "trade":"Builder Workes",
     *       "uom":"Ton",
     *       "currency":"MYR",
     *       "qty":10.00,
     *       "unitPrice":900.00,
     *       "taxCode":"GST7",
     *       "taxCodeUuid":"abc",
     *       "taxCodeValue":7,
     *       "inSourceCurrency":9000.00,
     *       "exchangeRate":0.32,
     *       "inDocumentCurrency":2880.00,
     *       "deliveryAddress":{
     *         "addressLabel":"HQ",
     *         "addressFirstLine":"12 Leng Kee Road",
     *         "addressSecondLine":"#12-07 ICA Building",
     *         "city":"Singapore",
     *         "state":"Singapore",
     *         "country":"Singapore",
     *         "postalCode":"353323"
     *       },
     *       "deliveryDate":"2021-07-17 02:20:33",
     *       "glAccount":"123abc",
     *       "note":"from malaysia"
     *     },
     *     {
     *       "itemCode":"RB-003",
     *       "itemName":"Rebar",
     *       "itemDescription":"50mm",
     *       "model":"Reebar",
     *       "size":"50mm",
     *       "brand":"Liebherr",
     *       "trade":"Builder Workes",
     *       "uom":"Ton",
     *       "currency":"USD",
     *       "qty":10.00,
     *       "unitPrice":6000.00,
     *       "taxCode":"GST7",
     *       "taxCodeUuid":"abc",
     *       "taxCodeValue":7,
     *       "inSourceCurrency":60000.00,
     *       "exchangeRate":1.30,
     *       "inDocumentCurrency":78000.00,
     *       "deliveryAddress":{
     *         "addressLabel":"HQ",
     *         "addressFirstLine":"12 Leng Kee Road",
     *         "addressSecondLine":"#12-07 ICA Building",
     *         "city":"Singapore",
     *         "state":"Singapore",
     *         "country":"Singapore",
     *         "postalCode":"353323"
     *       },
     *       "deliveryDate":"2021-07-17 02:20:33",
     *       "glAccount":"123abc",
     *       "note":"from USA"
     *     }
     *   ],
     *   "contractDocuments":[
     *     {
     *       "guid":"333",
     *       "title":"Terms & Conditions",
     *       "fileName":"Terms & Conditions.pdf",
     *       "description":"Contract Terms & Conditions",
     *       "uploadBy":"John Smith",
     *       "uploaderUuid":"1212",
     *       "uploadOn":"2021-07-17 02:20:33",
     *       "updatedOn":"2021-07-17 02:20:33"
     *     },
     *     {
     *       "guid":"334",
     *       "title":"NDA",
     *       "fileName":"NDA.pdf",
     *       "description":"Non Disclosure Agreement",
     *       "uploadBy":"John Smith",
     *       "uploaderUuid":"1212",
     *       "uploadOn":"2021-07-17 02:20:33",
     *       "updatedOn":"2021-07-17 02:20:33"
     *     }
     *     ]
     * }
     *
     *
     * @apiSuccessExample Success-Response Example:
     * {
     *     "status": "OK",
     *     "message": "The contract is submitted successfully: cdebc9f5-c397-4f97-aafb-c9d54bcc99d2",
     *     "timestamp": 1626459744091,
     *     "statusCode": 0
     * }
     */

    @PutMapping(ControllerPath.BUYER+ControllerPath.SUBMIT+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> submitContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid, @RequestBody @Validated ContractSubmissionDto contractInformation) throws Exception {
        ApiResponse apiResponse = contractService.submitContract(companyUuid, contractUuid, contractInformation, ContractStatusEnum.PENDING_APPROVAL.getValue(), ContractActionEnum.SUBMIT.toString());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


    /**
     *@api {put} /:companyUuid/contract/buyer/save-draft/contract-uuid/:contractUuid  Save draft contract
     * @apiDescription save draft contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     * @apiParam {String} contractTitle contract title
     * @apiParam {String} contractingEntity contract entity
     * @apiParam {String} contractingOwner contract owner
     * @apiParam {String} contractType contract type
     * @apiParam {boolean} isOutsourcingContract is this outsourcing contract
     * @apiParam {String} currencyCode currency code
     * @apiParam {BigDecimal} contractValue contract value
     * @apiParam {Instant} contractStartDate contract start date
     * @apiParam {Instant} contractEndDate contract end date
     * @apiParam {String} paymentTermName name of payment term
     * @apiParam {String} paymentTermUuid uuid of payment term
     * @apiParam {String} renewalOption renewal option
     * @apiParam {String} natureOfContract nature of contract
     * @apiParam {String} projectName name of project
     * @apiParam {String} projectUuid uuid of project
     * @apiParam {String} projectRfqNo Request for Quote Number for project
     * @apiParam {Object} deliveryAddress contact address information
     * @apiParam {String} deliveryAddress.addressLabel label for the address
     * @apiParam {String} deliveryAddress.addressFirstLine the first line of the address
     * @apiParam {String} deliveryAddress.addressSecondLine the second line of the address
     * @apiParam {String} deliveryAddress.city the city of the address
     * @apiParam {String} deliveryAddress.state the state of the address
     * @apiParam {String} deliveryAddress.country the country of the address
     * @apiParam {String} deliveryAddress.postalCode the postal code or zip code of the address
     * @apiParam {BigDecimal} totalUsed total used value
     * @apiParam {String} productServiceDescription uuid of project
     * @apiParam {String} approvalRouteUuid Approval sequence of approval route (cost center)
     * @apiParam {boolean} eSignRouting are the vendors connected
     * @apiParam {Object} buyerInformation buyer information
     * @apiParam {String} buyerInformation.buyerCode buyer code
     * @apiParam {String} [buyerInformation.buyerVendorConnectionUuid] buyer uuid that's from the supplier table
     * @apiParam {String} buyerInformation.buyerCompanyUuid buyer companyUuid
     * @apiParam {String} buyerInformation.companyName the company name
     * @apiParam {String} buyerInformation.taxRegNo the company's UEN
     * @apiParam {Object} buyerInformation.companyAddress contact address information
     * @apiParam {String} buyerInformation.companyAddress.addressLabel label for the address
     * @apiParam {String} buyerInformation.companyAddress.addressFirstLine the first line of the address
     * @apiParam {String} buyerInformation.companyAddress.addressSecondLine the second line of the address
     * @apiParam {String} buyerInformation.companyAddress.city the city of the address
     * @apiParam {String} buyerInformation.companyAddress.state the state of the address
     * @apiParam {String} buyerInformation.companyAddress.country the country of the address
     * @apiParam {String} buyerInformation.companyAddress.postalCode the postal code or zip code of the address
     * @apiParam {Object} supplierInformation supplier information
     * @apiParam {String} supplierInformation.supplierCode supplier code
     * @apiParam {String} supplierInformation.supplierVendorConnectionUuid supplier uuid that's from the supplier table
     * @apiParam {String} [supplierInformation.supplierCompanyUuid] supplier companyUuid
     * @apiParam {String} supplierInformation.companyName the company name
     * @apiParam {String} supplierInformation.taxRegNo the company's UEN
     * @apiParam {Object} supplierInformation.companyAddress contact address information
     * @apiParam {String} supplierInformation.companyAddress.addressLabel label for the address
     * @apiParam {String} supplierInformation.companyAddress.addressFirstLine the first line of the address
     * @apiParam {String} supplierInformation.companyAddress.addressSecondLine the second line of the address
     * @apiParam {String} supplierInformation.companyAddress.city the city of the address
     * @apiParam {String} supplierInformation.companyAddress.state the state of the address
     * @apiParam {String} supplierInformation.companyAddress.country the country of the address
     * @apiParam {String} supplierInformation.companyAddress.postalCode the postal code or zip code of the address
     * @apiParam {Object} supplierInformation.contactInformation contact information
     * @apiParam {String} supplierInformation.contactInformation.contactName contact name
     * @apiParam {String} supplierInformation.contactInformation.contactEmail contact email
     * @apiParam {String} supplierInformation.contactInformation.contactNumber contact number
     * @apiParam {Object[]} items contract items
     * @apiParam {String} items.itemCode item code
     * @apiParam {String} items.itemName the name of the item
     * @apiParam {String} items.itemDescription the file name of the item
     * @apiParam {String} items.model The model of the item
     * @apiParam {String} items.size the size of the item
     * @apiParam {String} items.brand the name of the item
     * @apiParam {String} items.trade the name of the item
     * @apiParam {String} items.uom the unit of measurement used by the item
     * @apiParam {String} items.currency the currency the item is expressed in
     * @apiParam {BigDecimal} items.qty the name of the document
     * @apiParam {BigDecimal} items.unitPrice the name of the document
     * @apiParam {String} items.taxCode the name of the document
     * @apiParam {String} items.taxCodeUuid the name of the document
     * @apiParam {BigDecimal} items.taxCodeValue the name of the document
     * @apiParam {BigDecimal} items.inSourceCurrency the name of the document
     * @apiParam {BigDecimal} items.exchangeRate the name of the document
     * @apiParam {BigDecimal} items.inDocumentCurrency the name of the document
     * @apiParam {Instant} items.deliveryDate the name of the document
     * @apiParam {Object} items.deliveryAddress contact address information
     * @apiParam {String} items.deliveryAddress.addressLabel label for the address
     * @apiParam {String} items.deliveryAddress.addressFirstLine the first line of the address
     * @apiParam {String} items.deliveryAddress.addressSecondLine the second line of the address
     * @apiParam {String} items.deliveryAddress.city the city of the address
     * @apiParam {String} items.deliveryAddress.state the state of the address
     * @apiParam {String} items.deliveryAddress.country the country of the address
     * @apiParam {String} items.deliveryAddress.postalCode the postal code or zip code of the address
     * @apiParam {String} items.glAccount the name of the document
     * @apiParam {String} items.note the name of the document
     * @apiParam {Object[]} contractDocuments contract documents
     * @apiParam {String} contractDocuments.guid unique identifier for the documents
     * @apiParam {String} contractDocuments.title the name of the document
     * @apiParam {String} contractDocuments.fileName the file name of the document
     * @apiParam {String} contractDocuments.description The description of the document
     * @apiParam {String} contractDocuments.uploadBy The user who upload the document
     * @apiParam {String} contractDocuments.uploaderUuid The user uuid who upload the document
     * @apiParam {Instant} contractDocuments.uploadOn The date the document upload on
     * @apiParam {Instant} contractDocuments.updatedOn The date the document updated on
     * @apiParam {boolean} externalDocument is external or internal
     * @apiHeader {String} Authorization JWT token
     *
     * @apiParamExample {json} Request-Example:
     * {
     *   "contractTitle":"Contract with NS",
     *   "contractingEntity":"Company 1",
     *   "contractingOwner":"Sarah Han",
     *   "contractType": "General",
     *   "isOutsourcingContract":false,
     *   "currencyCode":"SGD",
     *   "contractValue":167000.00,
     *   "contractStartDate":"2021-07-17 02:20:33",
     *   "contractEndDate":"2021-07-17 02:20:33",
     *   "paymentTermName":"30 days",
     *   "paymentTermUuid":"2222",
     *   "renewalOption":"N/A",
     *   "natureOfContract":"PROJECT",
     *   "projectName":"Project Test 1",
     *   "projectUuid":"32342jk3",
     *   "projectRfqNo":"3232",
     *   "deliveryAddress":{
     *     "addressLabel":"HQ",
     *     "addressFirstLine":"12 Leng Kee Road",
     *     "addressSecondLine":"#12-07 ICA Building",
     *     "city":"Singapore",
     *     "state":"Singapore",
     *     "country":"Singapore",
     *     "postalCode":"353323"
     *   },
     *   "totalUsed":100.00,
     *   "productServiceDescription":"Bondex Materials",
     * 	 "approvalRouteUuid": "11111",
     *   "eSignRouting":false,
     *   "buyerInformation":{
     *     "buyerCode":"123",
     *     "buyerVendorConnectionUuid":"111",
     *     "buyerCompanyUuid":"11111",
     *     "companyName":"BlueStone Pte Ltd",
     *     "taxRegNo":"HJ8768 7679",
     *     "country":"Singapore",
     *     "companyAddress":{
     *       "addressLabel":"Blk 35",
     *       "addressFirstLine":"Blk 35 Mandalay Road",
     *       "addressSecondLine":"#13-37 Mandalay Towers",
     *       "city":"Singapore",
     *       "state":"Singapore",
     *       "country":"Singapore",
     *       "postalCode":"308215"
     *     },
     *     "contactInformation":{
     *       "contactName":"Sarah Lee",
     *       "contactEmail":"SarahLee@getnada.com",
     *       "contactNumber":"8768 7679"
     *     }
     *   },
     *   "supplierInformation":{
     *     "supplierCode":"321",
     *     "supplierVendorConnectionUuid":"222",
     *     "supplierCompanyUuid":"1111",
     *     "companyName":"NetSteel Holdings Pte Ltd",
     *     "taxRegNo":"K9NB9889",
     *     "country":"Singapore",
     *     "companyAddress":{
     *       "addressLabel":"FDK Singapore",
     *       "addressFirstLine":"4 Leng Kee Road",
     *       "addressSecondLine":"#06-07 SIS Building",
     *       "city":"Singapore",
     *       "state":"Singapore",
     *       "country":"Singapore",
     *       "postalCode":"159088"
     *     },
     *     "contactInformation":{
     *       "contactName":"Alice Grace",
     *       "contactEmail":"AliceGrace@getnada.com",
     *       "contactNumber":"8709 6437"
     *     }
     *   },
     *   "items":[
     *     {
     *       "itemCode":"RB-002",
     *       "itemName":"Rebar",
     *       "itemDescription":"40mm",
     *       "model":"Reebar",
     *       "size":"40mm",
     *       "brand":"Liebherr",
     *       "trade":"Builder Workes",
     *       "uom":"Ton",
     *       "currency":"SGD",
     *       "qty":10.00,
     *       "unitPrice":1000.00,
     *       "taxCode":"GST7",
     *       "taxCodeUuid":"abc",
     *       "taxCodeValue":7,
     *       "inSourceCurrency":10000,
     *       "exchangeRate":1,
     *       "inDocumentCurrency":10000,
     *       "deliveryAddress":{
     *         "addressLabel":"HQ",
     *         "addressFirstLine":"12 Leng Kee Road",
     *         "addressSecondLine":"#12-07 ICA Building",
     *         "city":"Singapore",
     *         "state":"Singapore",
     *         "country":"Singapore",
     *         "postalCode":"353323"
     *       },
     *       "deliveryDate":"2021-07-17 02:20:33",
     *       "glAccount":"123abc",
     *       "note":"from singapore"
     *     },
     *     {
     *       "itemCode":"RB-002",
     *       "itemName":"Rebar",
     *       "itemDescription":"30mm",
     *       "model":"Reebar",
     *       "size":"30mm",
     *       "brand":"Liebherr",
     *       "trade":"Builder Workes",
     *       "uom":"Ton",
     *       "currency":"MYR",
     *       "qty":10.00,
     *       "unitPrice":900.00,
     *       "taxCode":"GST7",
     *       "taxCodeUuid":"abc",
     *       "taxCodeValue":7,
     *       "inSourceCurrency":9000.00,
     *       "exchangeRate":0.32,
     *       "inDocumentCurrency":2880.00,
     *       "deliveryAddress":{
     *         "addressLabel":"HQ",
     *         "addressFirstLine":"12 Leng Kee Road",
     *         "addressSecondLine":"#12-07 ICA Building",
     *         "city":"Singapore",
     *         "state":"Singapore",
     *         "country":"Singapore",
     *         "postalCode":"353323"
     *       },
     *       "deliveryDate":"2021-07-17 02:20:33",
     *       "glAccount":"123abc",
     *       "note":"from malaysia"
     *     },
     *     {
     *       "itemCode":"RB-003",
     *       "itemName":"Rebar",
     *       "itemDescription":"50mm",
     *       "model":"Reebar",
     *       "size":"50mm",
     *       "brand":"Liebherr",
     *       "trade":"Builder Workes",
     *       "uom":"Ton",
     *       "currency":"USD",
     *       "qty":10.00,
     *       "unitPrice":6000.00,
     *       "taxCode":"GST7",
     *       "taxCodeUuid":"abc",
     *       "taxCodeValue":7,
     *       "inSourceCurrency":60000.00,
     *       "exchangeRate":1.30,
     *       "inDocumentCurrency":78000.00,
     *       "deliveryAddress":{
     *         "addressLabel":"HQ",
     *         "addressFirstLine":"12 Leng Kee Road",
     *         "addressSecondLine":"#12-07 ICA Building",
     *         "city":"Singapore",
     *         "state":"Singapore",
     *         "country":"Singapore",
     *         "postalCode":"353323"
     *       },
     *       "deliveryDate":"2021-07-17 02:20:33",
     *       "glAccount":"123abc",
     *       "note":"from USA"
     *     }
     *   ],
     *   "contractDocuments":[
     *     {
     *       "guid":"333",
     *       "title":"Terms & Conditions",
     *       "fileName":"Terms & Conditions.pdf",
     *       "description":"Contract Terms & Conditions",
     *       "uploadBy":"John Smith",
     *       "uploaderUuid":"1212",
     *       "uploadOn":"2021-07-17 02:20:33",
     *       "updatedOn":"2021-07-17 02:20:33"
     *     },
     *     {
     *       "guid":"334",
     *       "title":"NDA",
     *       "fileName":"NDA.pdf",
     *       "description":"Non Disclosure Agreement",
     *       "uploadBy":"John Smith",
     *       "uploaderUuid":"1212",
     *       "uploadOn":"2021-07-17 02:20:33",
     *       "updatedOn":"2021-07-17 02:20:33"
     *     }
     *     ]
     * }
     *
     *
     * @apiSuccessExample Success-Response Example:
     * {
     *     "status": "OK",
     *     "message": "The contract is submitted successfully: cdebc9f5-c397-4f97-aafb-c9d54bcc99d2",
     *     "timestamp": 1626459744091,
     *     "statusCode": 0
     * }
     */
    @PutMapping(ControllerPath.BUYER+ControllerPath.SAVE_DRAFT+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> saveDraftContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid, @RequestBody @Validated ContractSubmissionDto contractInformation) throws Exception {
        ApiResponse apiResponse = contractService.submitContract(companyUuid, contractUuid, contractInformation, ContractStatusEnum.DRAFT_CONTRACT.getValue(), ContractActionEnum.SAVE_DRAFT.toString());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {put} /:companyUuid/contract/buyer/send-back/contract-uuid/:contractUuid  Send back contract
     * @apiDescription Send back contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     *
     *
     * @apiSuccessExample Success-Response Example:
     * {
     *     "status": "OK",
     *     "message": "The contract has been sendback: 3e4b4c6e-b3a0-4c85-8217-75d7983d1a22",
     *     "timestamp": 1626874461407,
     *     "statusCode": 0
     * }
     */
    @PutMapping(ControllerPath.BUYER+ControllerPath.SEND_BACK+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> sendBackContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.changeRejectandSendBackContractStatus(companyUuid, contractUuid, ContractStatusEnum.SENT_BACK.getValue(), ContractActionEnum.SENDBACK.toString());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {put} /:companyUuid/contract/buyer/reject/contract-uuid/:contractUuid  Reject contract
     * @apiDescription Reject contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     *
     *
     * @apiSuccessExample Success-Response Example:
     * {
     *     "status": "OK",
     *     "message": "The contract has been rejected: 3e4b4c6e-b3a0-4c85-8217-75d7983d1a22",
     *     "timestamp": 1626874461407,
     *     "statusCode": 0
     * }
     */
    @PutMapping(value = {ControllerPath.BUYER+ControllerPath.REJECT+ControllerPath.CONTRACT_UUID,ControllerPath.BUYER+ControllerPath.CANCEL+ControllerPath.CONTRACT_UUID})
    public ResponseEntity<ApiResponse> rejectContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.changeRejectandSendBackContractStatus(companyUuid, contractUuid, ContractStatusEnum.REJECTED.getValue(), ContractActionEnum.REJECT.toString());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {put} /:companyUuid/contract/buyer/approve/contract-uuid/:contractUuid  Approve contract
     * @apiDescription Approve contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     *
     * @apiSuccessExample Success-Response Example:
     * {
     *     "status": "OK",
     *     "message": "The contract has been approved: 3e4b4c6e-b3a0-4c85-8217-75d7983d1a22",
     *     "timestamp": 1626874461407,
     *     "statusCode": 0
     * }
     */
    @PutMapping(ControllerPath.BUYER+ControllerPath.APPROVE+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> approveContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.approveContract(companyUuid, contractUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {put} /:companyUuid/contract/buyer/recall/contract-uuid/:contractUuid  Recall contract
     * @apiDescription Recall contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     *
     * @apiSuccessExample Success-Response Example:
     * {
     *     "status": "OK",
     *     "message": "The contract has been recalled: 3e4b4c6e-b3a0-4c85-8217-75d7983d1a22",
     *     "timestamp": 1626874461407,
     *     "statusCode": 0
     * }
     */
    @PutMapping(ControllerPath.BUYER+ControllerPath.RECALL+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> recallContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.recallContract(companyUuid, contractUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {put} /:companyUuid/contract/buyer/issue/contract-uuid/:contractUuid  Issue contract
     * @apiDescription Issue contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     *
     * @apiSuccessExample Success-Response Example:
     * {
     *     "status": "OK",
     *     "message": "The contract has been issued: 3e4b4c6e-b3a0-4c85-8217-75d7983d1a22",
     *     "timestamp": 1626874461407,
     *     "statusCode": 0
     * }
     */
    @PutMapping(ControllerPath.BUYER+ControllerPath.ISSUE+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> issueContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.issueContract(companyUuid, contractUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     *
     * @api {put} /:companyUuid/contract/supplier/acknowledge/contract-uuid/:contractUuid  Acknowledge contract
     * @apiDescription Acknowledge contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     *
     * {
     *     "status": "OK",
     *     "message": "The supplier has acknowledge the contract: 957b84e0-1880-4468-bd7b-3f734c6ce7ca",
     *     "timestamp": 1626459686927,
     *     "statusCode": 0
     * }
     */
    @PutMapping(ControllerPath.SUPPLIER+ControllerPath.ACKNOWLEDGE+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> acknowledgeContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.supplierChangeContractStatus(companyUuid, contractUuid, ContractStatusEnum.COMPLETE.getValue(), ContractActionEnum.ACKNOWLEDGE.toString());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     *
     * @api {put} /:companyUuid/contract/supplier/reject/contract-uuid/:contractUuid  Reject contract
     * @apiDescription Reject contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     * {
     *     "status": "OK",
     *     "message": "The contract has been rejected: cdebc9f5-c397-4f97-aafb-c9d54bcc99d2",
     *     "timestamp": 1626459817873,
     *     "statusCode": 0
     * }
     */
    @PutMapping(ControllerPath.SUPPLIER+ControllerPath.REJECT+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> supplierRejectContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.supplierChangeContractStatus(companyUuid, contractUuid, ContractStatusEnum.REJECTED.getValue(), ContractActionEnum.REJECT.toString());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * @api {put} /:companyUuid/contract/buyer/terminate/contract-uuid/:contractUuid  Terminate contract
     * @apiDescription Terminate contract
     * @apiDescription @author Andrew
     * @apiGroup Manage Contract
     * @apiVersion 0.0.1
     * @apiParam {String} companyUuid company's unique ID
     * @apiParam {String} contractUuid contract's unique ID
     *
     *
     * @apiSuccessExample Success-Response Example:
     * {
     *     "status": "OK",
     *     "message": "The contract has been terminated: 3e4b4c6e-b3a0-4c85-8217-75d7983d1a22",
     *     "timestamp": 1626874461407,
     *     "statusCode": 0
     * }
     */
    @PutMapping(ControllerPath.BUYER+ControllerPath.TERMINATE+ControllerPath.CONTRACT_UUID)
    public ResponseEntity<ApiResponse> terminateContract(@PathVariable("companyUuid") @NotEmpty String companyUuid, @PathVariable("contractUuid") @NotEmpty String contractUuid) throws Exception {
        ApiResponse apiResponse = contractService.terminateContract(companyUuid, contractUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }


}
