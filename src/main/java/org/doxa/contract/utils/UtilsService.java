package org.doxa.contract.utils;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.doxa.contract.microservices.DTO.entityService.IEntitiesService;
import org.doxa.contract.repositories.contract.ContractRepository;
import org.doxa.contract.repositories.contractRequest.ContractRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilsService {

    private static final String PREFIX_FUNCTION_CONTRACT = "contract";
    public static final String PREFIX_TYPE_MANUAL = "Manual";

    @Autowired
    private ContractRequestRepository contractRequestRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private IEntitiesService entitiesService;

    public String generateGlobalCRNumber(){
        return ("CR"+String.format("%08d" , contractRequestRepository.getNextValMySequence()));
    }
	
	public  String generateCRNumber(String companyUuid){
	    int crNumber = contractRequestRepository.totalRow(companyUuid)+1;
	    return ("CR-"+String.format("%08d" , crNumber));
    }

    public String generateGlobalCNumber(){
        return ("CT"+String.format("%08d" , contractRepository.getNextValMySequence()));
    }

    public String generateCNumber(String companyUuid, String projectCode, String supplierCode) {
        String nextNumber = entitiesService.getLatestFunctionNumber(companyUuid, projectCode, supplierCode, PREFIX_FUNCTION_CONTRACT);
        if (StringUtils.isEmpty(nextNumber)) {
            int contractNumber = contractRepository.totalRow(companyUuid) + 1;
            return "CT" + String.format("%08d", contractNumber);
        }
        return nextNumber;
    }

    public  String generateUUID(){
        return (UUID.randomUUID().toString());
    }


}
