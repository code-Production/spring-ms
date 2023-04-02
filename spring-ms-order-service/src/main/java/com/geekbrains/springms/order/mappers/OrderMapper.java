package com.geekbrains.springms.order.mappers;

import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.order.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private OrderItemMapper orderItemMapper;

    @Autowired
    public void setOrderItemMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public OrderDto toDto(Order order) {
        return new OrderDto().newBuilder()
                .setId(order.getId())
                .setUsername(order.getUsername())
                .setOrderItems(orderItemMapper.toDtoList(order.getOrderItems()))
                .setAddressId(order.getAddressId())
                .setBillingId(order.getBillingId())
                .setOrderTotal(order.getOrderTotal())
                .build();
//        return new OrderDto(
//                order.getId(),
//                order.getUsername(),
//                orderItemMapper.toDtoList(order.getOrderItems()),
//                order.getAddressId(),
//                order.getBillingId(),
//                order.getOrderTotal()
//        );
    }
}