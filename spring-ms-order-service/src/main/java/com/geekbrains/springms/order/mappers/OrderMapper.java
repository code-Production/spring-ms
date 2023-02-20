package com.geekbrains.springms.order.mappers;

import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.order.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = OrderItemMapper.class)
public interface OrderMapper {

    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    OrderDto toDto(Order order);


}
