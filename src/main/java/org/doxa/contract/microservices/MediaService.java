package org.doxa.contract.microservices;

import org.doxa.contract.config.DoxaServiceNameConsts;
import org.doxa.contract.microservices.interfaces.IDoxaMicroservices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * This service is used to communicate with other microservices
 * For local development purpose, please use ribbon and eureka client
 * For Kubernetes deployment (dev, uat, PROD): please specify the service name, ex: media-service, authorization-service
 */

@Service
public class MediaService implements IDoxaMicroservices {

    private final EurekaService eurekaService;

    public MediaService() {
        eurekaService = new EurekaService();
    }

    @Autowired
    private Environment environment;

    @Override
    public String getServiceUrl() {
        if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            return eurekaService.getService(DoxaServiceNameConsts.MEDIA_SERVICE.getValue());
        }
        return "http://" + DoxaServiceNameConsts.MEDIA_SERVICE.getValue();
    }

}
