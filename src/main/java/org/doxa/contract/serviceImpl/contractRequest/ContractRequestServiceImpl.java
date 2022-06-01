package org.doxa.contract.serviceImpl.contractRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.doxa.contract.DTO.contractRequest.*;
import org.doxa.auth.DoxaAuthenticationManager;
import org.doxa.contract.enums.ContractRequestStatus;
import org.doxa.contract.config.Message;
import org.doxa.contract.exceptions.AccessDeniedException;
import org.doxa.contract.exceptions.BadRequestException;
import org.doxa.contract.exceptions.ResourceNotFoundException;
import org.doxa.contract.mapper.AddressMapper;
import org.doxa.contract.mapper.VendorInformationMapper;
import org.doxa.contract.microservices.DTO.ApprovalGroupDetail;
import org.doxa.contract.microservices.DTO.ApprovalMatrixDetailsAPIDto;
import org.doxa.contract.microservices.DTO.ProjectDetailsApiDto;
import org.doxa.contract.microservices.DTO.entityService.IEntitiesService;
import org.doxa.contract.models.contractRequest.ContractRequest;
import org.doxa.contract.models.contractRequest.ContractRequestAuditTrail;
import org.doxa.contract.models.contractRequest.ContractRequestDocument;
import org.doxa.contract.models.contractRequest.ContractRequestItem;
import org.doxa.contract.repositories.contractRequest.ContractRequestAuditTrailRepository;
import org.doxa.contract.repositories.contractRequest.ContractRequestDocumentRepository;
import org.doxa.contract.repositories.contractRequest.ContractRequestItemRepository;
import org.doxa.contract.repositories.contractRequest.ContractRequestRepository;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.serviceImpl.validators.EntityServiceValidator;
import org.doxa.contract.services.contractRequest.IContractRequestService;
import org.doxa.contract.utils.ObjectService;
import org.doxa.contract.utils.UtilsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContractRequestServiceImpl implements IContractRequestService {
    private static final Logger LOG = LoggerFactory.getLogger(ContractRequestServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VendorInformationMapper vendorInformationMapper;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private DoxaAuthenticationManager authenticationManager;

    @Autowired
    private IEntitiesService entitiesService;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ObjectService objectService;

    @Autowired
    private ContractRequestRepository contractRequestRepository;

    @Autowired
    private ContractRequestItemRepository contractRequestItemRepository;

    @Autowired
    private ContractRequestAuditTrailRepository contractRequestAuditTrailRepository;

    @Autowired
    private ContractRequestDocumentRepository contractRequestDocumentRepository;

    @Autowired
    private EntityServiceValidator entityServiceValidator;

    @Override
    public ApiResponse listContractRequest(String companyUuid) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<ContractRequest> contractRequestList = contractRequestRepository.findByCompanyUuid(companyUuid);
            List<ListCRDto> contractRequestDtoList = new ArrayList<>();
            for (ContractRequest contractRequest : contractRequestList) {
                if (!contractRequest.getStatus().equals(ContractRequestStatus.CONVERTED.getValue())) {
                    ListCRDto crDto = modelMapper.map(contractRequest, ListCRDto.class);
                    contractRequestDtoList.add(crDto);
                }
            }
            apiResponse.setData(contractRequestDtoList);
            apiResponse.setStatus(HttpStatus.OK);
            return apiResponse;
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.BAD_REQUEST);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
            return apiResponse;
        }
    }

    @Override
    public ApiResponse retrieveContractRequestDetails(String companyUuid, String contractRequestUuid) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional<ContractRequest> optContractRequest = contractRequestRepository.findByCompanyUuidAndContractRequestUuid(companyUuid, contractRequestUuid);
            if (optContractRequest.isEmpty()) {
                throw new BadRequestException(Message.CONTRACT_REQUEST_NOT_FOUND.getValue());
            }
            ContractRequest contractRequest = optContractRequest.get();
            DetailsCRDto contractRequestDto = modelMapper.map(contractRequest, DetailsCRDto.class);

            String requesterUuid = (String) authenticationManager.getUserByKey("sub");
            if (requesterUuid.equals(contractRequestDto.getCreatedByUuid())) {
                contractRequestDto.setContractCreator(true);
                contractRequestDto.setApproverRole(false);
                contractRequestDto.setHasApproved(false);
            } else {
                contractRequestDto.setContractCreator(false);
                if (contractRequestDto.getNextApprovalGroup() != null) {
                    ApprovalGroupDetail currentApprovalGroup = entitiesService.getApprovalGroupDetail(companyUuid, contractRequestDto.getNextApprovalGroup());
                    contractRequestDto.setApproverRole(currentApprovalGroup != null && currentApprovalGroup.isUserInGroup(requesterUuid));
                } else {
                    contractRequestDto.setApproverRole(false);
                }
            }

            List<ContractRequestItem> contractRequestItemList = contractRequestItemRepository.findByContractRequestUuid(contractRequestUuid);
            List<CRItemDto> crItemDtoList = new ArrayList<>();
            for (ContractRequestItem contractRequestItem : contractRequestItemList) {
                CRItemDto crItemDto = modelMapper.map(contractRequestItem, CRItemDto.class);
                crItemDtoList.add(crItemDto);
            }
            contractRequestDto.setContractItemList(crItemDtoList);

            List<ContractRequestDocument> contractRequestDocumentList = contractRequestDocumentRepository.findByContractRequestUuid(contractRequestUuid);
            List<CRDocumentDto> crDocumentDtoList = new ArrayList<>();
            for (ContractRequestDocument contractRequestDocument : contractRequestDocumentList) {
                CRDocumentDto crDocumentDto = modelMapper.map(contractRequestDocument, CRDocumentDto.class);
                crDocumentDtoList.add(crDocumentDto);
            }
            contractRequestDto.setDocumentList(crDocumentDtoList);

            List<ContractRequestAuditTrail> contractRequestAuditTrailList = contractRequestAuditTrailRepository.findByContractRequestUuid(contractRequestUuid);
            List<CRAuditTrailDto> crAuditTrailDtoList = new ArrayList<>();
            for (ContractRequestAuditTrail contractRequestAuditTrail : contractRequestAuditTrailList) {
                CRAuditTrailDto crAuditTrailDto = modelMapper.map(contractRequestAuditTrail, CRAuditTrailDto.class);
                crAuditTrailDtoList.add(crAuditTrailDto);
            }

            HashMap<String, ContractRequestAuditTrail> auditTrail = new HashMap<>();
            int countApprovers = countNumberOfApproval(contractRequestAuditTrailList, auditTrail);
            contractRequestDto.setFirstApproved(countApprovers > 0);
