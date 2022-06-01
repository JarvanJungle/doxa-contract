package org.doxa.contract.controllers.contractRequest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.doxa.contract.DTO.contractRequest.CRDocumentDto;
import org.doxa.contract.DTO.contractRequest.CreateCRDto;
import org.doxa.contract.DTO.contractRequest.CreateCRDtoWithValidation;
import org.doxa.contract.DTO.contractRequest.EditCRDtoWithValidation;
import org.doxa.contract.authorization.ContractRequestAuthorization;
import org.doxa.contract.enums.ContractRequestStatus;
import org.doxa.contract.config.ControllerPath;
import org.doxa.contract.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ControllerPath.CONTRACT_REQUEST)
@Slf4j
public class ContractRequestController {

	@Autowired
	private ContractRequestAuthorization contractRequestService;
	
	
    /**
     * @api {post} /contract/:companyUuid/contract-request/submit Submit a contract request
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam  {String} contractRequestUuid Contract request uuid (If submit from saved draft)
     * @apiParam  {String} contractingEntity Contracting entity
     * @apiParam  {String} contractingOwner Contracting owner
     * @apiParam  {String} contractTitle Contract title
     * @apiParam  {String} contractType Contract type
     * @apiParam  {boolean} outSourcingContract Is the contract outsource?
     * @apiParam  {String} currencyCode Currency code
     * @apiParam  {Instant} contractStartDate Contract start date
     * @apiParam  {Instant} contractEndDate Contract end date
     * @apiParam  {String} paymentTermName Payment term name
     * @apiParam  {String} paymentTermUuid Payment term uuid
     * @apiParam  {String} renewalOption Renewal option, yes or no
     * @apiParam  {boolean} project Is tied to project?
     * @apiParam  {String} projectName Project name if project = true
     * @apiParam  {String} projectUuid Project uuid if project = true
     * @apiParam  {String} projectRfqNo Project rfq number
	 * @apiParam {Object} deliveryAddress contact address information
	 * @apiParam {String} deliveryAddress.addressLabel label for the address
	 * @apiParam {String} deliveryAddress.addressFirstLine the first line of the address
	 * @apiParam {String} deliveryAddress.addressSecondLine the second line of the address
	 * @apiParam {String} deliveryAddress.city the city of the address
	 * @apiParam {String} deliveryAddress.state the state of the address
	 * @apiParam {String} deliveryAddress.country the country of the address
	 * @apiParam {String} deliveryAddress.postalCode the postal code or zip code of the address
     * @apiParam  {String} totalUsedCurrencyCode Total used currency code
     * @apiParam  {Number} totalUsed Total used
     * @apiParam  {String} productServiceDescription Product service description
	 * @apiParam {String} approvalRouteUuid Approval sequence of approval route (cost center)
	 * @apiParam {Object} supplierInformation supplier information
	 * @apiParam {String} supplierInformation.supplierCode supplier code
	 * @apiParam {String} supplierInformation.supplierVendorConnectionUuid supplier uuid that's from the supplier table
	 * @apiParam {String} supplierInformation.supplierCompanyUuid supplier companyUuid
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
     * @apiParam  {boolean} connected Is connected to vendor?
     * @apiParam  {Array} documentList List of documents or attachments
     * @apiParam  (List) {String} guid Unique guid of the document
     * @apiParam  (List) {String} fileLabel Attachment file's label
     * @apiParam  (List) {String} fileDescription Attachment file's description
	 * @apiParam  (List) {boolean} externalDocument is external or internal
     * @apiParam  {Array} contractItemList List of contract items
     * @apiParam  (List) {String} itemCode Item code
     * @apiParam  (List) {String} itemName Item name
     * @apiParam  (List) {String} itemDescription Item description
     * @apiParam  (List) {String} itemModel Item model
     * @apiParam  (List) {String} itemSize Item size
     * @apiParam  (List) {String} itemBrand Item brand
     * @apiParam  (List) {String} trade Trade
     * @apiParam  (List) {String} uomCode UOM Code
     * @apiParam  (List) {Number} itemQuantity Item quantity
     * @apiParam  (List) {String} currencyCode Currency code
     * @apiParam  (List) {Number} itemUnitPrice Item unit price
     * @apiParam  (List) {String} taxCode Tax code
	 * @apiParam  (List) {String} taxCodeUuid Tax code Uuid
     * @apiParam  (List) {Number} taxPercentage Tax percentage
     * @apiParam  (List) {Number} exchangeRate Exchange rate
     * @apiParam  (List) {String} glAccountNumber GL account number
     * @apiParam  (List) {String} glAccountUuid GL account uuid
     * @apiParam  (List) {String} note Note
     * @apiParam  (List) {boolean} manualItem Is item manual?
     * 
     * 
     * @apiParamExample {json} Request-Example:
	 *		{
	 *			"contractRequestUuid":"f442318f-4037-434d-a004-18e8c8174d9e",
	 *		    "contractingEntity":"Contract Entity",
	 *		    "contractingOwner":"Contract Owner",
	 *		    "contractTitle":"Contract Title",
	 *		    "contractType":"General",
	 *		    "outSourcingContract":true,
	 *		    "currencyCode":"SGD",
	 *		    "contractStartDate":"2021-07-17 02:20:33",
	 *		    "contractEndDate":"2021-07-17 02:20:33",
	 *		    "paymentTermName":"30 Days",
	 *		    "paymentTermUuid":"11111",
	 *		    "renewalOption":"YES",
	 *		    "project":true,
	 *		    "projectName":"Project name",
	 *		    "projectUuid":"11111",
	 *		    "projectRfqNo":"Project RFQ",
	 *   		"deliveryAddress":{
	 *     			"addressLabel":"HQ",
	 *     			"addressFirstLine":"12 Leng Kee Road",
	 *     			"addressSecondLine":"#12-07 ICA Building",
	 *     			"city":"Singapore",
	 *     			"state":"Singapore",
	 *     			"country":"Singapore",
	 *     			"postalCode":"353323"
	 *   		},
	 *		    "totalUsedCurrencyCode":"SGD",
	 *		    "totalUsed":"100",
	 *		    "productServiceDescription":"Product service description blah blah",
	 * 			"approvalRouteUuid": "11111",
	 *   		"supplierInformation":{
	 *     			"supplierCode":"321",
	 *     			"supplierVendorConnectionUuid":"222",
	 *     			"supplierCompanyUuid":"1111",
	 *     			"companyName":"NetSteel Holdings Pte Ltd",
	 *     			"taxRegNo":"K9NB9889",
	 *     			"country":"Singapore",
	 *     			"companyAddress":{
	 *       			"addressLabel":"FDK Singapore",
	 *       			"addressFirstLine":"4 Leng Kee Road",
	 *       			"addressSecondLine":"#06-07 SIS Building",
	 *       			"city":"Singapore",
	 *       			"state":"Singapore",
	 *       			"country":"Singapore",
	 *       			"postalCode":"159088"
	 *     			},
	 *     			"contactInformation":{
	 *       		"contactName":"Alice Grace",
	 *       		"contactEmail":"AliceGrace@getnada.com",
	 *       		"contactNumber":"8709 6437"
	 *     			}
	 *  		 },
	 *		    "connected":false,
	 *		    "documentList": [
	 *		        {
	 *		            "fileLabel": "submitCRPDF.pdf", 
	 *		            "fileDescription": "submitCR_desciptionPDF.pdf", 
	 *		            "guid": "dafdg431afds6f4sd6v4fs313ewr"
	 *		        }
	 *		    ],
	 *		    "contractItemList":[
	 *		        {
	 *		            "itemCode": "RB-001", 
	 *		            "itemName": "Rebar", 
	 *		            "itemDescription": "30mm description",
	 *		            "itemModel": "Rebar model", 
	 *		            "itemSize": "30mm", 
	 *		            "itemBrand": "Liebherr",
	 *		            "trade": "Builder Works", 
	 *		            "uomCode": "Ton", 
	 *		            "itemQuantity": "10",
	 *		            "currencyCode": "SGD", 
	 *		            "itemUnitPrice": "1000.00", 
	 *		            "taxCode": "GST7",
	 *		            "taxPercentage": "7", 
	 *		            "exchangeRate": "1.00",
	 *		            "glAccountNumber": "GL Acoount Number",
	 *		            "glAccountUuid": "glaccountUuid", 
	 *		            "note": "note for item", 
	 *		            "manualItem": false
	 *		        },
	 *		        {
	 *		            "itemCode": "RB-002", 
	 *		            "itemName": "Rebar 2", 
	 *		            "itemDescription": "40mm description",
	 *		            "itemModel": "Rebar 2 model", 
	 *		            "itemSize": "40mm", 
	 *		            "itemBrand": "Liebherr",
	 *		            "trade": "Builder Works", 
	 *		            "uomCode": "Ton", 
	 *		            "itemQuantity": "10",
	 *		            "currencyCode": "MYR", 
	 *		            "itemUnitPrice": "900.00", 
	 *		            "taxCode": "GST7",
	 *		            "taxPercentage": "7", 
	 *		            "exchangeRate": "0.32",
	 *		            "glAccountNumber": "GL Acoount Number",
	 *		            "glAccountUuid": "glaccountUuid", 
	 *		            "note": "note for item", 
	 *		            "manualItem": false
	 *		        }
	 *		    ]
	 *		}
     * 
     * @apiHeader {String} Authorization JWT token
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
	 *		"message": "Submit is successful",
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "Essential fields/information are missing",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Submit a contract request @author: Melvin
     *
     */
    @PostMapping(ControllerPath.CR_SUBMIT)
    public ResponseEntity<ApiResponse> submitContractRequest(@PathVariable("companyUuid") String companyUuid, @Valid @RequestBody CreateCRDtoWithValidation createDto) throws Exception {
        ApiResponse apiResponse = contractRequestService.submitContractRequest(companyUuid, createDto);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    /**
     * @api {post} /contract/:companyUuid/contract-request/save-draft Save draft for contract request (All fields are optional)
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam  {String} contractingEntity Contracting entity
     * @apiParam  {String} contractingOwner Contracting owner
     * @apiParam  {String} contractTitle Contract title
     * @apiParam  {String} contractType Contract type
     * @apiParam  {boolean} outSourcingContract Is the contract outsource?
     * @apiParam  {String} currencyCode Currency code
     * @apiParam  {Instant} contractStartDate Contract start date
     * @apiParam  {Instant} contractEndDate Contract end date
     * @apiParam  {String} paymentTermName Payment term name
     * @apiParam  {String} paymentTermUuid Payment term uuid
     * @apiParam  {String} renewalOption Renewal option, yes or no
     * @apiParam  {boolean} project Is tied to project?
     * @apiParam  {String} projectName Project name if project = true
     * @apiParam  {String} projectUuid Project uuid if project = true
     * @apiParam  {String} projectRfqNo Project rfq number
	 * @apiParam {Object} deliveryAddress contact address information
	 * @apiParam {String} deliveryAddress.addressLabel label for the address
	 * @apiParam {String} deliveryAddress.addressFirstLine the first line of the address
	 * @apiParam {String} deliveryAddress.addressSecondLine the second line of the address
	 * @apiParam {String} deliveryAddress.city the city of the address
	 * @apiParam {String} deliveryAddress.state the state of the address
	 * @apiParam {String} deliveryAddress.country the country of the address
	 * @apiParam {String} deliveryAddress.postalCode the postal code or zip code of the address
     * @apiParam  {String} totalUsedCurrencyCode Total used currency code
     * @apiParam  {Number} totalUsed Total used
     * @apiParam  {String} productServiceDescription Product service description
	 * @apiParam {String} approvalRouteUuid Approval sequence of approval route (cost center)
	 * @apiParam {Object} supplierInformation supplier information
	 * @apiParam {String} supplierInformation.supplierCode supplier code
	 * @apiParam {String} supplierInformation.supplierVendorConnectionUuid supplier uuid that's from the supplier table
	 * @apiParam {String} supplierInformation.supplierCompanyUuid supplier companyUuid
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
     * @apiParam  {boolean} connected Is connected to vendor?
     * @apiParam  {Array} documentList List of documents or attachments
     * @apiParam  (List) {String} guid Unique guid of the document
     * @apiParam  (List) {String} fileLabel Attachment file's label
     * @apiParam  (List) {String} fileDescription Attachment file's description
	 * @apiParam  (List) {boolean} externalDocument is external or internal
     * @apiParam  {Array} contractItemList List of contract items
     * @apiParam  (List) {String} itemCode Item code
     * @apiParam  (List) {String} itemName Item name
     * @apiParam  (List) {String} itemDescription Item description
     * @apiParam  (List) {String} itemModel Item model
     * @apiParam  (List) {String} itemSize Item size
     * @apiParam  (List) {String} itemBrand Item brand
     * @apiParam  (List) {String} trade Trade
     * @apiParam  (List) {String} uomCode UOM Code
     * @apiParam  (List) {Number} itemQuantity Item quantity
     * @apiParam  (List) {String} currencyCode Currency code
     * @apiParam  (List) {Number} itemUnitPrice Item unit price
     * @apiParam  (List) {String} taxCode Tax code
	 * @apiParam  (List) {String} taxCodeUuid Tax code Uuid
     * @apiParam  (List) {Number} taxPercentage Tax percentage
     * @apiParam  (List) {Number} exchangeRate Exchange rate
     * @apiParam  (List) {String} glAccountNumber GL account number
     * @apiParam  (List) {String} glAccountUuid GL account uuid
     * @apiParam  (List) {String} note Note
     * @apiParam  (List) {boolean} manualItem Is item manual?
     * 
     * 
     * @apiParamExample {json} Request-Example:
	 *		{
	 *		    "contractingEntity":"Contract Entity",
	 *		    "contractingOwner":"Contract Owner",
	 *		    "contractTitle":"Contract Title",
	 *		    "contractType":"General",
	 *		    "outSourcingContract":true,
	 *		    "currencyCode":"SGD",
	 *		    "contractStartDate":"2021-07-17 02:20:33",
	 *		    "contractEndDate":"2021-07-17 02:20:33",
	 *		    "paymentTermName":"30 Days",
	 *		    "paymentTermUuid":"11111",
	 *		    "renewalOption":"YES",
	 *		    "project":true,
	 *		    "projectName":"Project name",
	 *		    "projectUuid":"11111",
	 *		    "projectRfqNo":"Project RFQ",
	 *   		"deliveryAddress":{
	 *     			"addressLabel":"HQ",
	 *     			"addressFirstLine":"12 Leng Kee Road",
	 *     			"addressSecondLine":"#12-07 ICA Building",
	 *     			"city":"Singapore",
	 *     			"state":"Singapore",
	 *     			"country":"Singapore",
	 *     			"postalCode":"353323"
	 *   		},
	 *		    "totalUsedCurrencyCode":"SGD",
	 *		    "totalUsed":"100",
	 *		    "productServiceDescription":"Product service description blah blah",
	 * 			"approvalRouteUuid": "11111",
	 *   		"supplierInformation":{
	 *     			"supplierCode":"321",
	 *     			"supplierVendorConnectionUuid":"222",
	 *     			"supplierCompanyUuid":"1111",
	 *     			"companyName":"NetSteel Holdings Pte Ltd",
	 *     			"taxRegNo":"K9NB9889",
	 *     			"country":"Singapore",
	 *     			"companyAddress":{
	 *       			"addressLabel":"FDK Singapore",
	 *       			"addressFirstLine":"4 Leng Kee Road",
	 *       			"addressSecondLine":"#06-07 SIS Building",
	 *       			"city":"Singapore",
	 *       			"state":"Singapore",
	 *       			"country":"Singapore",
	 *       			"postalCode":"159088"
	 *     			},
	 *     			"contactInformation":{
	 *       		"contactName":"Alice Grace",
	 *       		"contactEmail":"AliceGrace@getnada.com",
	 *       		"contactNumber":"8709 6437"
	 *     			}
	 *  		 },
	 *		    "connected":false,
	 *		    "documentList": [
	 *		        {
	 *		            "fileLabel": "submitCRPDF.pdf", 
	 *		            "fileDescription": "submitCR_desciptionPDF.pdf", 
	 *		            "guid": "dafdg431afds6f4sd6v4fs313ewr"
	 *		        }
	 *		    ],
	 *		    "contractItemList":[
	 *		        {
	 *		            "itemCode": "RB-001", 
	 *		            "itemName": "Rebar", 
	 *		            "itemDescription": "30mm description",
	 *		            "itemModel": "Rebar model", 
	 *		            "itemSize": "30mm", 
	 *		            "itemBrand": "Liebherr",
	 *		            "trade": "Builder Works", 
	 *		            "uomCode": "Ton", 
	 *		            "itemQuantity": "10",
	 *		            "currencyCode": "SGD", 
	 *		            "itemUnitPrice": "1000.00", 
	 *		            "taxCode": "GST7",
	 *		            "taxPercentage": "7", 
	 *		            "exchangeRate": "1.00",
	 *		            "glAccountNumber": "GL Acoount Number",
	 *		            "glAccountUuid": "glaccountUuid", 
	 *		            "note": "note for item", 
	 *		            "manualItem": false
	 *		        },
	 *		        {
	 *		            "itemCode": "RB-002", 
	 *		            "itemName": "Rebar 2", 
	 *		            "itemDescription": "40mm description",
	 *		            "itemModel": "Rebar 2 model", 
	 *		            "itemSize": "40mm", 
	 *		            "itemBrand": "Liebherr",
	 *		            "trade": "Builder Works", 
	 *		            "uomCode": "Ton", 
	 *		            "itemQuantity": "10",
	 *		            "currencyCode": "MYR", 
	 *		            "itemUnitPrice": "900.00", 
	 *		            "taxCode": "GST7",
	 *		            "taxPercentage": "7", 
	 *		            "exchangeRate": "0.32",
	 *		            "glAccountNumber": "GL Acoount Number",
	 *		            "glAccountUuid": "glaccountUuid", 
	 *		            "note": "note for item", 
	 *		            "manualItem": false
	 *		        }
	 *		    ]
	 *		}
     * 
     * @apiHeader {String} Authorization JWT token
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
	 *		"message": "Draft save is successful",
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "Essential fields/information are missing",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Save draft for contract request (All fields are optional) @author: Melvin
     *
     */
    @PostMapping(ControllerPath.CR_SAVE_DRAFT)
    public ResponseEntity<ApiResponse> saveDraftContractRequest(@PathVariable("companyUuid") String companyUuid, @Valid @RequestBody CreateCRDto createDto) throws Exception {
        ApiResponse apiResponse = contractRequestService.createContractRequest(companyUuid, createDto, true);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    /**
     * @api {get} /contract/:companyUuid/contract-request/list Retrieve all contract request records
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam {String} companyUuid Company's unique UUID.
     *
     * @apiHeader {String} Authorization JWT token
     *
     * @apiSuccess  {Array} data List of contract request records
     * @apiSuccess  (List) {String} supplierName Supplier name
     * @apiSuccess  (List) {String} contractRequestNumber Contract request number
     * @apiSuccess  (List) {String} contractTitle Contract title
     * @apiSuccess  (List) {String} status Contract request status
	 * @apiSuccess  (List) {String} approvalRouteName Name of approval route (cost center)
	 * @apiSuccess  (List) {String} approvalRouteSequence Approval sequence of approval route
	 * @apiSuccess  (List) {String} nextApprover Approval group for the current allocated approvers
     * @apiSuccess  (List) {String} projectName Project title
     * @apiSuccess  (List) {Number} totalAmount Total amount
     * @apiSuccess  (List) {Number} totalUsed Total used
     * @apiSuccess  (List) {String} createdByName Created by name
     * @apiSuccess  (List) {String} createdDate Created date
     * @apiSuccess  (List) {String} updatedDate Updated date
     * @apiSuccess  (List) {String} approvedDate Approved date
     * @apiSuccess  (List) {String} contractRequestUuid Contract request uuid
     * @apiSuccess  (List) {boolean} connected Is the vendor connected?
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
     *       "data": [
	 *	        {
	 *	            "supplierName": "TeongSeng",
	 *	            "contractRequestNumber": "CT-00000009",
	 *	            "contractTitle": "Contract Title",
	 *	            "status": "Request Pending Approval",
	 * 				"approvalRouteName": "PEARL BANK DRAWDOWN01",
	 * 				"approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
	 * 				"nextApprover": "Finance Group (1)",
	 *	            "projectName": "Project name",
	 *	            "totalAmount": 13781.6,
	 *	            "totalUsed": 100,
	 *	            "createdByName": "Entity Admin",
	 *	            "createdDate": "2021-07-17 02:20:33",
	 *	            "updatedDate":"2021-07-17 02:20:33",
	 *	            "contractRequestUuid": "2300f850-9d26-4345-ae8b-736d4d597b0a",
	 *	            "connected": false
	 *	        },
	 *	        {
	 *	            "supplierName": "TeongSeng",
	 *	            "contractRequestNumber": "CT-00000005",
	 *	            "contractTitle": "Contract Title",
	 *	            "status": "Contract Pending Conversion",
	 * 				"approvalRouteName": "PEARL BANK DRAWDOWN01",
	 * 				"approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
	 * 				"nextApprover": "Finance Group (1)",
	 *	            "projectName": "Project name",
	 *	            "totalAmount": 13781.6,
	 *	            "totalUsed": 100,
	 *	            "createdByName": "Entity Admin",
	 *	            "createdDate": "2021-07-17 02:20:33",
	 *	            "updatedDate": "2021-07-17 02:20:33",
	 *	            "approvedDate": "2021-07-17 02:20:33",
	 *	            "contractRequestUuid": "ff8025d4-e994-4c95-a72b-40f6a310ba41",
	 *	            "connected": false
	 *	        }
	 *	    ]
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "You do not have permission",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Retrieve all contract request records @author: Melvin
     *
     */
    @GetMapping(ControllerPath.CR_LIST)
    public ResponseEntity<ApiResponse> listContractRequest(@PathVariable("companyUuid") String companyUuid) throws Exception {
        ApiResponse apiResponse = contractRequestService.listContractRequest(companyUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    /**
     * @api {get} /contract/:companyUuid/contract-request/details/{contractRequestUuid} Retrieve the contract request details
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam {String} companyUuid Company's unique UUID.
     * @apiParam {String} contractRequestUuid Contract request's unique UUID.
     *
     * @apiHeader {String} Authorization JWT token
     *
     *
     * @apiSuccess  {String} contractRequestNumber Contract request number
     * @apiSuccess  {String} globalCRNumber Global contract request number
     * @apiSuccess  {String} contractingEntity Contracting entity
     * @apiSuccess  {String} contractingOwner Contracting owner
     * @apiSuccess  {String} contractTitle Contract title
     * @apiSuccess  {String} contractType Contract type
     * @apiSuccess  {String} currencyCode Currency code
     * @apiSuccess  {String} contractValue Contract value
     * @apiSuccess  {Instant} contractStartDate Contract start date
     * @apiSuccess  {Instant} contractEndDate Contract end date
     * @apiSuccess  {String} paymentTermName Payment term name
     * @apiSuccess  {String} paymentTermUuid Payment term uuid
     * @apiSuccess  {String} renewalOption Renewal option
     * @apiSuccess  {String} createdDate Created date
     * @apiSuccess  {String} submittedDate Submitted date
     * @apiSuccess  {String} createdByName Created by user name
     * @apiSuccess  {String} createdByUuid Created by user uuid	 *
	 * @apiSuccess {boolean} contractCreator True if user is creator of the contract request
	 * @apiSuccess {boolean} approverRole If user has approval role for the contract request
	 * @apiSuccess {boolean} firstApproved If the contract request has been approved for the first time
	 * @apiSuccess {boolean} hasApproved If the user already approved the contract request
     * @apiSuccess  {String} projectName Project name
     * @apiSuccess  {String} projectUuid Project uuid
     * @apiSuccess  {String} projectRfqNo Project rfq number
	 * @apiSuccess {Object} deliveryAddress contact address information
	 * @apiSuccess {String} deliveryAddress.addressLabel label for the address
	 * @apiSuccess {String} deliveryAddress.addressFirstLine the first line of the address
	 * @apiSuccess {String} deliveryAddress.addressSecondLine the second line of the address
	 * @apiSuccess {String} deliveryAddress.city the city of the address
	 * @apiSuccess {String} deliveryAddress.state the state of the address
	 * @apiSuccess {String} deliveryAddress.country the country of the address
	 * @apiSuccess {String} deliveryAddress.postalCode the postal code or zip code of the address
     * @apiSuccess  {String} totalUsedCurrencyCode Total used currency code
     * @apiSuccess  {Number} totalUsed Total used amount
     * @apiSuccess  {String} productServiceDescription Product service description
	 * @apiSuccess {String} approvalRouteName Name of approval route (cost center)
	 * @apiSuccess {String} approvalRouteSequence Approval sequence of approval route
	 * @apiSuccess {String} approvalRouteUuid Uuid of approval route (cost center)
     * @apiSuccess  {String} status Contract request status
     * @apiSuccess  {Number} totalAmount Total amount
     * @apiSuccess  {String} updatedDate Updated date
	 * @apiSuccess {Object} supplierInformation supplier information
	 * @apiSuccess {String} supplierInformation.supplierCode supplier code
	 * @apiSuccess {String} supplierInformation.supplierVendorConnectionUuid supplier uuid that's from the supplier table
	 * @apiSuccess {String} supplierInformation.supplierCompanyUuid supplier companyUuid
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
     * @apiSuccess  {Array} documentList List of attachments to this contract request
     * @apiSuccess  (List) {String} guid Unique guid of the document
     * @apiSuccess  (List) {String} fileLabel File label
     * @apiSuccess  (List) {String} fileDescription File description
     * @apiSuccess  (List) {String} upLoadedOn Uploaded on date
     * @apiSuccess  (List) {String} uploadedByName Uploaded by name
     * @apiSuccess  (List) {String} uploadedByUuid Uploaded by user uuid
	 * @apiSuccess  (List) {boolean} externalDocument is external or internal
     * @apiSuccess  {Array} contractItemList List of contract items
     * @apiSuccess  (List) {String} itemCode Item code
     * @apiSuccess  (List) {String} itemName Item name
     * @apiSuccess  (List) {String} itemDescription Item description
     * @apiSuccess  (List) {String} itemModel Item model
     * @apiSuccess  (List) {String} itemSize Item size
     * @apiSuccess  (List) {String} itemBrand Item brand
     * @apiSuccess  (List) {String} trade Trade
     * @apiSuccess  (List) {String} uomCode UOM code
     * @apiSuccess  (List) {Number} itemQuantity Item quantity
     * @apiSuccess  (List) {String} currencyCode Currency code
     * @apiSuccess  (List) {Number} itemUnitPrice Item unit price
     * @apiSuccess  (List) {String} taxCode Tax code
	 * @apiSuccess  (List) {String} taxCodeUuid Tax code Uuid
     * @apiSuccess  (List) {Number} taxPercentage Tax percentage
     * @apiSuccess  (List) {Number} exchangeRate Exchange rate
     * @apiSuccess  (List) {String} glAccountNumber Gl account number
     * @apiSuccess  (List) {String} glAccountUuid GL account uuid
     * @apiSuccess  (List) {String} note Note
     * @apiSuccess  (List) {boolean} manualItem Is the item manual?
     * @apiSuccess  (List) {Number} inSourceCurrencyBeforeTax In source currency before tax
     * @apiSuccess  (List) {Number} inDocumentCurrencyBeforeTax In document currency before tax
     * @apiSuccess  (List) {Number} inDocumentCurrencyTaxAmount In document currency tax amount
     * @apiSuccess  (List) {Number} inDocumentCurrencyAfterTax In document currency after tax
     * @apiSuccess  {Array} auditTrailList List of audit trails
     * @apiSuccess  (List) {String} userUuid User uuid
     * @apiSuccess  (List) {String} userName User name
     * @apiSuccess  (List) {String} role Role
     * @apiSuccess  (List) {String} action Action
     * @apiSuccess  (List) {String} createdDate Created date
     * @apiSuccess  {boolean} connected Is the vendor connected?
     * @apiSuccess  {boolean} converted Has the contract request been converted
     * @apiSuccess  {boolean} project Is this tied to project
     * @apiSuccess  {boolean} outSourcingContract Is this an outsourcing contract?
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
     *       "data": {
	 *	        "contractRequestNumber": "CT-00000010",
	 *	        "globalCRNumber": "CT00000015",
	 *	        "contractingEntity": "Contract Entity",
	 *	        "contractingOwner": "Contract Owner",
	 *	        "contractTitle": "Contract Title",
	 *	        "contractType": "General",
	 *	        "currencyCode": "SGD",
	 *	        "contractValue": 12880,
	 *	        "contractStartDate": "2021-07-17 02:20:33",
	 *	        "contractEndDate": "2021-07-17 02:20:33",
	 *	        "paymentTermName": "30 Days",
	 *	        "paymentTermUuid": "11111",
	 *	        "renewalOption": "YES",
	 *	        "createdDate": "2021-07-17 02:20:33",
	 *	        "submittedDate": "2021-07-17 02:20:33",
	 *	        "createdByName": "Entity Admin",
	 *	        "createdByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
	 *       	"prCreator": true,
	 *       	"approverRole": false,
	 *       	"firstApproved"	: false,
	 *       	"hasApproved" : false,
	 *	        "projectName": "Project name",
	 *	        "projectUuid": "11111",
	 *	        "projectRfqNo": "Project RFQ",
	 *   		"deliveryAddress":{
	 *     			"addressLabel":"HQ",
	 *     			"addressFirstLine":"12 Leng Kee Road",
	 *     			"addressSecondLine":"#12-07 ICA Building",
	 *     			"city":"Singapore",
	 *     			"state":"Singapore",
	 *     			"country":"Singapore",
	 *     			"postalCode":"353323"
	 *   		},
	 *	        "totalUsedCurrencyCode": "SGD",
	 *	        "totalUsed": 100,
	 *	        "productServiceDescription": "Product service description blah blah",
	 * 			"approvalRouteName": "PEARL BANK DRAWDOWN01",
	 * 			"approvalRouteSequence": "Finance Group (1) > Admin Group (1)",
	 * 			"approvalRouteUuid": "11111",
	 *	        "status": "Contract Pending Conversion",
	 *	        "totalAmount": 13781.6,
	 *	        "updatedDate": "2021-07-17 02:20:33",
	 *   		"supplierInformation":{
	 *     			"supplierCode":"321",
	 *     			"supplierVendorConnectionUuid":"222",
	 *     			"supplierCompanyUuid":"1111",
	 *     			"companyName":"NetSteel Holdings Pte Ltd",
	 *     			"taxRegNo":"K9NB9889",
	 *     			"country":"Singapore",
	 *     			"companyAddress":{
	 *       			"addressLabel":"FDK Singapore",
	 *       			"addressFirstLine":"4 Leng Kee Road",
	 *       			"addressSecondLine":"#06-07 SIS Building",
	 *       			"city":"Singapore",
	 *       			"state":"Singapore",
	 *       			"country":"Singapore",
	 *       			"postalCode":"159088"
	 *     			},
	 *     			"contactInformation":{
	 *       		"contactName":"Alice Grace",
	 *       		"contactEmail":"AliceGrace@getnada.com",
	 *       		"contactNumber":"8709 6437"
	 *     			}
	 *  		 },
	 *	        "documentList": [
	 *	            {
	 *	                "guid": "dafdg431afds6f4sd6v4fs313ewr",
	 *	                "fileLabel": "submitCRPDF.pdf",
	 *	                "fileDescription": "submitCR_desciptionPDF.pdf",
	 *	                "upLoadedOn": "2021-07-17 02:20:33",
	 *	                "uploadedByName": "Entity Admin",
	 *	                "uploadedByUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6"
	 *	            }
	 *	        ],
	 *	        "contractItemList": [
	 *	            {
	 *	                "itemCode": "RB-001",
	 *	                "itemName": "Rebar",
	 *	                "itemDescription": "30mm description",
	 *	                "itemModel": "Rebar model",
	 *	                "itemSize": "30mm",
	 *	                "itemBrand": "Liebherr",
	 *	                "trade": "Builder Works",
	 *	                "uomCode": "Ton",
	 *	                "itemQuantity": 10,
	 *	                "currencyCode": "SGD",
	 *	                "itemUnitPrice": 1000,
	 *	                "taxCode": "GST7",
	 *	                "taxPercentage": 7,
	 *	                "exchangeRate": 1,
	 *	                "glAccountNumber": "GL Acoount Number",
	 *	                "glAccountUuid": "glaccountUuid",
	 *	                "note": "note for item",
	 *	                "manualItem": false,
	 *	                "inSourceCurrencyBeforeTax": 10000,
	 *	                "inDocumentCurrencyBeforeTax": 10000,
	 *	                "inDocumentCurrencyTaxAmount": 700,
	 *	                "inDocumentCurrencyAfterTax": 10700
	 *	            },
	 *	            {
	 *	                "itemCode": "RB-002",
	 *	                "itemName": "Rebar 2",
	 *	                "itemDescription": "40mm description",
	 *	                "itemModel": "Rebar 2 model",
	 *	                "itemSize": "40mm",
	 *	                "itemBrand": "Liebherr",
	 *	                "trade": "Builder Works",
	 *	                "uomCode": "Ton",
	 *	                "itemQuantity": 10,
	 *	                "currencyCode": "MYR",
	 *	                "itemUnitPrice": 900,
	 *	                "taxCode": "GST7",
	 *	                "taxPercentage": 7,
	 *	                "exchangeRate": 0.32,
	 *	                "glAccountNumber": "GL Acoount Number",
	 *	                "glAccountUuid": "glaccountUuid",
	 *	                "note": "note for item",
	 *	                "manualItem": false,
	 *	                "inSourceCurrencyBeforeTax": 9000,
	 *	                "inDocumentCurrencyBeforeTax": 2880,
	 *	                "inDocumentCurrencyTaxAmount": 201.6,
	 *	                "inDocumentCurrencyAfterTax": 3081.6
	 *	            }
	 *	        ],
	 *	        "auditTrailList": [
	 *	            {
	 *	                "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
	 *	                "userName": "Entity Admin",
	 *	                "role": "manager",
	 *	                "action": "Submit",
	 *	                "createdDate": "2021-07-17 02:20:33"
	 *	            },
	 *	            {
	 *	                "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
	 *	                "userName": "Entity Admin",
	 *	                "role": "manager",
	 *	                "action": "Approved",
	 *	                "createdDate": "2021-07-17 02:20:33"
	 *	            },
	 *	            {
	 *	                "userUuid": "e5ff2cf2-22d4-4544-8a92-c61128b7e5d6",
	 *	                "userName": "Entity Admin",
	 *	                "role": "manager",
	 *	                "action": "Approved",
	 *	                "createdDate": "2021-07-17 02:20:33"
	 *	            }
	 *	        ],
	 *	        "connected": false,
	 *	        "converted": false,
	 *	        "project": true,
	 *	        "outSourcingContract": true
	 *	    },
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "You do not have permission",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Retrieve the contract request details @author: Melvin
     *
     */
    @GetMapping(ControllerPath.CR_DETAILS)
    public ResponseEntity<ApiResponse> retrieveContractRequestDetails(@PathVariable("companyUuid") String companyUuid, @PathVariable("contractRequestUuid") String contractRequestUuid) throws Exception {
        ApiResponse apiResponse = contractRequestService.retrieveContractRequestDetails(companyUuid, contractRequestUuid);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    /**
     * @api {put} /contract/:companyUuid/contract-request/recall/{contractRequestUuid} Recall contract request by the creator
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam {String} companyUuid Company's unique UUID.
     * @apiParam {String} contractRequestUuid Contract request's unique UUID.
     *
     * @apiHeader {String} Authorization JWT token
     *
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
     *      "message": "Recall is successful",
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "Recall action is invalid at current stage",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Recall contract request by the creator @author: Melvin
     *
     */
    @PutMapping(ControllerPath.CR_RECALL)
    public ResponseEntity<ApiResponse> recallContractRequest(@PathVariable("companyUuid") String companyUuid, @PathVariable("contractRequestUuid") String contractRequestUuid) throws Exception {
        ApiResponse apiResponse = contractRequestService.recallCancelContractRequest(companyUuid, contractRequestUuid, true);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    /**
     * @api {put} /contract/:companyUuid/contract-request/cancel/{contractRequestUuid} Cancel contract request by the creator
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam {String} companyUuid Company's unique UUID.
     * @apiParam {String} contractRequestUuid Contract request's unique UUID.
     *
     * @apiHeader {String} Authorization JWT token
     *
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
     *      "message": "Cancel is successful",
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "Cancel action is invalid at current stage",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Cancel contract request by the creator @author: Melvin
     *
     */
    @PutMapping(ControllerPath.CR_CANCEL)
    public ResponseEntity<ApiResponse> cancelContractRequest(@PathVariable("companyUuid") String companyUuid, @PathVariable("contractRequestUuid") String contractRequestUuid) throws Exception {
        ApiResponse apiResponse = contractRequestService.recallCancelContractRequest(companyUuid, contractRequestUuid, false);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    /**
     * @api {put} /contract/:companyUuid/contract-request/edit-submit Edit and submit a contract request
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam  {String} contractRequestUuid Contract request uuid
     * @apiParam  {String} contractingEntity Contracting entity
     * @apiParam  {String} contractingOwner Contracting owner
     * @apiParam  {String} contractTitle Contract title
     * @apiParam  {String} contractType Contract type
     * @apiParam  {boolean} outSourcingContract Is the contract outsource?
     * @apiParam  {String} currencyCode Currency code
     * @apiParam  {Instant} contractStartDate Contract start date
     * @apiParam  {Instant} contractEndDate Contract end date
     * @apiParam  {String} paymentTermName Payment term name
     * @apiParam  {String} paymentTermUuid Payment term uuid
     * @apiParam  {String} renewalOption Renewal option, yes or no
     * @apiParam  {boolean} project Is tied to project?
     * @apiParam  {String} projectName Project name if project = true
     * @apiParam  {String} projectUuid Project uuid if project = true
     * @apiParam  {String} projectRfqNo Project rfq number
	 * @apiParam {Object} deliveryAddress contact address information
	 * @apiParam {String} deliveryAddress.addressLabel label for the address
	 * @apiParam {String} deliveryAddress.addressFirstLine the first line of the address
	 * @apiParam {String} deliveryAddress.addressSecondLine the second line of the address
	 * @apiParam {String} deliveryAddress.city the city of the address
	 * @apiParam {String} deliveryAddress.state the state of the address
	 * @apiParam {String} deliveryAddress.country the country of the address
	 * @apiParam {String} deliveryAddress.postalCode the postal code or zip code of the address
     * @apiParam  {String} totalUsedCurrencyCode Total used currency code
     * @apiParam  {Number} totalUsed Total used
     * @apiParam  {String} productServiceDescription Product service description
	 * @apiParam {String} approvalRouteUuid Approval sequence of approval route (cost center)
     * @apiParam  {boolean} connected Is connected to vendor?
     * @apiParam  {Array} documentList List of documents or attachments
     * @apiParam  (List) {String} guid Unique guid of the document
     * @apiParam  (List) {String} fileLabel Attachment file's label
     * @apiParam  (List) {String} fileDescription Attachment file's description
	 * @apiParam  (List) {boolean} externalDocument is external or internal
     * @apiParam  {Array} contractItemList List of contract items
     * @apiParam  (List) {String} itemCode Item code
     * @apiParam  (List) {String} itemName Item name
     * @apiParam  (List) {String} itemDescription Item description
     * @apiParam  (List) {String} itemModel Item model
     * @apiParam  (List) {String} itemSize Item size
     * @apiParam  (List) {String} itemBrand Item brand
     * @apiParam  (List) {String} trade Trade
     * @apiParam  (List) {String} uomCode UOM Code
     * @apiParam  (List) {Number} itemQuantity Item quantity
     * @apiParam  (List) {String} currencyCode Currency code
     * @apiParam  (List) {Number} itemUnitPrice Item unit price
     * @apiParam  (List) {String} taxCode Tax code
	 * @apiParam  (List) {String} taxCodeUuid Tax code Uuid
     * @apiParam  (List) {Number} taxPercentage Tax percentage
     * @apiParam  (List) {Number} exchangeRate Exchange rate
     * @apiParam  (List) {String} glAccountNumber GL account number
     * @apiParam  (List) {String} glAccountUuid GL account uuid
     * @apiParam  (List) {String} note Note
     * @apiParam  (List) {boolean} manualItem Is item manual?
     * 
     * 
     * @apiParamExample {json} Request-Example:
	 *		{
	 *			"contractRequestUuid":"f442318f-4037-434d-a004-18e8c8174d9e",
	 *		    "contractingEntity":"Contract Entity",
	 *		    "contractingOwner":"Contract Owner",
	 *		    "contractTitle":"Contract Title",
	 *		    "contractType":"General",
	 *		    "outSourcingContract":true,
	 *		    "currencyCode":"SGD",
	 *		    "contractStartDate":"2021-07-17 02:20:33",
	 *		    "contractEndDate":"2021-07-17 02:20:33",
	 *		    "paymentTermName":"30 Days",
	 *		    "paymentTermUuid":"11111",
	 *		    "renewalOption":"YES",
	 *		    "project":true,
	 *		    "projectName":"Project name",
	 *		    "projectUuid":"11111",
	 *		    "projectRfqNo":"Project RFQ",
	 *   		"deliveryAddress":{
	 *     			"addressLabel":"HQ",
	 *     			"addressFirstLine":"12 Leng Kee Road",
	 *     			"addressSecondLine":"#12-07 ICA Building",
	 *     			"city":"Singapore",
	 *     			"state":"Singapore",
	 *     			"country":"Singapore",
	 *     			"postalCode":"353323"
	 *   		},
	 *		    "totalUsedCurrencyCode":"SGD",
	 *		    "totalUsed":"100",
	 *		    "productServiceDescription":"Product service description blah blah",
	 * 			"approvalRouteUuid": "11111",
	 *		    "connected":false,
	 *		    "documentList": [
	 *		        {
	 *		            "fileLabel": "submitCRPDF.pdf", 
	 *		            "fileDescription": "submitCR_desciptionPDF.pdf", 
	 *		            "guid": "dafdg431afds6f4sd6v4fs313ewr"
	 *		        }
	 *		    ],
	 *		    "contractItemList":[
	 *		        {
	 *		            "itemCode": "RB-001", 
	 *		            "itemName": "Rebar", 
	 *		            "itemDescription": "30mm description",
	 *		            "itemModel": "Rebar model", 
	 *		            "itemSize": "30mm", 
	 *		            "itemBrand": "Liebherr",
	 *		            "trade": "Builder Works", 
	 *		            "uomCode": "Ton", 
	 *		            "itemQuantity": "10",
	 *		            "currencyCode": "SGD", 
	 *		            "itemUnitPrice": "1000.00", 
	 *		            "taxCode": "GST7",
	 *		            "taxPercentage": "7", 
	 *		            "exchangeRate": "1.00",
	 *		            "glAccountNumber": "GL Acoount Number",
	 *		            "glAccountUuid": "glaccountUuid", 
	 *		            "note": "note for item", 
	 *		            "manualItem": false
	 *		        },
	 *		        {
	 *		            "itemCode": "RB-002", 
	 *		            "itemName": "Rebar 2", 
	 *		            "itemDescription": "40mm description",
	 *		            "itemModel": "Rebar 2 model", 
	 *		            "itemSize": "40mm", 
	 *		            "itemBrand": "Liebherr",
	 *		            "trade": "Builder Works", 
	 *		            "uomCode": "Ton", 
	 *		            "itemQuantity": "10",
	 *		            "currencyCode": "MYR", 
	 *		            "itemUnitPrice": "900.00", 
	 *		            "taxCode": "GST7",
	 *		            "taxPercentage": "7", 
	 *		            "exchangeRate": "0.32",
	 *		            "glAccountNumber": "GL Acoount Number",
	 *		            "glAccountUuid": "glaccountUuid", 
	 *		            "note": "note for item", 
	 *		            "manualItem": false
	 *		        }
	 *		    ]
	 *		}
     * 
     * @apiHeader {String} Authorization JWT token
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
	 *		"message": "Submit is successful",
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "Essential fields/information are missing",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Edit and submit a contract request. Only supplier non editable according to Kim Vu @author: Melvin
     *
     */
    @PutMapping(ControllerPath.CR_EDIT_SUBMIT)
    public ResponseEntity<ApiResponse> editSubmitContractRequest(@PathVariable("companyUuid") String companyUuid, @Valid @RequestBody EditCRDtoWithValidation editDto) throws Exception{
    	ApiResponse apiResponse = contractRequestService.editSubmitContractRequest(companyUuid, editDto);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    
    /**
     * @api {put} /contract/:companyUuid/contract-request/approve/{contractRequestUuid} Approve contract request
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam  {String} companyUuid Company's unique UUID.
     * @apiParam  {String} contractRequestUuid Contract request's unique UUID.
     * @apiParam  {Array} documentList List of newly added documents or attachments
     * @apiParam  (List) {String} guid Unique guid of the document
     * @apiParam  (List) {String} fileLabel Attachment file's label
     * @apiParam  (List) {String} fileDescription Attachment file's description
     * 
     * 
     * @apiParamExample {json} Request-Example:
	 *		[
	 *		    {
	 *		        "fileLabel": "approveFirstPDF.pdf", 
	 *		        "fileDescription": "approveFirstDesciptionPDF.pdf", 
	 *		        "guid": "123456"
	 *		    },
	 *		    {
	 *		        "fileLabel": "approveSecondPDF.pdf", 
	 *		        "fileDescription": "approveSecondDesciptionPDF.pdf", 
	 *		        "guid": "654321"
	 *		    }
	 *		]
     *
     *
     *
     * @apiHeader {String} Authorization JWT token
     *
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
     *      "message": "Approve is successful",
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "Action is invalid for this record with the current status",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Approve contract request @author: Melvin
     *
     */
    @PutMapping(ControllerPath.CR_APPROVE)
    public ResponseEntity<ApiResponse> approveContractRequest(@PathVariable("companyUuid") String companyUuid, @PathVariable("contractRequestUuid") String contractRequestUuid, @Valid @RequestBody(required=false) List<CRDocumentDto> newlyAddedDocuments) throws Exception{
    	ApiResponse apiResponse = contractRequestService.approverAction(companyUuid, contractRequestUuid, newlyAddedDocuments, ContractRequestStatus.APPROVE.getValue());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    //sent back
    /**
     * @api {put} /contract/:companyUuid/contract-request/sentback/{contractRequestUuid} Send back contract request
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam  {String} companyUuid Company's unique UUID.
     * @apiParam  {String} contractRequestUuid Contract request's unique UUID.
     * @apiParam  {Array} documentList List of newly added documents or attachments
     * @apiParam  (List) {String} guid Unique guid of the document
     * @apiParam  (List) {String} fileLabel Attachment file's label
     * @apiParam  (List) {String} fileDescription Attachment file's description
     * 
     * 
     * @apiParamExample {json} Request-Example:
	 *		[
	 *		    {
	 *		        "fileLabel": "approveFirstPDF.pdf", 
	 *		        "fileDescription": "approveFirstDesciptionPDF.pdf", 
	 *		        "guid": "123456"
	 *		    },
	 *		    {
	 *		        "fileLabel": "approveSecondPDF.pdf", 
	 *		        "fileDescription": "approveSecondDesciptionPDF.pdf", 
	 *		        "guid": "654321"
	 *		    }
	 *		]
     *
     *
     *
     * @apiHeader {String} Authorization JWT token
     *
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
     *      "message": "Send back is successful",
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "Action is invalid for this record with the current status",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Send back contract request @author: Melvin
     *
     */
    @PutMapping(ControllerPath.CR_SENTBACK)
    public ResponseEntity<ApiResponse> sentbackContractRequest(@PathVariable("companyUuid") String companyUuid, @PathVariable("contractRequestUuid") String contractRequestUuid, @Valid @RequestBody(required=false) List<CRDocumentDto> newlyAddedDocuments) throws Exception{
    	ApiResponse apiResponse = contractRequestService.approverAction(companyUuid, contractRequestUuid, newlyAddedDocuments, ContractRequestStatus.SENT_BACK.getValue());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
    
    //reject
    /**
     * @api {put} /contract/:companyUuid/contract-request/reject/{contractRequestUuid} Reject contract request
     * @apiGroup Contract Management
     * @apiVersion 0.0.1
     *
     * @apiParam  {String} companyUuid Company's unique UUID.
     * @apiParam  {String} contractRequestUuid Contract request's unique UUID.

     * @apiHeader {String} Authorization JWT token
     *
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *	 {
     *	    "status": "OK",
     *      "message": "Reject is successful",
     *	    "timestamp": 1619083490713,
     *	    "statusCode": 0
     *	 }
     *
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 200 OK
     *	{
     *   	"status": "BAD_REQUEST",
     *   	"message": "Action is invalid for this record with the current status",
     *   	"timestamp": 1619083958882,
     *   	"statusCode": 0
     *	}
     *
     * @apiDescription Reject contract request @author: Melvin
     *
     */
    @PutMapping(ControllerPath.CR_REJECT)
    public ResponseEntity<ApiResponse> rejectContractRequest(@PathVariable("companyUuid") String companyUuid, @PathVariable("contractRequestUuid") String contractRequestUuid) throws Exception{
    	//pass in empty list for document since reject cannot upload any new attachments
    	ApiResponse apiResponse = contractRequestService.approverAction(companyUuid, contractRequestUuid, new ArrayList<CRDocumentDto>(), ContractRequestStatus.REJECTED.getValue());
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

}
