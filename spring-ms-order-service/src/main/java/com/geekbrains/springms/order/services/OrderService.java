package com.geekbrains.springms.order.services;

import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.CartItemDto;
import com.geekbrains.springms.order.entities.Order;
import com.geekbrains.springms.order.entities.OrderItem;
import com.geekbrains.springms.order.repositories.OrderItemRepository;
import com.geekbrains.springms.order.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Optional<Order> createOrder(CartDto cartDto, String username) {
        //TODO check username
        if (!cartDto.getItems().isEmpty()) {
            Order order = new Order();
            order.setUsername(username);
            order.setAddressId(null);
            order.setOrderTotal(cartDto.getTotalPrice());
            order.setOrderItems(cartDto.getItems().stream()
                    .map(cartItemDto -> new OrderItem(
                                null,
                                order,
                                cartItemDto.getProductDto().getId(),
                                cartItemDto.getProductDto().getPrice(),
                                cartItemDto.getAmount(),
                                cartItemDto.getSum()
                    ))
                    .toList());
            orderRepository.save(order);
            return Optional.of(order);
        }
        return Optional.empty();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}
