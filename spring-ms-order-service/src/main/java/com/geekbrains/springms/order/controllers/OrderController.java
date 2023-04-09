package com.geekbrains.springms.order.controllers;

import com.geekbrains.springms.api.AddressDto;
import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.order.entities.Order;
import com.geekbrains.springms.order.integrations.AddressServiceIntegration;
import com.geekbrains.springms.order.integrations.UserServiceIntegration;
import com.geekbrains.springms.order.mappers.OrderItemMapper;
import com.geekbrains.springms.order.mappers.OrderMapper;
import com.geekbrains.springms.order.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrderController {

    private OrderService orderService;

    private OrderMapper orderMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public OrderDto createOrder(@RequestBody CartDto cartDto, HttpServletRequest request) {
        String authorizedUsername = checkAuthorizationHeaderOrThrowException(request);
        if (!authorizedUsername.equals(cartDto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request");
        }
        Order order = orderService.createOrder(cartDto, authorizedUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return orderMapper.toDto(order);
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id, HttpServletRequest request) {
        Order order = orderService.getOrderById(id);
        String authorizedUsername = checkAuthorizationHeaderOrThrowException(request);
        if (!order.getUsername().equals(authorizedUsername)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request");
        }
        return orderMapper.toDto(order);
    }

    @GetMapping("/all")
    public List<OrderDto> getUserOrders(HttpServletRequest request) {
        String username = checkAuthorizationHeaderOrThrowException(request);
        return orderService.getUserOrders(username).stream().map(orderMapper::toDto).toList();
    }


    private String checkAuthorizationHeaderOrThrowException(HttpServletRequest request) {
        String username = request.getHeader("username");
        if (username != null && !username.isBlank()) {
            return username;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access.");
    }

}
