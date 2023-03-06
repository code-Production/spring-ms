package com.geekbrains.springms.cart.controllers;

import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.api.StringResponse;
import com.geekbrains.springms.cart.mapper.CartMapper;
import com.geekbrains.springms.cart.models.Cart;
import com.geekbrains.springms.cart.services.CartServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;


@RestController
public class CartController {

    private CartServices cartServices;

    @Autowired
    public void setCartServices(CartServices cartServices) {
        this.cartServices = cartServices;
    }

    @GetMapping("/generate_uid")
    public StringResponse generateId() {
        return new StringResponse(UUID.randomUUID().toString());
    }

    @GetMapping("/{guestCartId}")
    public CartDto getCartContent(
            @RequestHeader (required = false) String username,
            @PathVariable String guestCartId
    )
    {
        return CartMapper.MAPPER.toDto(cartServices.getCart(username, guestCartId));
    }

    @PostMapping("/{guestCartId}")
    public CartDto addProductToCartById(
            @RequestHeader(required = false) String username,
            @PathVariable String guestCartId,
            @RequestParam(name = "product_id") Long productId,
            @RequestParam(required = false) Integer amount
    )
    {
        return CartMapper.MAPPER.toDto(cartServices.addProductToCartById(username, guestCartId, productId, amount));
    }

    @DeleteMapping("/{guestCartId}")
    public CartDto removeProductFromCartById(
            @RequestHeader(required = false) String username,
            @PathVariable String guestCartId,
            @RequestParam(name = "product_id") Long productId,
            @RequestParam(required = false) Integer amount
    )
    {
        return CartMapper.MAPPER.toDto(cartServices.removeProductFromCartById(username, guestCartId, productId, amount));
    }

    @DeleteMapping("/{guestCartId}/clear")
    public CartDto clearCartContent(
            @RequestHeader(required = false) String username,
            @PathVariable String guestCartId
    ) {
        return CartMapper.MAPPER.toDto(cartServices.clearCartContent(username, guestCartId));
    }

    @GetMapping("/checkout")
    public OrderDto createOnOrderFromCartContent(
            @RequestHeader String username,
            @RequestParam(name = "address_id") Long addressId,
            @RequestParam(name = "billing_id") Long billingId
    ){
        return cartServices.createAnOrderFromCartContent(username, addressId, billingId);
    }


}
