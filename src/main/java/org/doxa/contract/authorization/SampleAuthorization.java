package org.doxa.contract.authorization;

import org.doxa.contract.exceptions.AccessDeniedException;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.services.ISample;
import org.doxa.contract.cache.SampleCache;
import org.doxa.auth.DoxaAuthenticationManager;
import org.doxa.contract.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleAuthorization implements ISample {
    // Resource name indicate what the api is serving,
    // for example: po, do, invoice
    private static final String RESOURCE_NAME = "sample";
    @Autowired
    private DoxaAuthenticationManager authenticationManager;

    @Autowired
    private SampleCache sample;

    @Override
    public ApiResponse printHello(String companyUuid) throws AccessDeniedException {
        if (authenticationManager.isDoxaAdmin() || authenticationManager.hasAuthority(companyUuid, RESOURCE_NAME)) {
            return sample.printHello(companyUuid);
        }
        throw new AccessDeniedException(Message.ACCESS_DENIED.getValue() + RESOURCE_NAME);
    }
}
