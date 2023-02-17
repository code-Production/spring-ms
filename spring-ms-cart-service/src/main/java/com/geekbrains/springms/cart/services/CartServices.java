package com.geekbrains.springms.cart.services;


import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.cart.integrations.OrderServiceIntegration;
import com.geekbrains.springms.cart.mapper.CartMapper;
import com.geekbrains.springms.cart.models.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Long createAnOrderFromCartContent() {
        CartDto cartDto = CartMapper.MAPPER.toDto(cart);
        Long orderId = orderServiceIntegration.createOrderFromCartContent(cartDto);
        cart.clearCartContent();
        return orderId;
    }

}
