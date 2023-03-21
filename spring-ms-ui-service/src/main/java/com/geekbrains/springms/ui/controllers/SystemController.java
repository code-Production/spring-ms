package com.geekbrains.springms.ui.controllers;

import com.geekbrains.springms.api.ResourceAddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

//@CrossOrigin("*")
@RestController
public class SystemController {



    private DiscoveryClient discoveryClient;

    @Autowired
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/gateway-address")
    public ResourceAddressResponse getGatewayAddress() {
        List<ServiceInstance> gatewayServices = discoveryClient.getInstances("gateway-service");
        if (gatewayServices != null && gatewayServices.size() > 0) {
            ServiceInstance gatewayInstance = gatewayServices.get(0);
            String host = gatewayInstance.getHost();
            int port = gatewayInstance.getPort();
            String url = gatewayInstance.getUri().toString();
            //to fix cors error
            if (url.contains(host + ":" + port)) {
                url = url.replace(host + ":" + port, "localhost" + ":" + port);
            }
            return new ResourceAddressResponse(url);
        } else {
            return new ResourceAddressResponse("");
        }
    }

//    @GetMapping("/callback")
//    public void callback(
//            @RequestParam String session_state,
//            @RequestParam String code
//    )
//    {
//        System.out.println("Code:" + code);
//        System.out.println("Session_state:" + session_state);
//    }
}
