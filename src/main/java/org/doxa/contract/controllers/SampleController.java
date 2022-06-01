package org.doxa.contract.controllers;

import org.doxa.contract.exceptions.AccessDeniedException;
import org.doxa.contract.responses.ApiResponse;
import org.doxa.contract.authorization.SampleAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class SampleController {

    @Autowired
    private SampleAuthorization sampleAuthorization;

    @GetMapping
    private ResponseEntity<ApiResponse> sayHello() throws AccessDeniedException {
        sampleAuthorization.printHello("");
        return ResponseEntity.ok().body(new ApiResponse());
    }

}
