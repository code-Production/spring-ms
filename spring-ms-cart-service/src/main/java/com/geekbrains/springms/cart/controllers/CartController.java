package com.geekbrains.springms.cart.controllers;

import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.cart.mapper.CartMapper;
import com.geekbrains.springms.cart.models.Cart;
import com.geekbrains.springms.cart.services.CartServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Enumeration;


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
    public OrderDto createOnOrderFromCartContent(
            @RequestParam(name = "address_id") Long addressId,
            @RequestParam(name = "billing_id") Long billingId,
            HttpServletRequest request
    ){
        String username = checkAuthorizationHeaderOrThrowException(request);
        return cartServices.createAnOrderFromCartContent(username, addressId, billingId);
    }


    private String checkAuthorizationHeaderOrThrowException(HttpServletRequest request) {
        String username = request.getHeader("username");
        if (username != null && !username.isBlank()) {
            return username;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access.");
    }

}
