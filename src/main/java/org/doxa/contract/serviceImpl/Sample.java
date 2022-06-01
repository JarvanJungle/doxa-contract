package org.doxa.contract.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.services.ISample;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Sample implements ISample {
    @Override
    public ApiResponse printHello(String companyUuid) {
        log.info("Sample service say: Hello");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("OK");
        return apiResponse;
    }
}