//			if user is part of the "past approvers" list
            contractRequestDto.setHasApproved(auditTrail.containsKey(requesterUuid));

            contractRequestDto.setAuditTrailList(crAuditTrailDtoList);

            apiResponse.setData(contractRequestDto);
            apiResponse.setStatus(HttpStatus.OK);
            return apiResponse;
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.BAD_REQUEST);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
            return apiResponse;
        }
    }

    @Override
    public ApiResponse submitContractRequest(String companyUuid, CreateCRDtoWithValidation submitDto) throws Exception {
        //map DTO with validation to general DTO
        List<CRItemDto> itemDtoList = new ArrayList<>();
        for (CreateCRItemDtoWithValidation validateItemDto : submitDto.getContractItemList()) {
            CRItemDto mappedItem = new CRItemDto(validateItemDto);
            itemDtoList.add(mappedItem);
        }

        CreateCRDto crDto = new CreateCRDto(submitDto, itemDtoList);

        return createContractRequest(companyUuid, crDto, false);
    }

    @Override
    public ApiResponse editSubmitContractRequest(String companyUuid, EditCRDtoWithValidation editDto) throws Exception {
        //edit will be like submit from a saved draft except that the supplier cant be changed (According to Kim Vu)
        //map DTO with validation to general DTO
        List<CRItemDto> itemDtoList = new ArrayList<>();
        for (CreateCRItemDtoWithValidation validateItemDto : editDto.getContractItemList()) {
            CRItemDto mappedItem = new CRItemDto(validateItemDto);
            itemDtoList.add(mappedItem);
        }

        CreateCRDto crDto = new CreateCRDto(editDto, itemDtoList);

        //Need to extract from DB and set the supplier in this crDto
        Optional<ContractRequest> optRecord = contractRequestRepository.findByCompanyUuidAndContractRequestUuid(companyUuid, editDto.getContractRequestUuid());
        if (optRecord.isEmpty()) {
            throw new ResourceNotFoundException(Message.CONTRACT_REQUEST.getValue());
        }
        ContractRequest record = optRecord.get();
        crDto.setSupplierInformation(vendorInformationMapper.supplierInformationDto(record.getSupplierInformation()));

        return createContractRequest(companyUuid, crDto, false);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse createContractRequest(String companyUuid, CreateCRDto createDto, boolean isSaveDraft) throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        String userName = (String) authenticationManager.getUserByKey("name");
        String userUuid = (String) authenticationManager.getUserByKey("sub");
        String designation = (String) authenticationManager.getUserByKey("designation");

        ContractRequest newContractRequest = new ContractRequest(createDto, companyUuid, userUuid, userName);

        if (createDto.getSupplierInformation() != null) {
            newContractRequest.setSupplierInformation(
                    objectService.saveSupplierInformation(
                            vendorInformationMapper.supplierInformation(createDto.getSupplierInformation())));
        }

        // check if cr is draft, set draft status if true, else set parameters
        if (isSaveDraft) {
            newContractRequest.setStatus(ContractRequestStatus.DRAFT_CONTRACT_REQUEST.getValue());
        } else {
            newContractRequest.setContractRequestNumber(utilsService.generateCRNumber(companyUuid));
            newContractRequest.setGlobalCRNumber(utilsService.generateGlobalCRNumber());
            newContractRequest.setStatus(ContractRequestStatus.REQUEST_PENDING_APPROVAL.getValue());
            newContractRequest.setSubmittedDate(Instant.now());
        }

        List<ContractRequestDocument> toSavedDocuments = new ArrayList<>();

        //means if submit from a saved Draft or recalled or send back record, use the record uuid instead and update the documents at the same time
        // check if cr is new or from saved draft, if from saved draft then recycle the CRuuid delete all document in repo for the CRuuid save document from payload, else generate new uuid and set parameters.
        if (createDto.getContractRequestUuid() != null) {
            Optional<ContractRequest> optOldDraft = contractRequestRepository.findByCompanyUuidAndContractRequestUuid(companyUuid, createDto.getContractRequestUuid());
            if (optOldDraft.isEmpty()) {
                throw new ResourceNotFoundException(Message.CONTRACT_REQUEST.getValue());
            }
            ContractRequest oldDraft = optOldDraft.get();

            //check status of repo CR. if repo CR is not draft throw exception
            if (isSaveDraft) {
                if (!oldDraft.getStatus().equals(ContractRequestStatus.DRAFT_CONTRACT_REQUEST.getValue())) {
                    throw new BadRequestException(Message.INVALID_ACTION.getValue());
                }
            } else {
                //means its submit from draft or resubmit from sentback or recall record
                if (!(oldDraft.getStatus().equals(ContractRequestStatus.DRAFT_CONTRACT_REQUEST.getValue())
                        || oldDraft.getStatus().equals(ContractRequestStatus.RECALLED.getValue())
                        || oldDraft.getStatus().equals(ContractRequestStatus.SENT_BACK.getValue()))) {
                    throw new BadRequestException(Message.INVALID_ACTION.getValue());
                }
            }


            //delete all previous contract items
            List<ContractRequestItem> oldItems = contractRequestItemRepository.findByContractRequestUuid(oldDraft.getContractRequestUuid());
            contractRequestItemRepository.deleteInBatch(oldItems);

            //update documents for old draft
            toSavedDocuments = updateAndRemoveDocuments(oldDraft, createDto.getDocumentList(), userName, userUuid);

            newContractRequest.setContractRequestUuid(createDto.getContractRequestUuid());
            newContractRequest.setId(oldDraft.getId());
            newContractRequest.setCreatedDate(oldDraft.getCreatedDate());
        } else {
            newContractRequest.setContractRequestUuid(utilsService.generateUUID());
            newContractRequest.setCreatedDate(Instant.now());
            newContractRequest.setCreatedByName(userName);
            newContractRequest.setCreatedByUuid(userUuid);
        }

        List<ContractRequestItem> toSavedItemList = new ArrayList<>();
        BigDecimal totalAmount = new BigDecimal("0");
        //Contract value is totalAmount without tax as per Queenie
        BigDecimal contractValue = new BigDecimal("0");
        for (CRItemDto itemDto : createDto.getContractItemList()) {
            //set the respective values, total amount and contract value
            ContractRequestItem newCRItem = new ContractRequestItem(itemDto);
            BigDecimal inSourceCurrencyBeforeTax = itemDto.getItemQuantity().multiply(itemDto.getItemUnitPrice());
            BigDecimal inDocumentCurrencyBeforeTax = inSourceCurrencyBeforeTax.multiply(itemDto.getExchangeRate());
            BigDecimal inDocumentCurrencyTaxAmount = inDocumentCurrencyBeforeTax.multiply(itemDto.getTaxPercentage().divide(new BigDecimal("100")));
            BigDecimal inDocumentCurrencyAfterTax = inDocumentCurrencyBeforeTax.add(inDocumentCurrencyTaxAmount);
            newCRItem.setInSourceCurrencyBeforeTax(inSourceCurrencyBeforeTax);
            newCRItem.setInDocumentCurrencyBeforeTax(inDocumentCurrencyBeforeTax);
            newCRItem.setInDocumentCurrencyTaxAmount(inDocumentCurrencyTaxAmount);
            newCRItem.setInDocumentCurrencyAfterTax(inDocumentCurrencyAfterTax);
            toSavedItemList.add(newCRItem);

            totalAmount = totalAmount.add(inDocumentCurrencyAfterTax);
            contractValue = contractValue.add(inDocumentCurrencyBeforeTax);
        }
        newContractRequest.setTotalAmount(totalAmount);
        newContractRequest.setContractValue(contractValue);

        //			split the approval sequence to get the first approver group
        ApprovalMatrixDetailsAPIDto approvalMatrixResponse = entitiesService
                .getApprovalMatrixDetails(createDto.getApprovalRouteUuid(),
                        totalAmount, createDto.getCurrencyCode(), companyUuid);
        String approvalRouteSequence = approvalMatrixResponse.getApprovalGroupSequence();

        String approvalRouteName = approvalMatrixResponse.getApprovalMatrixName();


        newContractRequest.setApprovalRouteName(approvalRouteName);
        newContractRequest.setApprovalRouteSequence(approvalRouteSequence);
        //set the approval matrix name, uuid, sequence and etc. Dun need to save approval sequence for save draft since no approvals can be done yet
        if (!isSaveDraft) {
            // Set first approval
            ApprovalGroupDetail firstApprovalGroup = approvalMatrixResponse.getFirstApproval();
            newContractRequest.setNextApprover(firstApprovalGroup.getApprovalNameAndCount());
            newContractRequest.setNextApprovalGroup(firstApprovalGroup.getUuid());
            newContractRequest.setApprovalRouteUuid(approvalMatrixResponse.getUuid());
        }
        if (createDto.getDeliveryAddress() != null) {
            newContractRequest.setDeliveryAddress(objectService.saveAddress(addressMapper.address(createDto.getDeliveryAddress())));
        }
        //set fields for project = true
        if (createDto.isProject()) {
            //from the screen, looks like the compulsory fields are name and uuid
            if (createDto.getProjectName() == null || createDto.getProjectUuid() == null) {
                throw new BadRequestException(Message.MISSING_NECESSARY_FIELDS.getValue());
            }

            ProjectDetailsApiDto project = null;
            project = entityServiceValidator.validateProject(companyUuid, createDto.getProjectUuid());

            if (project != null) {
                newContractRequest.setProjectName(project.getProjectTitle());
                newContractRequest.setProjectUuid(project.getUuid());
                newContractRequest.setProjectCode(project.getProjectCode());
            }

//    		newContractRequest.setProjectDeliveryDate(createDto.getProjectDeliveryDate());
            newContractRequest.setTotalUsedCurrencyCode(createDto.getTotalUsedCurrencyCode());
            newContractRequest.setTotalUsed(createDto.getTotalUsed());
        }
        ContractRequest savedContractRequest = contractRequestRepository.save(newContractRequest);

        //set the contract request to contract items
        for (ContractRequestItem crItem : toSavedItemList) {
            crItem.setContractRequest(savedContractRequest);
        }

        //save documents for newly create
        if (createDto.getContractRequestUuid() == null) {
            for (CRDocumentDto documentDto : createDto.getDocumentList()) {
                ContractRequestDocument newDocument = new ContractRequestDocument(documentDto.getGuid(), documentDto.getFileLabel(), documentDto.getFileDescription(), userName, userUuid, savedContractRequest, documentDto.isExternalDocument());
                toSavedDocuments.add(newDocument);
            }
        }

        ContractRequestAuditTrail newAuditTrail;
        if (isSaveDraft) {
            newAuditTrail = new ContractRequestAuditTrail(userName, userUuid, designation, Message.DRAFT_SAVED.getValue(), savedContractRequest);
            apiResponse.setMessage(Message.DRAFT_SAVED_SUCCESSFULLY.getValue() + ": " + savedContractRequest.getContractRequestNumber());
        } else {
            newAuditTrail = new ContractRequestAuditTrail(userName, userUuid, designation, Message.SUBMIT.getValue(), savedContractRequest);
            apiResponse.setMessage(Message.CREATED_SUCCESSFULLY.getValue() + ": " + savedContractRequest.getContractRequestNumber());
        }

        contractRequestAuditTrailRepository.save(newAuditTrail);
        contractRequestDocumentRepository.saveAll(toSavedDocuments);
        contractRequestItemRepository.saveAll(toSavedItemList);
        SubmitResponseCRDto dataResponse = new SubmitResponseCRDto();
        if (savedContractRequest.getContractRequestNumber() != null) {
            dataResponse.setContractRequestNumber(savedContractRequest.getContractRequestNumber());
        }
        if (savedContractRequest.getContractRequestUuid() != null) {
            dataResponse.setContractRequestUuid(savedContractRequest.getContractRequestUuid());
        }
        if (savedContractRequest.getGlobalCRNumber() != null) {
            dataResponse.setGlobalCRNumber((savedContractRequest.getGlobalCRNumber()));
        }
        apiResponse.setData(dataResponse);
        apiResponse.setStatus(HttpStatus.OK);
        return apiResponse;
    }

    public List<ContractRequestDocument> updateAndRemoveDocuments(ContractRequest contractRequest, List<CRDocumentDto> documents, String userName, String userUuid) {
        List<ContractRequestDocument> toSavedDocuments = new ArrayList<>();
        List<ContractRequestDocument> dbList = contractRequestDocumentRepository.findByContractRequestUuid(contractRequest.getContractRequestUuid());
        HashMap<String, ContractRequestDocument> documentsFromDb = new HashMap<>();
        for (ContractRequestDocument d : dbList) {
            documentsFromDb.put(d.getGuid(), d);
        }

        List<ContractRequestDocument> dtoList = new ArrayList<>();
        //documents
        for (CRDocumentDto documentDto : documents) {
            ContractRequestDocument newDocument = new ContractRequestDocument(documentDto.getGuid(), documentDto.getFileLabel(), documentDto.getFileDescription(), userName, userUuid, contractRequest, documentDto.isExternalDocument());
            dtoList.add(newDocument);
        }

        //new documents to be created
        for (ContractRequestDocument d : dtoList) {
            if (documentsFromDb.containsKey(d.getGuid())) {
                // update a new document
                ContractRequestDocument document = documentsFromDb.get(d.getGuid());
                document.setFileLabel(d.getFileLabel());
                document.setFileDescription(d.getFileDescription());
                toSavedDocuments.add(document);
            } else {
                // create file
                toSavedDocuments.add(d);
            }
        }

        HashMap<String, ContractRequestDocument> documentsFromDto = new HashMap<>();
        for (ContractRequestDocument d : dtoList) {
            documentsFromDto.put(d.getGuid(), d);
        }

        HashMap<String, ContractRequestDocument> copyDocumentsFromDb = new HashMap<>(documentsFromDb);
        // FILES TO BE REMOVED
        copyDocumentsFromDb.keySet().removeAll(documentsFromDto.keySet());
        if (!copyDocumentsFromDb.isEmpty()) {
            for (ContractRequestDocument d : dbList) {
                if (copyDocumentsFromDb.containsKey(d.getGuid())) {
                    //set to null instead of delete to represent deleted
                    d.setContractRequest(null);
                    toSavedDocuments.add(d);
                }
            }
        }

        return toSavedDocuments;
    }

    @Override
    public ApiResponse recallCancelContractRequest(String companyUuid, String contractRequestUuid, boolean isRecall) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Optional<ContractRequest> optContractRequest = contractRequestRepository.findByCompanyUuidAndContractRequestUuid(companyUuid, contractRequestUuid);
            if (optContractRequest.isEmpty()) {
                throw new BadRequestException(Message.CONTRACT_REQUEST_NOT_FOUND.getValue());
            }
            ContractRequest contractRequest = optContractRequest.get();

            String userName = (String) authenticationManager.getUserByKey("name");
            String userUuid = (String) authenticationManager.getUserByKey("sub");
            String designation = (String) authenticationManager.getUserByKey("designation");

            if (!userUuid.equals(contractRequest.getCreatedByUuid())) {
                if (isRecall) {
                    throw new AccessDeniedException(Message.CONTRACT_REQUEST_RECALL_ACCESS_DENIED.getValue());
                } else {
                    throw new AccessDeniedException(Message.CONTRACT_REQUEST_CANCEL_ACCESS_DENIED.getValue());
                }
            }

            //Only can be recall before first approver approves
            //Only can be cancel if in approval stage (any approval stages)
            ContractRequestAuditTrail newAuditTrail;
            if (isRecall) {
                String lastAuditAction = contractRequestAuditTrailRepository.findLatestCRAuditByCRId(contractRequest.getId());
                if (!lastAuditAction.equals(Message.SUBMIT.getValue())) {
                    throw new BadRequestException(Message.RECALL_INVALID.getValue());
                }

                contractRequest.setStatus(ContractRequestStatus.RECALLED.getValue());
                newAuditTrail = new ContractRequestAuditTrail(userName, userUuid, designation, Message.RECALL.getValue(), contractRequest);
                apiResponse.setMessage(Message.RECALL_SUCCESSFULLY.getValue());
            } else {
                if (!contractRequest.getStatus().equals(ContractRequestStatus.REQUEST_PENDING_APPROVAL.getValue())) {
                    throw new BadRequestException(Message.CANCEL_INVALID.getValue());
                }

                contractRequest.setStatus(ContractRequestStatus.CANCELLED.getValue());
                newAuditTrail = new ContractRequestAuditTrail(userName, userUuid, designation, Message.CANCEL.getValue(), contractRequest);
                apiResponse.setMessage(Message.CANCEL_SUCCESSFULLY.getValue());
            }

            contractRequest.setNextApprover(null);

            contractRequestRepository.save(contractRequest);
            contractRequestAuditTrailRepository.save(newAuditTrail);

            apiResponse.setStatus(HttpStatus.OK);
            return apiResponse;
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.BAD_REQUEST);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
            return apiResponse;
        }
    }

    @Override
    public ApiResponse approverAction(String companyUuid, String contractRequestUuid, List<CRDocumentDto> newlyAddedDocuments, String action) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            //find contract based on contractRequestUuid
            Optional<ContractRequest> optContractRequest = contractRequestRepository.findByCompanyUuidAndContractRequestUuid(companyUuid, contractRequestUuid);
            //if not exists throw exception
            if (optContractRequest.isEmpty()) {
                throw new BadRequestException(Message.CONTRACT_REQUEST_NOT_FOUND.getValue());
            }
            //get the actual contractRequest from optional contractRequest
            ContractRequest contractRequest = optContractRequest.get();

            String userName = (String) authenticationManager.getUserByKey("name");
            //get the uuid of user for this approval action
            String userUuid = (String) authenticationManager.getUserByKey("sub");
            String designation = (String) authenticationManager.getUserByKey("designation");

            //status of record needs to be in pending approval
            if (!contractRequest.getStatus().equals(ContractRequestStatus.REQUEST_PENDING_APPROVAL.getValue())) {
                throw new BadRequestException(Message.INVALID_ACTION.getValue());
            }

            //Check if the user has approved before && get the list of pastAuditTrails to send to countNumberOfApproval
            List<ContractRequestAuditTrail> pastAuditTrails = contractRequestAuditTrailRepository.findByContractRequestUuid(contractRequestUuid);
            int index = 0;

            //create new hashmap to send to function countNumberOfApproval
            HashMap<String, ContractRequestAuditTrail> auditTrail = new HashMap<>();
            // count the number of approvals already done.
            int countApprovers = countNumberOfApproval(pastAuditTrails, auditTrail);
