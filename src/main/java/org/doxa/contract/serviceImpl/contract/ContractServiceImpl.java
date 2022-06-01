package org.doxa.contract.serviceImpl.contract;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.doxa.auth.DoxaAuthenticationManager;
import org.doxa.contract.DTO.contract.BuyerListingViewDto;
import org.doxa.contract.DTO.contract.ContractDto;
import org.doxa.contract.DTO.contract.ContractSubmissionDto;
import org.doxa.contract.DTO.contract.SupplierListingViewDto;
import org.doxa.contract.config.Message;
import org.doxa.contract.enums.ContractActionEnum;
import org.doxa.contract.enums.ContractRequestStatus;
import org.doxa.contract.enums.ContractStatusEnum;
import org.doxa.contract.exceptions.*;
import org.doxa.contract.mapper.*;
import org.doxa.contract.microservices.DTO.ApprovalGroupDetail;
import org.doxa.contract.microservices.DTO.ApprovalMatrixDetailsAPIDto;
import org.doxa.contract.microservices.DTO.SupplierInfo;
import org.doxa.contract.microservices.DTO.entityService.AddressDetails;
import org.doxa.contract.microservices.DTO.entityService.EntitiesService;
import org.doxa.contract.microservices.DTO.entityService.IEntitiesService;
import org.doxa.contract.microservices.DTO.oauth.CompanyDetails;
import org.doxa.contract.microservices.DTO.oauth.IOauthService;
import org.doxa.contract.microservices.DTO.oauth.UserDetail;
import org.doxa.contract.microservices.DTO.purchase.ContractInfoApiDto;
import org.doxa.contract.microservices.DTO.purchase.QuoteItemDto;
import org.doxa.contract.microservices.DTO.purchase.RfqDocumentMetaDataDto;
import org.doxa.contract.microservices.DTO.purchase.RfqDto;
import org.doxa.contract.models.common.Address;
import org.doxa.contract.models.common.BuyerInformation;
import org.doxa.contract.models.common.ContactInformation;
import org.doxa.contract.models.common.SupplierInformation;
import org.doxa.contract.models.contract.Contract;
import org.doxa.contract.models.contract.ContractAuditTrail;
import org.doxa.contract.models.contract.ContractDocument;
import org.doxa.contract.models.contract.ContractItem;
import org.doxa.contract.models.contractRequest.ContractRequest;
import org.doxa.contract.models.contractRequest.ContractRequestDocument;
import org.doxa.contract.models.contractRequest.ContractRequestItem;
import org.doxa.contract.repositories.common.AddressRepository;
import org.doxa.contract.repositories.common.BuyerInformationRepository;
import org.doxa.contract.repositories.common.ContactInformationRepository;
import org.doxa.contract.repositories.common.SupplierInformationRepository;
import org.doxa.contract.repositories.contract.ContractAuditTrailRepository;
import org.doxa.contract.repositories.contract.ContractDocumentRepository;
import org.doxa.contract.repositories.contract.ContractItemRepository;
import org.doxa.contract.repositories.contract.ContractRepository;
import org.doxa.contract.repositories.contractRequest.ContractRequestDocumentRepository;
import org.doxa.contract.repositories.contractRequest.ContractRequestItemRepository;
import org.doxa.contract.repositories.contractRequest.ContractRequestRepository;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.serviceImpl.contractRequest.ContractRequestServiceImpl;
import org.doxa.contract.serviceImpl.helper.VendorHelper;
import org.doxa.contract.services.contract.IContractService;
import org.doxa.contract.utils.ApprovalSequence;
import org.doxa.contract.utils.ObjectService;
import org.doxa.contract.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContractServiceImpl implements IContractService {
    private static final Logger LOG = LoggerFactory.getLogger(ContractRequestServiceImpl.class);

    @Autowired
    private DoxaAuthenticationManager authenticationManager;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private ObjectService objectService;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractItemMapper contractItemMapper;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContractRequestItemRepository contractRequestItemRepository;

    @Autowired
    private ContractRequestDocumentRepository contractRequestDocumentRepository;

    @Autowired
    private BuyerInformationRepository buyerInformationRepository;

    @Autowired
    private ContactInformationRepository contactInformationRepository;

    @Autowired
    private SupplierInformationRepository supplierInformationRepository;

    @Autowired
    private ContractRequestRepository contractRequestRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractItemRepository contractItemRepository;

    @Autowired
    private ContractDocumentRepository contractDocumentRepository;

    @Autowired
    private ApprovalSequence approvalSequence;

    @Autowired
    private ContractAuditTrailMapper contractAuditTrailMapper;

    @Autowired
    private ContractAuditTrailRepository contractAuditTrailRepository;

    @Autowired
    private IEntitiesService iEntitiesService;

    @Autowired
    private EntitiesService entitiesService;

    @Autowired
    private IOauthService iOauthService;

    @Autowired
    private ContractInformationMapper contractInformationMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private VendorHelper vendorHelper;

    @Override
    public ApiResponse getBuyerContractDetail(String companyUuid, String contractUuid) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional<Contract> contract = contractRepository.findContractByBuyerCompanyUuidAndContractUuid(companyUuid, contractUuid);
            if (contract.isEmpty()) {
                throw new ObjectDoesNotExistException(Message.CONTRACT_DOES_NOT_EXIST.getValue());
            }
            String buyerUuid = (String) authenticationManager.getUserByKey("sub");
            ContractDto contractDto = contractMapper.contractDto(contract.get());
            if (buyerUuid.equals(contract.get().getCreatedByUuid())) {
                contractDto.setContractCreator(true);
                contractDto.setApproverRole(false);
                contractDto.setHasApproved(false);
            } else {
                contractDto.setContractCreator(false);
                if (contract.get().getNextApprovalGroup() != null) {
                    ApprovalGroupDetail currentApprovalGroup = iEntitiesService.getApprovalGroupDetail(companyUuid, contract.get().getNextApprovalGroup());
                    contractDto.setApproverRole(currentApprovalGroup != null && currentApprovalGroup.isUserInGroup(buyerUuid));
                } else {
                    contractDto.setApproverRole(false);
                }
            }
            List<ContractAuditTrail> contractAuditTrailList = contractAuditTrailRepository.auditTrailByContractId(contract.get().getId());
            HashMap<String, ContractAuditTrail> auditTrail = new HashMap<>();
            int countApprovers = countNumberOfApproval(contractAuditTrailList, auditTrail);
            contractDto.setFirstApproved(countApprovers > 0);
//			if user is part of the "past approvers" list
            contractDto.setHasApproved(auditTrail.containsKey(buyerUuid));

            apiResponse.setData(contractDto);
            apiResponse.setStatus(HttpStatus.OK);
        } catch (ObjectDoesNotExistException e) {
            apiResponse.setStatus(HttpStatus.NOT_FOUND);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        }
        return apiResponse;

    }

    @Override
    public ApiResponse getSupplierContractDetail(String companyUuid, String contractUuid) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional<Contract> contract = contractRepository.findContractBySupplierCompanyUuidAndContractUuid(companyUuid, contractUuid);
            if (contract.isEmpty()) {
                throw new ObjectDoesNotExistException(Message.CONTRACT_DOES_NOT_EXIST.getValue());
            }
            String supplierUuid = (String) authenticationManager.getUserByKey("sub");
            ContractDto contractDto = contractMapper.contractDto(contract.get());
            if (supplierUuid.equals(contract.get().getCreatedByUuid())) {
                contractDto.setContractCreator(true);
                contractDto.setApproverRole(false);
                contractDto.setHasApproved(false);
            } else {
                contractDto.setContractCreator(false);
                if (contract.get().getNextApprovalGroup() != null) {
                    ApprovalGroupDetail currentApprovalGroup = iEntitiesService.getApprovalGroupDetail(companyUuid, contract.get().getNextApprovalGroup());
                    contractDto.setApproverRole(currentApprovalGroup != null && currentApprovalGroup.isUserInGroup(supplierUuid));
                } else {
                    contractDto.setApproverRole(false);
                }
            }
            List<ContractAuditTrail> contractAuditTrailList = contractAuditTrailRepository.auditTrailByContractId(contract.get().getId());
            HashMap<String, ContractAuditTrail> auditTrail = new HashMap<>();
            int countApprovers = countNumberOfApproval(contractAuditTrailList, auditTrail);
            contractDto.setFirstApproved(countApprovers > 0);
