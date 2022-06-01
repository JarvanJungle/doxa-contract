package org.doxa.contract.cache;

import lombok.extern.slf4j.Slf4j;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.serviceImpl.Sample;
import org.doxa.contract.services.ISample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SampleCache implements ISample {

    @Autowired
    private Sample sample;
    @Override
    public ApiResponse printHello(String companyUuid) {
        return sample.printHello(companyUuid);
    }
}
