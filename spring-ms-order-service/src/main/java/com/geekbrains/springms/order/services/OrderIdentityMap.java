package com.geekbrains.springms.order.services;

import com.geekbrains.springms.order.entities.Order;
import lombok.Getter;

import java.util.HashMap;

public class OrderIdentityMap {

    private final HashMap<Long, Order> orders;

    public OrderIdentityMap() {
        this.orders = new HashMap<>();
    }

    public void addOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public Order findOrder(Long id) {
        return orders.get(id);
    }

}
