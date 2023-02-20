package com.geekbrains.springms.cart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DiscoveryService {

    private DiscoveryClient discoveryClient;

    @Autowired
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public Optional<String> getServiceUrlByName(String name) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(name);
        if (serviceInstances != null && serviceInstances.size() > 0) {
            ServiceInstance serviceInstance = serviceInstances.get(0);
            String host = serviceInstance.getHost();
            int port = serviceInstance.getPort();
            String url = serviceInstance.getUri().toString();
            //to fix cors error
            if (url.contains(host + ":" + port)) {
                url = url.replace(host + ":" + port, "localhost" + ":" + port);
            }
            return Optional.of(url);
        }
        return Optional.empty();
    }
}
