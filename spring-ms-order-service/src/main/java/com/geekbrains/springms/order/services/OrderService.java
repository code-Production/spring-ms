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

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private OrderItemRepository orderItemRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setOrderItemRepository(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Optional<Long> createOrder(CartDto cartDto, String username) {
        //TODO check username
        if (!cartDto.getItems().isEmpty()) {
            Order order = new Order(
                    null,
                    username,
                    null,
                    null,
                    cartDto.getTotalPrice(),
                    null
            );
            order = orderRepository.save(order);
            for (CartItemDto cartItem : cartDto.getItems()) {
                OrderItem orderItem = new OrderItem(
                        null,
                        order,
                        cartItem.getProductDto().getId(),
                        cartItem.getProductDto().getPrice(),
                        cartItem.getAmount(),
                        cartItem.getSum()
                );
                orderItemRepository.save(orderItem);
            }
            return Optional.of(order.getId());
        }
        return Optional.empty();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}
