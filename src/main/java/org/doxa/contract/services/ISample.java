package org.doxa.contract.services;

import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.exceptions.AccessDeniedException;

public interface ISample {
    public ApiResponse printHello(String companyUuid) throws AccessDeniedException;
}
