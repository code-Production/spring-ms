package com.geekbrains.springms.order.controllers;

import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.order.entities.Order;
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

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    //TODO: add some address param
    @PostMapping("/")
    public Long createOrder(@RequestBody CartDto cartDto, HttpServletRequest request) {
//        String username = request.getHeader("username");
        String username = "BOB";
        return orderService.createOrder(cartDto, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return OrderMapper.MAPPER.toDto(order);
    }

}
