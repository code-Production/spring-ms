package com.geekbrains.springms.cart.controllers;

import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.cart.mapper.CartMapper;
import com.geekbrains.springms.cart.models.Cart;
import com.geekbrains.springms.cart.services.CartServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class CartController {

    private CartServices cartServices;

    @Autowired
    public void setCartServices(CartServices cartServices) {
        this.cartServices = cartServices;
    }

    @GetMapping("/")
    public CartDto getCartContent() {
        return CartMapper.MAPPER.toDto(cartServices.getCart());
    }

    @PostMapping("/")
    public CartDto addProductToCartById(@RequestParam Long id, @RequestParam(required = false) Integer amount) {
        return CartMapper.MAPPER.toDto(cartServices.addProductToCartById(id, amount));
    }

    @DeleteMapping("/")
    public CartDto removeProductFromCartById(@RequestParam Long id, @RequestParam(required = false) Integer amount){
        return CartMapper.MAPPER.toDto(cartServices.removeProductFromCartById(id, amount));
    }

    @DeleteMapping("/clear")
    public CartDto clearCartContent() {
        return CartMapper.MAPPER.toDto(cartServices.clearCartContent());
    }

    @GetMapping("/checkout")
    public OrderDto createOnOrderFromCartContent(){
        return cartServices.createAnOrderFromCartContent();
    }
}
