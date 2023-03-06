package com.geekbrains.springms.cart.services;


import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.cart.integrations.OrderServiceIntegration;
import com.geekbrains.springms.cart.mapper.CartMapper;
import com.geekbrains.springms.cart.models.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class CartServices {

    private Cart cart;

    private OrderServiceIntegration orderServiceIntegration;

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Autowired
    public void setOrderServiceIntegration(OrderServiceIntegration orderServiceIntegration) {
        this.orderServiceIntegration = orderServiceIntegration;
    }

    public Cart getCart() {
        return cart;
    }

    public Cart addProductToCartById(Long id, Integer amount) {
        return cart.addProductToCartById(id, amount);
    }

    public Cart removeProductFromCartById(Long id, Integer amount){
        return cart.removeProductFromCartById(id, amount);
    }

    public Cart clearCartContent() {
        return cart.clearCartContent();
    }

    public OrderDto createAnOrderFromCartContent(String username, Long addressId, Long billingId) {
        cart.setUsername(username);
        cart.setAddressId(addressId);
        cart.setBillingId(billingId);
        if (cart.getUsername() == null || cart.getUsername().isBlank() ||
                cart.getTotalPrice() == null ||
                cart.getAddressId() == null ||
                cart.getBillingId() == null ||
                cart.getItems().isEmpty()
        )
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart parameters must not be null.");
        }
        CartDto cartDto = CartMapper.MAPPER.toDto(cart);
        OrderDto orderDto = orderServiceIntegration.createOrderFromCartContent(cartDto);
        cart.clearCartContent();
        return orderDto;
    }

}