//			check has user already approved in current group or not
            if (auditTrail.containsKey(userUuid)
                    && auditTrail.get(userUuid).getApprovalGroupUuid().equals(contractRequest.getNextApprovalGroup())) {
                throw new BadRequestException(Message.CANNOT_APPROVE_REJECT_SENDBACK_APPROVED.getValue());
            }

            ApprovalMatrixDetailsAPIDto approvalMatrixDetailsAPIDto =
                    entitiesService.getApprovalMatrixDetails(
                            contractRequest.getApprovalRouteUuid(),
                            contractRequest.getTotalAmount(),
                            contractRequest.getCurrencyCode(),
                            contractRequest.getCompanyUuid()
                    );
            log.info("Approval matrix {}: " + approvalMatrixDetailsAPIDto.toString());
            ApprovalGroupDetail currentApprovalGroup = approvalMatrixDetailsAPIDto.getCurrentApprovalGroup(contractRequest.getNextApprovalGroup());
            log.info("currentApprovalGroup {}: " + currentApprovalGroup.toString());

            //will never exceed the list size as the first auditTrail is always the submit
            while (pastAuditTrails.get(index).getAction().equals(ContractRequestStatus.APPROVED.getValue()) && pastAuditTrails.get(index).getApprovalGroupUuid().equals(currentApprovalGroup.getUuid())) {
                if (pastAuditTrails.get(index).getUserUuid().equals(userUuid)) {
                    throw new BadRequestException(Message.USER_HAS_APPROVED.getValue());
                }
                index += 1;
            }

            //Check if the user is the right approver for current group through calling entities microservice
            //if false, then throw no permission
            if (!entitiesService.checkUserGroupValidity(userUuid, companyUuid, currentApprovalGroup.getUuid())) {
                throw new BadRequestException(Message.NO_PERMISSION.getValue());
            }

            if (action.equals(ContractRequestStatus.APPROVE.getValue())) {
                //add the approve to audit trail
                contractRequest = handleApproveAction(approvalMatrixDetailsAPIDto, contractRequest, countApprovers);
                contractRequestRepository.save(contractRequest);
                ContractRequestAuditTrail newAuditTrail = new ContractRequestAuditTrail(userName, userUuid, designation, ContractRequestStatus.APPROVED.getValue(), contractRequest);
                newAuditTrail.setApprovalGroupUuid(currentApprovalGroup.getUuid());
                newAuditTrail.setApprovalGroup(currentApprovalGroup.getName());
                ContractRequestAuditTrail savedAuditTrail = contractRequestAuditTrailRepository.save(newAuditTrail);

                apiResponse.setMessage(Message.APPROVE_SUCCESSFULLY.getValue());

            } else if (action.equals(ContractRequestStatus.SENT_BACK.getValue())) {
                //update the contractRequest status and next approver group
                contractRequest.sendBack();
                contractRequestRepository.save(contractRequest);

                //add the sentback audit trail
                ContractRequestAuditTrail newAuditTrail = new ContractRequestAuditTrail(userName, userUuid, designation, ContractRequestStatus.SENT_BACK.getValue(), contractRequest);
                contractRequestAuditTrailRepository.save(newAuditTrail);

                apiResponse.setMessage(Message.SENTBACK_SUCCESSFULLY.getValue());

            } else {
                //the last action possible will be rejected
                //update the contractRequest status and next approver group
                contractRequest.reject();
                contractRequestRepository.save(contractRequest);

                //add the rejected audit trail
                ContractRequestAuditTrail newAuditTrail = new ContractRequestAuditTrail(userName, userUuid, designation, ContractRequestStatus.REJECTED.getValue(), contractRequest);
                contractRequestAuditTrailRepository.save(newAuditTrail);

                apiResponse.setMessage(Message.REJECTED_SUCCESSFULLY.getValue());
            }

            //Check for new documents uploaded
            if (newlyAddedDocuments != null && !newlyAddedDocuments.isEmpty()) {
                List<ContractRequestDocument> toSavedDocuments = getToSaveDocuments(contractRequest, userName, userUuid, newlyAddedDocuments);

                contractRequestDocumentRepository.saveAll(toSavedDocuments);
            }
            apiResponse.setStatus(HttpStatus.OK);
            return apiResponse;
        } catch (Exception e) {
            apiResponse.setStatus(HttpStatus.BAD_REQUEST);
            apiResponse.setMessage(e.getMessage());
            LOG.error(e.getMessage());
            return apiResponse;
        }
    }

    public List<ContractRequestDocument> getToSaveDocuments(ContractRequest contractRequest, String userName, String userUuid, List<CRDocumentDto> newlyAddedDocuments) {
        //Check for new documents uploaded
        List<ContractRequestDocument> toSavedDocuments = new ArrayList<>();
        List<ContractRequestDocument> dbList = contractRequestDocumentRepository.findByContractRequestUuid(contractRequest.getContractRequestUuid());
        HashMap<String, ContractRequestDocument> documentsFromDb = new HashMap<>();
        for (ContractRequestDocument d : dbList) {
            documentsFromDb.put(d.getGuid(), d);
        }

        List<ContractRequestDocument> dtoList = new ArrayList<>();
        for (CRDocumentDto documentDto : newlyAddedDocuments) {
            ContractRequestDocument newDocument = new ContractRequestDocument(documentDto.getGuid(), documentDto.getFileLabel(), documentDto.getFileDescription(), userName, userUuid, contractRequest, documentDto.isExternalDocument());
            dtoList.add(newDocument);
        }
        //new documents to be created
        for (ContractRequestDocument d : dtoList) {
            if (!documentsFromDb.containsKey(d.getGuid())) {
                toSavedDocuments.add(d);
            }
        }
        return toSavedDocuments;
    }

    //get the number of approval already done.
    private int countNumberOfApproval(List<ContractRequestAuditTrail> contractReqAuditTrail, HashMap<String, ContractRequestAuditTrail> auditTrail) {
        int counter = 0;
        if (contractReqAuditTrail != null) {
            for (ContractRequestAuditTrail p : contractReqAuditTrail) {
                if (!p.getAction().equals(ContractRequestStatus.APPROVED.getValue())) {
                    break;
                } else {
                    counter++;
                    auditTrail.put(p.getUserUuid(), p);
                }
            }
        }
        return counter;
    }

    private ContractRequest handleApproveAction(ApprovalMatrixDetailsAPIDto approvalMatrixDetailsAPIDto, ContractRequest contractRequest, int countApprovers) throws AccessDeniedException {
        String requesterUuid = (String) authenticationManager.getUserByKey("sub");
        ApprovalGroupDetail currentApprovalGroup = approvalMatrixDetailsAPIDto.getCurrentApprovalGroup(contractRequest.getNextApprovalGroup());
        if (!currentApprovalGroup.isUserInGroup(requesterUuid) || requesterUuid.equals(contractRequest.getCreatedByUuid())) {
            throw new AccessDeniedException(Message.ACCESS_DENIED.getValue());
        }
        ApprovalGroupDetail nextApprovalGroup = approvalMatrixDetailsAPIDto.getNextApprovalUuid(currentApprovalGroup.getUuid());
        // If all users in current group has approved PR, move to next approval group
        if ((countApprovers + 1) >= currentApprovalGroup.getTotalApproves()) {
            if (nextApprovalGroup != null) {
                contractRequest.setNextApprover(nextApprovalGroup.getApprovalNameAndCount());
                contractRequest.setNextApprovalGroup(nextApprovalGroup.getUuid());
                contractRequest.setApprovedDate(Instant.now());
                contractRequest.setStatus(ContractRequestStatus.REQUEST_PENDING_APPROVAL.getValue());
            } else {
                contractRequest.setNextApprover(null);
                contractRequest.setApprovedDate(Instant.now());
                contractRequest.setStatus(ContractRequestStatus.CONTRACT_PENDING_CONVERSION.getValue());
            }
        }
        return contractRequest;
    }
}
