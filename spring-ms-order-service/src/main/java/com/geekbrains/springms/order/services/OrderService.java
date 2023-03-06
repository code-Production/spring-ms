package com.geekbrains.springms.order.services;

import com.geekbrains.springms.api.AddressDto;
import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.CartItemDto;
import com.geekbrains.springms.api.ProductDto;
import com.geekbrains.springms.order.entities.Order;
import com.geekbrains.springms.order.entities.OrderItem;
import com.geekbrains.springms.order.integrations.AddressServiceIntegration;
import com.geekbrains.springms.order.integrations.ProductServiceIntegration;
import com.geekbrains.springms.order.integrations.UserServiceIntegration;
import com.geekbrains.springms.order.repositories.OrderItemRepository;
import com.geekbrains.springms.order.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private UserServiceIntegration userServiceIntegration;

    private AddressServiceIntegration addressServiceIntegration;

    private ProductServiceIntegration productServiceIntegration;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    @Autowired
    public void setAddressServiceIntegration(AddressServiceIntegration addressServiceIntegration) {
        this.addressServiceIntegration = addressServiceIntegration;
    }

    @Autowired
    public void setProductServiceIntegration(ProductServiceIntegration productServiceIntegration) {
        this.productServiceIntegration = productServiceIntegration;
    }

    @Transactional
    public Optional<Order> createOrder(CartDto cartDto, String username) {
        if (!userServiceIntegration.checkIfBillingBelongsToUser(cartDto.getUsername(), cartDto.getBillingId(), username)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Billing id '%s' doesn't belong to user '%s'", cartDto.getBillingId(), cartDto.getUsername())
            );
        }
        AddressDto addressById = addressServiceIntegration.getAddressById(cartDto.getAddressId(), username);
        if (addressById.getUsername() != null && !addressById.getUsername().equals(cartDto.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Address with id '%s' doesn't belong to user '%s'.", addressById.getId(), cartDto.getUsername())
            );
        }

        if (cartDto.getItems() != null && !cartDto.getItems().isEmpty()) {

            for (CartItemDto item : cartDto.getItems()) {
                ProductDto productById = productServiceIntegration.findProductById(item.getProductDto().getId());
                if (!item.getProductDto().getTitle().equals(productById.getTitle())) {
                    String errMsg = String.format("Product with id '%s' has outdated title '%s'.",
                            item.getProductDto().getId(),
                            item.getProductDto().getTitle());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
                }
                if (item.getProductDto().getPrice().compareTo(productById.getPrice()) != 0) {
                    String errMsg = String.format("Product with id '%s' has outdated price '%s'",
                            item.getProductDto().getId(),
                            item.getProductDto().getPrice());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
                }
            }
            Order order = new Order();
            order.setUsername(cartDto.getUsername());
            order.setAddressId(cartDto.getAddressId());
            order.setBillingId(cartDto.getBillingId());
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

    public List<Order> getUserOrders(String username) {
        return orderRepository.findAllByUsername(username);
    }
}
