package com.geekbrains.springms.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.CartItemDto;
import com.geekbrains.springms.api.ProductDto;
import com.geekbrains.springms.order.entities.Order;
import com.geekbrains.springms.order.entities.OrderItem;
import com.geekbrains.springms.order.repositories.OrderRepository;
import com.geekbrains.springms.order.services.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    // TODO: 25.02.2023 correct tests

    @Test
    public void createOrderFromCartTest() throws JsonProcessingException {
        ProductDto productDto1 = new ProductDto(11L, "TestTitle1", BigDecimal.valueOf(100));
        ProductDto productDto2 = new ProductDto(12L, "TestTitle2", BigDecimal.valueOf(50));

        int productAmount1 = 10;
        int productAmount2 = 1;

        BigDecimal productSum1 = productDto1.getPrice().multiply(BigDecimal.valueOf(productAmount1));
        BigDecimal productSum2 = productDto2.getPrice().multiply(BigDecimal.valueOf(productAmount2));

        CartDto cartDto = new CartDto();
        cartDto.setItems(new ArrayList<>(List.of(
                new CartItemDto(productDto1, productAmount1, productSum1),
                new CartItemDto(productDto2, productAmount2, productSum2)
        )));
        cartDto.setTotalPrice(productSum1.add(productSum2));

        Order order = new Order();
        OrderItem orderItem1 = new OrderItem(
                null,
                order,
                productDto1.getId(),
                productDto1.getPrice(),
                productAmount1,
                productDto1.getPrice().multiply(BigDecimal.valueOf(productAmount1))
        );
        OrderItem orderItem2 = new OrderItem(
                null,
                order,
                productDto2.getId(),
                productDto2.getPrice(),
                productAmount2,
                productDto2.getPrice().multiply(BigDecimal.valueOf(productAmount2))
        );
        order.setId(null);
        order.setUsername("TestUsername");
        order.setOrderTotal(productSum1.add(productSum2));
        order.setOrderItems(new ArrayList<>(List.of(orderItem1, orderItem2)));
        order.setCreatedAt(null);
        order.setAddressId(null);

        Mockito.doReturn(null).when(orderRepository).save(Mockito.any(Order.class));

        Order resultOrder = orderService.createOrder(cartDto, "TestUsername")
                .orElseThrow(() -> new RuntimeException("Order service returned null order after creating it."));

        Assertions.assertNotNull(resultOrder);

        Assertions.assertEquals(order.getId(), resultOrder.getId());
        Assertions.assertEquals(order.getUsername(), resultOrder.getUsername());

        Assertions.assertEquals(order.getOrderItems().get(0).getId(),
                resultOrder.getOrderItems().get(0).getId());
        Assertions.assertEquals(order.getOrderItems().get(0).getAmount(),
                resultOrder.getOrderItems().get(0).getAmount());
        Assertions.assertEquals(order.getOrderItems().get(0).getSum(),
                resultOrder.getOrderItems().get(0).getSum());
        Assertions.assertEquals(order.getOrderItems().get(0).getPrice(),
                resultOrder.getOrderItems().get(0).getPrice());
        Assertions.assertEquals(order.getOrderItems().get(0).getProductId(),
                resultOrder.getOrderItems().get(0).getProductId());

        Assertions.assertEquals(order.getOrderTotal(), resultOrder.getOrderTotal());
        Assertions.assertEquals(order.getCreatedAt(), resultOrder.getCreatedAt());
        Assertions.assertEquals(order.getAddressId(), resultOrder.getAddressId());



    }
}
