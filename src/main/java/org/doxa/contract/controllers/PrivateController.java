package org.doxa.contract.controllers;

import org.doxa.contract.config.ControllerPath;
import org.doxa.contract.microservices.DTO.purchase.RfqDto;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.serviceImpl.contract.ContractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ControllerPath.PRIVATE)
public class PrivateController {

    private static final Logger LOG = LoggerFactory.getLogger(PrivateController.class);

    @Autowired
    private ContractServiceImpl contractService;

    @PostMapping(ControllerPath.PRIVATE_CONVERT_RFQ_TO_CONTRACT)
    public ResponseEntity<ApiResponse> convertRfqToContract(@RequestParam String companyUuid,
                                                            @RequestParam String supplierUuid,
                                                            @RequestBody RfqDto rfqDto) {
        ApiResponse apiResponse = contractService.convertRfqToContract(companyUuid, supplierUuid, rfqDto);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

}
