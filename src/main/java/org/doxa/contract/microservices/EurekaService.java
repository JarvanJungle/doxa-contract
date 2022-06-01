package org.doxa.contract.microservices;

import org.doxa.contract.microservices.interfaces.IEurekaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(value = "eureka.client.enabled", havingValue = "true")
public class EurekaService implements IEurekaService {
    @Autowired
    private DiscoveryClient eurekaClient;

    @Override
    public String getService(String serviceName) {
//        Application application = this.eurekaClient.getApplication(serviceName);
//        if (application == null) {
//            return null;
//        }
//        InstanceInfo instanceInfo = application.getInstances().get(0);
//        return "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort();
        List<ServiceInstance> list = this.eurekaClient.getInstances("STORES");
        if (list != null && list.size() > 0 ) {
            return String.valueOf(list.get(0).getUri());
        }
        return null;
    }
}
