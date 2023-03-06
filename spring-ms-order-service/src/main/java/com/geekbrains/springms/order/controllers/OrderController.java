package com.geekbrains.springms.order.controllers;

import com.geekbrains.springms.api.AddressDto;
import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.order.entities.Order;
import com.geekbrains.springms.order.integrations.AddressServiceIntegration;
import com.geekbrains.springms.order.integrations.UserServiceIntegration;
import com.geekbrains.springms.order.mappers.OrderMapper;
import com.geekbrains.springms.order.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class OrderController {

    private OrderService orderService;

    private UserServiceIntegration userServiceIntegration;

    private AddressServiceIntegration addressServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    @Autowired
    public void setAddressServiceIntegration(AddressServiceIntegration addressServiceIntegration) {
        this.addressServiceIntegration = addressServiceIntegration;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public OrderDto createOrder(@RequestBody CartDto cartDto, HttpServletRequest request) {
        Order order = orderService.createOrder(cartDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return OrderMapper.MAPPER.toDto(order);
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return OrderMapper.MAPPER.toDto(order);
    }

//    @GetMapping("/inter")
//    public Boolean check(@RequestParam String username,
//                      @RequestParam(name = "billing_id") Long billingId
//    )
//    {
//        return userServiceIntegration.checkIfBillingBelongsToUser(username, billingId);
//    }
//
//    @GetMapping("/inter2")
//    public AddressDto getUserAddressById(@RequestParam Long id) {
//        return addressServiceIntegration.getAddressById(id);
//    }

}