//			if user is part of the "past approvers" list
            contractDto.setHasApproved(auditTrail.containsKey(supplierUuid));

            apiResponse.setData(contractDto);
            apiResponse.setStatus(HttpStatus.OK);
        } catch (ObjectDoesNotExistException e) {
            apiResponse.setStatus(HttpStatus.NOT_FOUND);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        }
        return apiResponse;

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ApiResponse convertToContract(String companyUuid, String contractRequestUuid) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        String userName = (String) authenticationManager.getUserByKey("name");
        String userUuid = (String) authenticationManager.getUserByKey("sub");
        String designation = (String) authenticationManager.getUserByKey("designation");

        ContractRequest contractRequestInformation = contractRequestRepository.findByCompanyUuidAndContractRequestUuid(companyUuid, contractRequestUuid).get();
        if (contractRequestInformation == null) {
            throw new ObjectDoesNotExistException(Message.CONTRACT_REQUEST_NOT_FOUND.getValue());
        }

        //check if the contract request exist
        Optional<Contract> contractExist = contractRepository.findByCRUuid(contractRequestInformation.getContractRequestUuid(), companyUuid);
        if (contractExist.isPresent()) {
            throw new ObjectExistException(Message.CONTRACT_EXIST_WITH_THIS_CR.getValue());
        }


        Contract contract = contractMapper.convertToPendingContract(contractRequestInformation, ContractStatusEnum.PENDING_SUBMISSION.getValue(), utilsService.generateUUID(), Instant.now(), userName, userUuid);

        contract.setNatureOfContract(String.valueOf(contractRequestInformation.isProject()));

        //add CR Items
        var contractRequestItemList = contractRequestItemRepository.findByContractRequestUuid(contractRequestInformation.getContractRequestUuid());
        List<ContractItem> contractItems = new ArrayList<ContractItem>();
        if (contractRequestItemList != null && !contractRequestItemList.isEmpty()) {
            for (ContractRequestItem cRItem : contractRequestItemList) {
                var contractItem = contractItemMapper.contractItemFromContractRequestItem(cRItem);
                if (contractItem != null) {
                    contractItems.add(contractItem);
                }
            }
        }
        contract.setItems(contractItems);

        //add CR documents
        List<ContractRequestDocument> contractRequestDocumentList = contractRequestDocumentRepository.findByContractRequestUuid(contractRequestUuid);
        List<ContractDocument> contractDocuments = new ArrayList<ContractDocument>();
        for (ContractRequestDocument contractRequestDocument : contractRequestDocumentList) {
            var contractDocument = new ContractDocument();
            contractDocument.setExternalDocument(contractRequestDocument.isExternalDocument());
            contractDocument.setDescription(contractRequestDocument.getFileDescription());
            contractDocument.setFileName((contractRequestDocument.getFileLabel()));
            contractDocument.setUploadBy(contractRequestDocument.getUploadedByName());
            contractDocument.setUploaderUuid(contractRequestDocument.getUploadedByUuid());
            contractDocument.setUploadOn(contractRequestDocument.getUploadedOn());
            contractDocument.setUpdatedOn(Instant.now());
            if (contractRequestDocument.getContractRequest() != null && contractRequestDocument.getContractRequest().getContractTitle() != null) {
                contractDocument.setTitle(contractRequestDocument.getContractRequest().getContractTitle());
            }
            contractDocument.setGuid(contractRequestDocument.getGuid());
            contractDocument.setAttachment(true);
            contractDocuments.add(contractDocument);
        }
        contract.setContractDocuments(contractDocuments);

        //if the delivery address object exist then reuse
        if (contract.getDeliveryAddress() != null) {
            contract.setDeliveryAddress(objectService.saveAddress(contract.getDeliveryAddress()));
        }
        //check if the supplier information exist if true than reuse
        if (contract.getSupplierInformation() != null) {
            contract.setSupplierInformation(objectService.saveSupplierInformation(contract.getSupplierInformation()));
        } else {
            throw new BadRequestException(Message.SUPPLIER_NOT_FOUND.getValue());
        }

        //check if the buyer information exist if true than reus
        if (contract.getBuyerInformation() != null) {
            contract.setBuyerInformation(objectService.saveBuyerInformation(contract.getBuyerInformation()));
        } else {
            //TODO for unconnected/not onboarded supplier, there will be an event published when new vendor is created by using rabbmitMQ or Kafka
            BuyerInformation buyerInformation = null;
            if (StringUtils.isNotEmpty(contract.getSupplierInformation().getSupplierCompanyUuid())) {
                SupplierInfo buyer = iEntitiesService.getSupplierInfo(contract.getSupplierInformation().getSupplierCompanyUuid(), companyUuid);
                if (buyer != null) {
                    ContactInformation buyerContactPerson = vendorHelper.extractContactFromExternalVendor(buyer);
                    Address buyerDefaultAddress = vendorHelper.extractAddressFromExternalVendor(buyer);
                    buyerInformation = vendorHelper.extractBuyerInformation(buyer);
                    buyerInformation.setBuyerCompanyUuid(companyUuid);
                    buyerContactPerson = objectService.saveContactInformation(buyerContactPerson);
                    buyerDefaultAddress = objectService.saveAddress(buyerDefaultAddress);
                    buyerInformation.setContactInformation(buyerContactPerson);
                    buyerInformation.setCompanyAddress(buyerDefaultAddress);
                    buyerInformation = objectService.saveBuyerInformation(buyerInformation);
                    contract.setBuyerInformation(buyerInformation);
                }
            } else {
                buyerInformation = getBuyerInformation(companyUuid);
                ContactInformation buyerContactPerson = getContactPerson(userUuid);
                buyerInformation.setContactInformation(buyerContactPerson);
                contract.setBuyerInformation(objectService.saveBuyerInformation(buyerInformation));
            }
        }

        //set approval position

        //split the approval sequence to get the first approver group
        ApprovalMatrixDetailsAPIDto approvalMatrixResponse = iEntitiesService.getApprovalMatrixDetails(contractRequestInformation.getApprovalRouteUuid(),
                contractRequestInformation.getTotalAmount(), contractRequestInformation.getCurrencyCode(), companyUuid);
        String approvalRouteSequence = approvalMatrixResponse.getApprovalGroupSequence();

        String approvalRouteName = approvalMatrixResponse.getApprovalMatrixName();

        contract.setApprovalRouteName(approvalRouteName);
        contract.setApprovalRouteSequence(approvalRouteSequence);
        // Set first approval
        ApprovalGroupDetail firstApprovalGroup = approvalMatrixResponse.getFirstApproval();
        contract.setNextApprover(firstApprovalGroup.getApprovalNameAndCount());
        contract.setNextApprovalGroup(firstApprovalGroup.getUuid());

        //desactivation of the field
            /*
            if (contract.getItems() != null) {
                for (ContractItem c : contract.getItems()) {
                    c.setDeliveryAddress(objectService.saveAddress(c.getDeliveryAddress()));
                }
            }
            */

        contractItemRepository.saveAll(contract.getItems());
        contract.setContractNumber(utilsService.generateCNumber(companyUuid, contract.getProjectCode(), contract.getSupplierInformation().getSupplierCode()));


        contract = contractRepository.save(contract);

        contractRequestInformation.setStatus(ContractRequestStatus.CONVERTED.getValue());

        contractRequestInformation = contractRequestRepository.save(contractRequestInformation);

        //add the contract items to the dB
        if (contract.getItems() != null) {
            for (ContractItem c : contract.getItems()) {
                //calculate the amounts
                c = objectService.calculateAmountInItems(c);
                c.setContractId(contract.getId());
            }
        }

        //store the subtotal, tax total, and total amount
        contract.setSubTotal(contractItemRepository.subTotalOfContract(contract.getId()));
        contract.setTaxTotal(contractItemRepository.taxTotalForContract(contract.getId()));
        contract.setTotalAmount(contract.getSubTotal().add(contract.getTaxTotal()));

        if (contract.getContractDocuments() != null) {
            for (ContractDocument c : contract.getContractDocuments()) {
                c.setContractId(contract.getId());
            }
        }
        contractItemRepository.saveAll(contract.getItems());
        if (contract.getContractDocuments() != null) {
            contractDocumentRepository.saveAll(contract.getContractDocuments());
        }
        //create and save the audit trail
        ContractAuditTrail contractAuditTrail = contractAuditTrailMapper.createAuditTrail(userName, userUuid, designation, ContractActionEnum.CONVERT_TO_CONTRACT.toString(), Instant.now(), contract.getId(), ContractStatusEnum.PENDING_SUBMISSION.getValue(), contract.getNextApprovalGroup());
        contractAuditTrailRepository.save(contractAuditTrail);
        apiResponse.setMessage(Message.PENDING_CONTRACT_CREATED_SUCCESSFULLY.getValue() + ": " + contract.getContractNumber());
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setData(contract.getContractUuid());


        return apiResponse;
    }

    @Override
    public ApiResponse listBuyerContracts(String companyUuid) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<Contract> contracts = contractRepository.findAllByCompanyUuid(companyUuid);
            List<BuyerListingViewDto> listing = contractMapper.buyerListingView(contracts);
            apiResponse.setData(listing);
            apiResponse.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        }
        return apiResponse;
    }

    @Override
    public ApiResponse listSupplierContracts(String companyUuid) {
        ApiResponse apiResponse = new ApiResponse();
        try {

            List<String> supplierConnectionUuidList = iEntitiesService.getSupplierListWithSupplierCompanyUuid(companyUuid, false).getBody().data;
            List<Contract> contracts = new ArrayList<>();
            for (String supplierVendorConnectionUuid : supplierConnectionUuidList) {
                List<Contract> supplierContractList = contractRepository.findAllBySupplierVendorConnectionUuid(supplierVendorConnectionUuid);
                contracts.addAll(supplierContractList);
            }

            //List<Contract> contracts = contractRepository.findAllBySupplierCompanyUuid(companyUuid);
            List<SupplierListingViewDto> listing = contractMapper.supplierListingView(contracts);
            apiResponse.setData(listing);
            apiResponse.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        }
        return apiResponse;
    }

    @Override
    @Transactional(rollbackOn = {ObjectDoesNotExistException.class, AccessDeniedException.class, BadRequestException.class, Exception.class})
    public ApiResponse submitContract(String companyUuid, String contractUuid, ContractSubmissionDto contractInformation, String contractStatus, String contractAction) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        String userName = (String) authenticationManager.getUserByKey("name");
        String userUuid = (String) authenticationManager.getUserByKey("sub");
        String designation = (String) authenticationManager.getUserByKey("designation");


        //check if the contract request exist
        Optional<Contract> contractExist = contractRepository.findContractByBuyerCompanyUuidAndContractUuid(companyUuid, contractUuid);
        //contract not exists then throws exception, else contract exists
        if (contractExist.isEmpty()) {
            throw new ObjectDoesNotExistException(Message.CONTRACT_DOES_NOT_EXIST.getValue());
        }


        //check if the existing contract status is SAVE_AS_DRAFT or PENDING_SUBMISSION
        if (!(contractExist.get().getContractStatus().equals(ContractStatusEnum.DRAFT_CONTRACT.getValue())
                || contractExist.get().getContractStatus().equals(ContractStatusEnum.PENDING_SUBMISSION.getValue())
                || contractExist.get().getContractStatus().equals(ContractStatusEnum.SENT_BACK.getValue())
                || contractExist.get().getContractStatus().equals(ContractStatusEnum.RECALLED.getValue()))) {
            throw new AccessDeniedException(Message.CONTRACT_SUBMISSION_DENIED.getValue());
        }

        Contract contract = contractMapper.contractSubmission(contractStatus, contractInformation, "contractNumber", Instant.now());
        String contractNumber;
        // check if contract from repo have contract number, generate if dont have Contract number, then map to Contract
        if (contractExist.get().getContractNumber() == null) {
            if (contractInformation.getSupplierInformation() == null) {
                throw new BadRequestException(Message.SUPPLIER_NOT_FOUND.getValue());
            }
            contractNumber = utilsService.generateCNumber(companyUuid, contractInformation.getProjectCode(), contractInformation.getSupplierInformation().getSupplierCode());
        } else {
            contractNumber = contractExist.get().getContractNumber();
        }
        if (UtilsService.PREFIX_TYPE_MANUAL.equalsIgnoreCase(contractNumber)) {
            if (StringUtils.isBlank(contractInformation.getContractNumber())) {
                throw new BadRequestException(Message.MANUAL_CT_NUMBER.getValue());
            } else {
                List<Contract> existingContracts = contractRepository.findContractByBuyerCompanyUuidAndContractNumber(companyUuid, contractInformation.getContractNumber());
                if (!CollectionUtils.isEmpty(existingContracts)) {
                    throw new BadRequestException(Message.NON_UNIQUE_CT_NUMBER.getValue());
                }
                contractNumber = contractInformation.getContractNumber();
            }
        }
        contract.setContractNumber(contractNumber);

        // check for project details
        if (contract.getProjectUuid() != null) {
            if (contract.getProjectName() == null || contract.getProjectUuid() == null || contract.getProjectRfqNo() == null) {
                throw new BadRequestException(Message.CONTRACT_MISSING_PROJECT_DETAILS.getValue());
            }
        }
        // set parameters for contract
        contract.setContractUuid(contractExist.get().getContractUuid());
        contract.setId(contractExist.get().getId());
        contract.setCreatedByName(contractExist.get().getCreatedByName());
        contract.setCreatedByUuid(contractExist.get().getCreatedByUuid());
        contract.setCreatedDate(contractExist.get().getCreatedDate());
        contract.setUpdatedDate(Instant.now());
        contract.setContractRequestNumber(contractExist.get().getContractRequestNumber());
        contract.setContractRequestUuid(contractExist.get().getContractRequestUuid());


        //if the delivery address object exist then reuse
        // why set Del Add for 'contract' as the Del Add gotten from 'contract' itself?
        // can't just save the address itself by only Address address = objectService.saveAddress(contract.getDeliveryAddress(); ?
        if (contract.getDeliveryAddress() != null) {

            contract.setDeliveryAddress(objectService.saveAddress(contract.getDeliveryAddress()));
        }
        //check if the supplier information exist if true than reuse
        // same issue as delivery address
        if (contract.getSupplierInformation() != null) {
            contract.setSupplierInformation(objectService.saveSupplierInformation(contract.getSupplierInformation()));
        }

        //check if the buyer information exist if true than reuse
        // same issue as delivery address
        if (contract.getBuyerInformation() != null) {
            contract.setBuyerInformation(objectService.saveBuyerInformation(contract.getBuyerInformation()));
        } else {
            // This information won't be changed once the PO is issued
            ContactInformation buyerContactPerson = getContactPerson(userUuid);
            BuyerInformation buyerInformation = getBuyerInformation(companyUuid);
            buyerInformation.setContactInformation(buyerContactPerson);
            contract.setBuyerInformation(objectService.saveBuyerInformation(buyerInformation));
        }
        //desactivated
        /*
        if (contract.getItems() != null) {
            for (ContractItem c : contract.getItems()) {
                c.setDeliveryAddress(objectService.saveAddress(c.getDeliveryAddress()));
            }
        }
        */

        // find all items previously saved for this contract and delete them as
        // newly submitted ContractSubmissionDto will have all info gotten from db

        //add the contract items to the dB
        if (contract.getItems() != null) {
            for (ContractItem c : contract.getItems()) {
                //calculate the amounts
                Optional<ContractItem> tempItem = contractItemRepository.findItemByContractId(contract.getId(), c.getItemCode());
                if (tempItem.isPresent()) {
                    c.setId(tempItem.get().getId());
                }
                c = objectService.calculateAmountInItems(c);
                c.setContractId(contract.getId());
            }
        }
        contractItemRepository.saveAll(contract.getItems());
        // setting contract commercials
        contract.setSubTotal(contractItemRepository.subTotalOfContract(contract.getId()));
        contract.setTaxTotal(contractItemRepository.taxTotalForContract(contract.getId()));
        contract.setTotalAmount(contract.getSubTotal().add(contract.getTaxTotal()));

        //set the approval requirements

        //			split the approval sequence to get the first approver group
        ApprovalMatrixDetailsAPIDto approvalMatrixResponse = iEntitiesService.getApprovalMatrixDetails(contract.getApprovalRouteUuid(),
                contract.getTotalAmount(), contract.getCurrencyCode(), companyUuid);
        String approvalRouteSequence = approvalMatrixResponse.getApprovalGroupSequence();

        String approvalRouteName = approvalMatrixResponse.getApprovalMatrixName();

        contract.setApprovalRouteName(approvalRouteName);
        contract.setApprovalRouteSequence(approvalRouteSequence);

        if (!contractAction.equals(ContractActionEnum.SAVE_DRAFT)) {
            // Set first approval
            ApprovalGroupDetail firstApprovalGroup = approvalMatrixResponse.getFirstApproval();
            contract.setNextApprover(firstApprovalGroup.getApprovalNameAndCount());
            contract.setNextApprovalGroup(firstApprovalGroup.getUuid());
        }

        if (contract.getContractDocuments() != null) {
            for (ContractDocument c : contract.getContractDocuments()) {
                c.setContractId(contract.getId());
                c.setAttachment(false);
            }
        }

        if (contract.getContractDocuments() != null) {
            contractDocumentRepository.saveAll(contract.getContractDocuments());
        }

        contract = contractRepository.save(contract);
        //create and save the audit trail
        ContractAuditTrail contractAuditTrail = contractAuditTrailMapper.createAuditTrail(userName, userUuid, designation, contractAction, Instant.now(), contract.getId(), contractStatus, contract.getNextApprovalGroup());
        contractAuditTrailRepository.save(contractAuditTrail);
        if (contractStatus.equals(ContractStatusEnum.PENDING_APPROVAL.getValue())) {
            apiResponse.setMessage(Message.CONTRACT_SUBMITTED_SUCCESSFULLY.getValue() + ": " + contract.getContractNumber());
        } else {
            apiResponse.setMessage(Message.CONTRACT_SAVED_SUCCESSFULLY.getValue() + ": " + contract.getContractNumber());
        }
        apiResponse.setData(contract.getContractUuid());
        apiResponse.setStatus(HttpStatus.OK);


        return apiResponse;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class, ObjectDoesNotExistException.class, AccessDeniedException.class})
    public ApiResponse changeContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        String userName = (String) authenticationManager.getUserByKey("name");
        String userUuid = (String) authenticationManager.getUserByKey("sub");
        String designation = (String) authenticationManager.getUserByKey("designation");

        //check if the contract status is pending approval
        Optional<Contract> contract = contractRepository.findContractByBuyerCompanyUuidAndContractUuid(companyUuid, contractUuid);
        if (contract.isEmpty()) {
            throw new ObjectDoesNotExistException(Message.CONTRACT_DOES_NOT_EXIST.getValue());
        }
        if (!(contract.get().getContractStatus().equals(ContractStatusEnum.PENDING_APPROVAL.getValue()))) {
            throw new AccessDeniedException(Message.CONTRACT_STATUS_CHANGE_DENIED.getValue());
        }
        //TODO check if the user has permission to change status
        contract.get().setContractStatus(contractStatus);
        //reset the approval route

        //			split the approval sequence to get the first approver group
        ApprovalMatrixDetailsAPIDto approvalMatrixResponse = iEntitiesService.getApprovalMatrixDetails(contract.get().getApprovalRouteUuid(),
                contract.get().getTotalAmount(), contract.get().getCurrencyCode(), companyUuid);
        String approvalRouteSequence = approvalMatrixResponse.getApprovalGroupSequence();

        String approvalRouteName = approvalMatrixResponse.getApprovalMatrixName();

        contract.get().setApprovalRouteName(approvalRouteName);
        contract.get().setApprovalRouteSequence(approvalRouteSequence);
        // Set first approval
        ApprovalGroupDetail firstApprovalGroup = approvalMatrixResponse.getFirstApproval();
        contract.get().setNextApprover(firstApprovalGroup.getApprovalNameAndCount());
        contract.get().setNextApprovalGroup(firstApprovalGroup.getUuid());

        contract.get().setUpdatedDate(Instant.now());
        Contract saveContract = contractRepository.save(contract.get());
        //create and save the audit trail
        ContractAuditTrail contractAuditTrail = contractAuditTrailMapper.createAuditTrail(userName, userUuid, designation, contractAction, Instant.now(), saveContract.getId(), contractStatus, saveContract.getNextApprovalGroup());
        contractAuditTrailRepository.save(contractAuditTrail);
        //set the message
        String message = new String();
        if (saveContract.getContractStatus().equals(ContractStatusEnum.REJECTED.getValue())) {
            message = Message.CONTRACT_REJECTED.getValue();
        } else {
            message = Message.CONTRACT_SENDBACK.getValue();
        }
        apiResponse.setMessage(message + ": " + saveContract.getContractNumber());
        apiResponse.setStatus(HttpStatus.OK);


        return apiResponse;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class, ObjectDoesNotExistException.class, AccessDeniedException.class, ApproveException.class})
    public ApiResponse approveContract(String companyUuid, String contractUuid) throws Exception {
        ApiResponse apiResponse = new ApiResponse();

        String userName = (String) authenticationManager.getUserByKey("name");
        String userUuid = (String) authenticationManager.getUserByKey("sub");
        String designation = (String) authenticationManager.getUserByKey("designation");

        //check if the contract status is pending approval
        Optional<Contract> contract = contractRepository.findContractByBuyerCompanyUuidAndContractUuid(companyUuid, contractUuid);

        if (contract.isEmpty()) {
            throw new ObjectDoesNotExistException(Message.CONTRACT_DOES_NOT_EXIST.getValue());
        }
        if (!(contract.get().getContractStatus().equals(ContractStatusEnum.PENDING_APPROVAL.getValue()))) {
            throw new AccessDeniedException(Message.CONTRACT_STATUS_CHANGE_DENIED.getValue());
        }


        // check if the creator is the approver
        if (contract.get().getCreatedByUuid().equals(userUuid)) {
            throw new ApproveException(Message.CREATOR_CANNOT_APPROVE.getValue());
        }

        HashMap<String, ContractAuditTrail> auditTrail = new HashMap<>();
        List<ContractAuditTrail> pastAuditTrails = contractAuditTrailRepository.auditTrailByContractId(contract.get().getId());
        int countApprovers = countNumberOfApproval(pastAuditTrails, auditTrail);

        ApprovalMatrixDetailsAPIDto approvalMatrixDetailsAPIDto =
                entitiesService.getApprovalMatrixDetails(
                        contract.get().getApprovalRouteUuid(),
                        contract.get().getTotalAmount(),
                        contract.get().getCurrencyCode(),
                        companyUuid
                );
        log.info("Approval matrix {}: " + approvalMatrixDetailsAPIDto.toString());
        ApprovalGroupDetail currentApprovalGroup = approvalMatrixDetailsAPIDto.getCurrentApprovalGroup(contract.get().getNextApprovalGroup());
        log.info("currentApprovalGroup {}: " + currentApprovalGroup.toString());
        //check if the user has approved before
        if (approvalSequence.hasApproverApproveBefore(userUuid, contract.get().getId(), contract.get().getNextApprovalGroup())) {
            throw new ApproveException(Message.APPROVER_CANNOT_APPROVE_AGAIN.getValue());
        }

        var contractApproved = handleApproveAction(approvalMatrixDetailsAPIDto, contract.get(), countApprovers);

        contractApproved.setUpdatedDate(Instant.now());
        Contract saveContract =
                contractRepository.save(
                        contractApproved
                );
        //create and save the audit trail
        ContractAuditTrail contractAuditTrail = contractAuditTrailMapper.createAuditTrail(userName, userUuid, designation, ContractActionEnum.APPROVE.toString(), Instant.now(), saveContract.getId(), saveContract.getContractStatus(), saveContract.getNextApprovalGroup());
        contractAuditTrailRepository.save(contractAuditTrail);

        apiResponse.setMessage(Message.CONTRACT_APPROVED.getValue() + ": " + saveContract.getContractNumber());
        apiResponse.setStatus(HttpStatus.OK);


        return apiResponse;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ApiResponse recallContract(String companyUuid, String contractUuid) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String userName = (String) authenticationManager.getUserByKey("name");
            String userUuid = (String) authenticationManager.getUserByKey("sub");
            String designation = (String) authenticationManager.getUserByKey("designation");

            //check if the contract status is pending approval
            Optional<Contract> contract = contractRepository.findContractByBuyerCompanyUuidAndContractUuid(companyUuid, contractUuid);
            if (contract.isEmpty()) {
                throw new ObjectDoesNotExistException(Message.CONTRACT_DOES_NOT_EXIST.getValue());
            }
            if (!(contract.get().getContractStatus().equals(ContractStatusEnum.PENDING_APPROVAL.getValue()))) {
                throw new AccessDeniedException(Message.CONTRACT_STATUS_CHANGE_DENIED.getValue());
            }
            //TODO check if the user has permission to change status

            //check if anyone has approved it
            List<ContractAuditTrail> auditTrailList = contractAuditTrailRepository.auditTrailByContractId(contract.get().getId());
            if (auditTrailList.get(0).getAction().equals(ContractActionEnum.APPROVE.toString())) {
                throw new ApproveException(Message.CONTRACT_CANNOT_BE_RECALLED.getValue());
            }

            contract.get().setContractStatus(ContractStatusEnum.RECALLED.getValue());
            //reset the approval route
//			split the approval sequence to get the first approver group
            ApprovalMatrixDetailsAPIDto approvalMatrixResponse = iEntitiesService.getApprovalMatrixDetails(contract.get().getApprovalRouteUuid(),
                    contract.get().getTotalAmount(), contract.get().getCurrencyCode(), companyUuid);
            String approvalRouteSequence = approvalMatrixResponse.getApprovalGroupSequence();

            String approvalRouteName = approvalMatrixResponse.getApprovalMatrixName();

            contract.get().setApprovalRouteName(approvalRouteName);
            contract.get().setApprovalRouteSequence(approvalRouteSequence);
            // Set first approval
            ApprovalGroupDetail firstApprovalGroup = approvalMatrixResponse.getFirstApproval();
            contract.get().setNextApprover(firstApprovalGroup.getApprovalNameAndCount());
            contract.get().setNextApprovalGroup(firstApprovalGroup.getUuid());

            contract.get().setUpdatedDate(Instant.now());
            Contract saveContract = contractRepository.save(contract.get());
            //create and save the audit trail
            ContractAuditTrail contractAuditTrail = contractAuditTrailMapper.createAuditTrail(userName, userUuid, designation, ContractActionEnum.RECALL.toString(), Instant.now(), saveContract.getId(), ContractStatusEnum.PENDING_SUBMISSION.getValue(), saveContract.getNextApprovalGroup());
            contractAuditTrailRepository.save(contractAuditTrail);
            apiResponse.setMessage(Message.CONTRACT_RECALLED.getValue() + ": " + saveContract.getContractNumber());
            apiResponse.setStatus(HttpStatus.OK);

        } catch (ApproveException e) {
            apiResponse.setStatus(HttpStatus.FORBIDDEN);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (AccessDeniedException e) {
            apiResponse.setStatus(HttpStatus.FORBIDDEN);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (ObjectDoesNotExistException e) {
            apiResponse.setStatus(HttpStatus.NOT_FOUND);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        }
        return apiResponse;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ApiResponse issueContract(String companyUuid, String contractUuid) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String userName = (String) authenticationManager.getUserByKey("name");
            String userUuid = (String) authenticationManager.getUserByKey("sub");
            String designation = (String) authenticationManager.getUserByKey("designation");

            //check if the contract status is pending approval
            Optional<Contract> contract = contractRepository.findContractByBuyerCompanyUuidAndContractUuid(companyUuid, contractUuid);
            if (contract.isEmpty()) {
                throw new ObjectDoesNotExistException(Message.CONTRACT_DOES_NOT_EXIST.getValue());
            }
            if (!(contract.get().getContractStatus().equals(ContractStatusEnum.PENDING_ISSUE.getValue()))) {
                throw new AccessDeniedException(Message.CONTRACT_STATUS_CHANGE_DENIED.getValue());
            }
            //TODO check if the user has permission to change status

            contract.get().setContractStatus(ContractStatusEnum.PENDING_ACKNOWLEDGEMENT.getValue());
            contract.get().setIssuedDate(Instant.now());
            contract.get().setIssuedBy(userName);
            contract.get().setUpdatedDate(Instant.now());
            Contract saveContract = contractRepository.save(contract.get());
            //create and save the audit trail
            ContractAuditTrail contractAuditTrail = contractAuditTrailMapper.createAuditTrail(userName, userUuid, designation, ContractActionEnum.ISSUE.toString(), Instant.now(), saveContract.getId(), ContractStatusEnum.PENDING_ACKNOWLEDGEMENT.getValue(), saveContract.getNextApprovalGroup());
            contractAuditTrailRepository.save(contractAuditTrail);

            apiResponse.setMessage(Message.CONTRACT_ISSUED.getValue() + ": " + saveContract.getContractNumber());
            apiResponse.setStatus(HttpStatus.OK);

        } catch (AccessDeniedException e) {
            apiResponse.setStatus(HttpStatus.FORBIDDEN);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (ObjectDoesNotExistException e) {
            apiResponse.setStatus(HttpStatus.NOT_FOUND);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        }
        return apiResponse;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ApiResponse supplierChangeContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String userName = (String) authenticationManager.getUserByKey("name");
            String userUuid = (String) authenticationManager.getUserByKey("sub");
            String designation = (String) authenticationManager.getUserByKey("designation");

            //check if the contract status is pending approval
            Optional<Contract> contract = contractRepository.findContractBySupplierCompanyUuidAndContractUuid(companyUuid, contractUuid);
            if (contract.isEmpty()) {
                throw new ObjectDoesNotExistException(Message.CONTRACT_DOES_NOT_EXIST.getValue());
            }
            if (!(contract.get().getContractStatus().equals(ContractStatusEnum.PENDING_ACKNOWLEDGEMENT.getValue()))) {
                throw new AccessDeniedException(Message.CONTRACT_STATUS_CHANGE_DENIED.getValue());
            }
            //TODO check if the user has permission to change status

            contract.get().setContractStatus(contractStatus);
            if (contractStatus.equals(ContractStatusEnum.COMPLETE.getValue())) {
                contract.get().setAcknowledgeDate(Instant.now());
            }
            contract.get().setUpdatedDate(Instant.now());
            Contract saveContract = contractRepository.save(contract.get());
            //create and save the audit trail
            ContractAuditTrail contractAuditTrail = contractAuditTrailMapper.createAuditTrail(userName, userUuid, designation, contractAction, Instant.now(), saveContract.getId(), saveContract.getContractStatus(), saveContract.getNextApprovalGroup());
            contractAuditTrailRepository.save(contractAuditTrail);
            if (saveContract.getContractStatus().equals(ContractStatusEnum.COMPLETE.getValue())) {
                apiResponse.setMessage(Message.CONTRACT_ACKNOWLEDGE.getValue() + ": " + saveContract.getContractNumber());
            } else {
                apiResponse.setMessage(Message.CONTRACT_REJECTED.getValue() + ": " + saveContract.getContractNumber());
            }
            apiResponse.setStatus(HttpStatus.OK);

        } catch (AccessDeniedException e) {
            apiResponse.setStatus(HttpStatus.FORBIDDEN);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (ObjectDoesNotExistException e) {
            apiResponse.setStatus(HttpStatus.NOT_FOUND);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        }
        return apiResponse;
    }

    @Override
    public ApiResponse changeRejectandSendBackContractStatus(String companyUuid, String contractUuid, String contractStatus, String contractAction) throws Exception {
        return this.changeContractStatus(companyUuid, contractUuid, contractStatus, contractAction);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ApiResponse terminateContract(String companyUuid, String contractUuid) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            String userName = (String) authenticationManager.getUserByKey("name");
            String userUuid = (String) authenticationManager.getUserByKey("sub");
            String designation = (String) authenticationManager.getUserByKey("designation");

            //check if the contract status is pending approval
            Optional<Contract> contract = contractRepository.findContractByBuyerCompanyUuidAndContractUuid(companyUuid, contractUuid);
            if (contract.isEmpty()) {
                throw new ObjectDoesNotExistException(Message.CONTRACT_DOES_NOT_EXIST.getValue());
            }
            if (!(contract.get().getContractStatus().equals(ContractStatusEnum.COMPLETE.getValue()))) {
                throw new AccessDeniedException(Message.CONTRACT_STATUS_CHANGE_DENIED.getValue());
            }
            //TODO check if the user has permission to change status
            contract.get().setContractStatus(ContractStatusEnum.TERMINATED.getValue());

            Contract saveContract = contractRepository.save(contract.get());
            //create and save the audit trail
            ContractAuditTrail contractAuditTrail = contractAuditTrailMapper.createAuditTrail(userName, userUuid, designation, ContractActionEnum.TERMINATE.toString(), Instant.now(), saveContract.getId(), ContractStatusEnum.TERMINATED.getValue(), saveContract.getNextApprovalGroup());
            contractAuditTrailRepository.save(contractAuditTrail);

            apiResponse.setMessage(Message.CONTRACT_TERMINATED.getValue() + ": " + saveContract.getContractNumber());
            apiResponse.setStatus(HttpStatus.OK);

        } catch (AccessDeniedException e) {
            apiResponse.setStatus(HttpStatus.FORBIDDEN);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (ObjectDoesNotExistException e) {
            apiResponse.setStatus(HttpStatus.NOT_FOUND);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        }
        return apiResponse;
    }

    private int countNumberOfApproval(List<ContractAuditTrail> purchaseReqAuditTrail, HashMap<String, ContractAuditTrail> auditTrail) {
        int counter = 0;
        if (purchaseReqAuditTrail != null) {
            for (ContractAuditTrail p : purchaseReqAuditTrail) {
                if (!p.getAction().equals(ContractActionEnum.APPROVE.toString())) {
//                    counter++;
//                    auditTrail.put(p.getUserUuid(), p);
                    break;
                } else {
                    counter++;
                    auditTrail.put(p.getUserUuid(), p);
                }
            }
        }
        return counter;
    }

    private Contract handleApproveAction(ApprovalMatrixDetailsAPIDto approvalMatrixDetailsAPIDto, Contract contract, int countApprovers) throws AccessDeniedException {
        String requesterUuid = (String) authenticationManager.getUserByKey("sub");
        ApprovalGroupDetail currentApprovalGroup = approvalMatrixDetailsAPIDto.getCurrentApprovalGroup(contract.getNextApprovalGroup());
        if (!currentApprovalGroup.isUserInGroup(requesterUuid) || requesterUuid.equals(contract.getCreatedByUuid())) {
            throw new AccessDeniedException(Message.ACCESS_DENIED.getValue());
        }
        ApprovalGroupDetail nextApprovalGroup = approvalMatrixDetailsAPIDto.getNextApprovalUuid(currentApprovalGroup.getUuid());
        if ((countApprovers + 1) >= currentApprovalGroup.getTotalApproves()) {
            if (nextApprovalGroup != null) {
                contract.setNextApprover(nextApprovalGroup.getApprovalNameAndCount());
                contract.setNextApprovalGroup(nextApprovalGroup.getUuid());
                contract.setApprovedDate(Instant.now());
                contract.setContractStatus(ContractStatusEnum.PENDING_APPROVAL.getValue());
            } else {
                contract.setNextApprover(null);
                contract.setApprovedDate(Instant.now());
                String status = contract.isESignRouting() ? ContractStatusEnum.PENDING_ESIGN.getValue() : ContractStatusEnum.PENDING_ISSUE.getValue();
                contract.setContractStatus(status);
            }
        }
        return contract;
    }


    private BuyerInformation getBuyerInformation(String companyUuid) {
        // NOTE: This information is temporary, it will be persisted once PO is issued
        // At this time, the information of buyer and supplier can be changed, but once the PO is issued, it's immutable
        CompanyDetails buyerInformation = iOauthService.getCompanyDetails(companyUuid);
        BuyerInformation buyer = new BuyerInformation();
        buyer.setCompanyName(buyerInformation.getCompanyName());
        buyer.setCompanyCountry(buyerInformation.getCountryOfOrigin());
        buyer.setTaxRegNo(buyerInformation.getGstRegNo());
        buyer.setBuyerCode(buyerInformation.getUen());
        buyer.setBuyerCompanyUuid(companyUuid);
        buyer.setBuyerVendorConnectionUuid(buyerInformation.getUuid());
        buyer.setCompanyAddress(objectService.saveAddress(getBuyerAddressDetails(companyUuid)));

        return buyer;
    }

    private Address getBuyerAddressDetails(String companyUuid) {
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

    private ContactInformation getContactPerson(String requesterUuid) {
        // Contact person is the person who creates PO
        UserDetail buyerContactPerson = iOauthService.getUserDetails(requesterUuid);
        ContactInformation contactPerson = new ContactInformation();
        contactPerson.setContactEmail(buyerContactPerson.getEmail());
        contactPerson.setContactNumber(buyerContactPerson.getWorkNumber());
        contactPerson.setContactName(buyerContactPerson.getName());
        return contactPerson;
    }

    private SupplierInformation setSupplierInformation(SupplierInfo supplierInfo) {
        SupplierInformation supplierInformation = new SupplierInformation();
        supplierInformation.setSupplierCode(supplierInfo.getCompanyCode());
        supplierInformation.setCompanyCountry(supplierInfo.getCountryOfOrigin());
        supplierInformation.setCompanyName(supplierInfo.getCompanyName());
        supplierInformation.setSupplierCompanyUuid(supplierInfo.getSupplierCompanyUuid());
        supplierInformation.setSupplierVendorConnectionUuid(supplierInfo.getUuid());
        supplierInformation.setTaxRegNo(supplierInfo.getGstRegNo());
        return supplierInformation;
    }


    /**
     * this is for private internal service for converting RFQ to Contract
     *
     * @param companyUuid
     * @param supplierUuid
     * @param rfqDto
     * @return
     */
    @SneakyThrows
    public ApiResponse convertRfqToContract(String companyUuid, String supplierUuid, RfqDto rfqDto) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<QuoteItemDto> quoteItems = rfqDto.getQuoteItemList();
            log.info("RFQ item size {}", quoteItems.size());
            if (quoteItems.isEmpty()) {
                throw new BadRequestException("RFQ item is empty");
            }
            List<RfqDocumentMetaDataDto> rfqDocuments = rfqDto.getRfqDocumentList();
            Optional<Contract> optionalContract = contractRepository.findByRfqUuidAndSupplierUuid(rfqDto.getUuid(), supplierUuid);
            if (optionalContract.isPresent()) {
                throw new BadRequestException("This supplier has been converted!");
            }

            List<ContractItem> contractItems = processRfqItemToContractItem(quoteItems);
            log.info("Contract item size after mapper {}", contractItems.size());

            List<ContractDocument> contractDocuments = new ArrayList<>();
            for (RfqDocumentMetaDataDto rfqDoc : rfqDocuments) {
                ContractDocument contractDocument = contractInformationMapper.fromRfqDocument(rfqDoc);
                contractDocument.setAttachment(true);
                contractDocuments.add(contractDocument);
            }

            Contract contract = convertRfq(rfqDto);

            //set supplier info
            SupplierInfo supplierInfo = iEntitiesService.getSupplierInfo(companyUuid, supplierUuid);
            if (supplierInfo == null) {
                throw new BadRequestException(Message.SUPPLIER_NOT_FOUND.getValue());
            }
            ContactInformation supplierContactInfo = objectService.saveContactInformation(vendorHelper.extractContactFromExternalVendor(supplierInfo));
            Address supplierAddress = objectService.saveAddress(vendorHelper.extractAddressFromExternalVendor(supplierInfo));
            SupplierInformation supplierInformation = setSupplierInformation(supplierInfo);
            supplierInformation.setContactInformation(supplierContactInfo);
            supplierInformation.setCompanyAddress(supplierAddress);
            contract.setSupplierInformation(objectService.saveSupplierInformation(supplierInformation));

            // set buyer info
            // Get buyer information from manage external vendor and persist in DB, this information is from buyer point of view
            // If supplier is onboarded
            //TODO for unconnected/ not onboarded supplier, there will be an event published when new vendor is created by using rabbmitMQ or Kafka
            BuyerInformation buyerInformation = null;
            if (StringUtils.isNotEmpty(supplierInformation.getSupplierCompanyUuid())) {
                SupplierInfo buyer = iEntitiesService.getSupplierInfo(contract.getSupplierInformation().getSupplierCompanyUuid(), companyUuid);
                if (buyer != null) {
                    ContactInformation buyerContactPerson = vendorHelper.extractContactFromExternalVendor(buyer);
                    Address buyerDefaultAddress = vendorHelper.extractAddressFromExternalVendor(buyer);
                    buyerInformation = vendorHelper.extractBuyerInformation(buyer);
                    buyerInformation.setBuyerCompanyUuid(companyUuid);
                    buyerContactPerson = objectService.saveContactInformation(buyerContactPerson);
                    buyerDefaultAddress = objectService.saveAddress(buyerDefaultAddress);
                    buyerInformation.setContactInformation(buyerContactPerson);
                    buyerInformation.setCompanyAddress(buyerDefaultAddress);
                    buyerInformation = objectService.saveBuyerInformation(buyerInformation);
                    contract.setBuyerInformation(buyerInformation);
                }
            } else {
                buyerInformation = getBuyerInformation(companyUuid);
                contract.setBuyerInformation(objectService.saveBuyerInformation(buyerInformation));
            }

            //set amount
            setContractAmount(contract, contractItems);
            //set general values
            contract.setContractUuid(utilsService.generateUUID());
            contract.setContractStatus(ContractStatusEnum.PENDING_SUBMISSION.getValue());
            contract.setContractNumber(utilsService.generateCNumber(companyUuid, contract.getProjectCode(), supplierInfo.getCompanyCode()));
            contract.setContractType(StringUtils.isEmpty(rfqDto.getRequisitionType()) ? "General" : rfqDto.getRequisitionType());
            contract.setContractStatus(ContractStatusEnum.PENDING_SUBMISSION.getValue());
            contract.setCreatedDate(Instant.now());
            contract.setUpdatedDate(Instant.now());
            Contract saved = contractRepository.save(contract);

            //set items and documents
            log.info("[{}] Contract is saved successfully", contract.getContractNumber());
            contractItems = contractItems.stream().peek(c -> {
                c.setContractId(saved.getId());
                c.setId(null);
            }).collect(Collectors.toList());
            log.info("[{}]Contract items {}", contract.getContractNumber(), contractItems.size());
            contractDocuments = contractDocuments.stream().peek(item -> item.setContractId(saved.getId())).collect(Collectors.toList());
            List<ContractItem> savedItems = contractItemRepository.saveAll(contractItems);
            saved.setItems(contractItems);
            log.info("[{}]Contract items after saved {}", contract.getContractNumber(), contractItems.size());
            contractDocumentRepository.saveAll(contractDocuments);
            saved.setContractDocuments(contractDocuments);
            //create and save the audit trail
            ContractAuditTrail contractAuditTrail = contractAuditTrailMapper.createAuditTrail(rfqDto.getRequesterName(), rfqDto.getRequesterUuid(), null, ContractActionEnum.CONVERT_TO_CONTRACT.toString(), Instant.now(), contract.getId(), ContractStatusEnum.PENDING_SUBMISSION.getValue(), contract.getNextApprovalGroup());
            contractAuditTrailRepository.save(contractAuditTrail);

            ContractInfoApiDto contractInfoApiDto = new ContractInfoApiDto();
            contractInfoApiDto.setContractUuid(saved.getContractUuid());
            contractInfoApiDto.setContractNumber(saved.getContractNumber());
            contractInfoApiDto.setConvertedFromRFQ(numberOfContractConvertedFromRFQ(companyUuid, saved.getRfqUuid()));

            apiResponse.setData(contractInfoApiDto);
            apiResponse.setMessage(Message.PENDING_CONTRACT_CREATED_SUCCESSFULLY.getValue() + ": " + contract.getContractNumber());
            apiResponse.setStatus(HttpStatus.OK);
        } catch (BadRequestException e) {
            apiResponse.setStatus(HttpStatus.BAD_REQUEST);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        } catch (ObjectDoesNotExistException e) {
            apiResponse.setStatus(HttpStatus.NOT_FOUND);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
        }
        return apiResponse;
    }

    private List<ContractItem> processRfqItemToContractItem(List<QuoteItemDto> quoteItems) {
        List<ContractItem> contractItems = new ArrayList<>();
        for (QuoteItemDto quoteItem : quoteItems) {
            ContractItem contractItem = contractItemMapper.fromRfqItem(quoteItem);
            contractItem = objectService.calculateAmountInItems(contractItem);
            contractItems.add(contractItem);
        }
        return contractItems;
    }

    private Contract convertRfq(RfqDto rfqDto) {
        Contract contract = new Contract();
        contract.setCreatedByName(rfqDto.getRequesterName());
        contract.setCreatedByUuid(rfqDto.getRequesterUuid());
        contract.setCurrencyCode(rfqDto.getCurrencyCode());
        contract.setRfqNumber(rfqDto.getRfqNumber());
        contract.setRfqUuid(rfqDto.getUuid());
        contract.setContractStartDate(rfqDto.getValidityStartDate());
        contract.setContractEndDate(rfqDto.getValidityEndDate());
        Address address = addressMapper.address(rfqDto.getDeliveryAddress());
        contract.setDeliveryAddress(objectService.saveAddress(address));
        contract.setDeliveryDate(rfqDto.getDeliveryDate());
        contract.setContractTitle(rfqDto.getRfqTitle());
        contract.setProjectName(rfqDto.getProjectTitle());
        contract.setProjectUuid(rfqDto.getProjectUuid());
        contract.setProjectCode(rfqDto.getProjectCode());
        contract.setNatureOfContract(String.valueOf(rfqDto.isProject()));
        contract.setContractStartDate(rfqDto.getValidityStartDate());
        contract.setContractEndDate(rfqDto.getValidityEndDate());
        contract.setProcurementType(rfqDto.getProcurementType());
        return contract;
    }

    private void setContractAmount(Contract contract, List<ContractItem> contractItems) {
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
        for (ContractItem item : contractItems) {
            subTotal = subTotal.add(item.getInSourceCurrency());
            taxAmount = taxAmount.add(item.getTaxAmount());
        }
        BigDecimal totalAmount = subTotal.add(taxAmount);
        contract.setTaxTotal(taxAmount);
        contract.setSubTotal(subTotal);
        contract.setTotalAmount(totalAmount);
    }

    private int numberOfContractConvertedFromRFQ(String companyUuid, String rfqUuid) {
        return contractRepository.countContractsByRfq(companyUuid, rfqUuid);
    }


}
