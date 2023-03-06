package com.geekbrains.springms.order.mappers;

import com.geekbrains.springms.api.OrderItemDto;
import com.geekbrains.springms.order.entities.OrderItem;
import com.geekbrains.springms.order.integrations.ProductServiceIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class OrderItemMapper {

    //не знаю какое решение лучше, либо тут запрашивать title, либо на фронте (что тоже самое) или хранить его в БД ордеров,
    // но так как базы изолированы не понятно как синхронизировать title здесь и в базе продуктов,
    // здесь решение выглядит приглядней чем на фронте
    //или пофигу что столько запросов?

    private ProductServiceIntegration productServiceIntegration;

    @Autowired
    public void setProductServiceIntegration(ProductServiceIntegration productServiceIntegration) {
        this.productServiceIntegration = productServiceIntegration;
    }

    public OrderItemDto toDto(OrderItem orderItem) {
        String title = productServiceIntegration.findProductById(orderItem.getProductId()).getTitle();
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getProductId(),
                title,
                orderItem.getPrice(),
                orderItem.getAmount(),
                orderItem.getSum()
        );
    }

    public List<OrderItemDto> toDtoList(List<OrderItem> orderItemList) {
        return orderItemList.stream().map(this::toDto).toList();
    }
}
