package com.geekbrains.springms.cart.services;


import com.geekbrains.springms.api.CartDto;
import com.geekbrains.springms.api.OrderDto;
import com.geekbrains.springms.api.ProductDto;
import com.geekbrains.springms.cart.integrations.OrderServiceIntegration;
import com.geekbrains.springms.cart.integrations.ProductServiceIntegration;
import com.geekbrains.springms.cart.integrations.ProductServiceIntegrationCached;
import com.geekbrains.springms.cart.mapper.CartMapper;
import com.geekbrains.springms.cart.models.Cart;
import com.geekbrains.springms.cart.models.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import com.geekbrains.springms.api.RedisPrefix;

@Service
public class CartServices {


    private RedisTemplate<String, Cart> carts;

    @Autowired
    public void setCarts(RedisTemplate<String, Cart> carts) {
        this.carts = carts;
    }

    private OrderServiceIntegration orderServiceIntegration;

    private ProductServiceIntegration productServiceIntegration;

    private ProductServiceIntegrationCached productServiceIntegrationCached;

    @Autowired
    public void setProductServiceIntegrationCached(ProductServiceIntegrationCached productServiceIntegrationCached) {
        this.productServiceIntegrationCached = productServiceIntegrationCached;
    }

    @Autowired
    public void setOrderServiceIntegration(OrderServiceIntegration orderServiceIntegration) {
        this.orderServiceIntegration = orderServiceIntegration;
    }

    @Autowired
    public void setProductServiceIntegration(ProductServiceIntegration productServiceIntegration) {
        this.productServiceIntegration = productServiceIntegration;
    }

    public Cart getCart(String username, String guestCartId) {

        String prefixedGuestCartId = addPrefixTo(guestCartId);
        String prefixedUsername = addPrefixTo(username);

        Cart guestCart = null;

        if (Boolean.TRUE.equals(carts.hasKey(prefixedGuestCartId))) {
            guestCart = carts.opsForValue().get(prefixedGuestCartId);
        }

        //logged in
        if (username != null) {
            if (guestCart != null && !guestCart.getItems().isEmpty()) {
                //merging guest and main cart
                for (CartItem item : guestCart.getItems()) {
                    //will choose username as id and create cart if needed
                    addProductToCartById(username, guestCartId, item.getProductDto().getId(), item.getAmount());
                }
            }
            if (guestCart != null) {
                carts.delete(prefixedGuestCartId); //remove guest cart from redis
            }
            return getCartOrCreate(prefixedUsername);
        }

        //not logged in
        return getCartOrCreate(prefixedGuestCartId);

    }


    private String chooseId(String username, String guestCartId) {
        if (username != null) {
            return username;
        }
        return guestCartId;
    }

    private String addPrefixTo(String str) {
        return RedisPrefix.CART.getPrefix() + str;
    }



    public OrderDto createAnOrderFromCartContent(String username, Long addressId, Long billingId) {

        String prefixedUsername = addPrefixTo(username);

        Cart cart = getCartOrCreate(prefixedUsername);

        cart.setUsername(username);
        cart.setAddressId(addressId);
        cart.setBillingId(billingId);

        if (cart.getUsername() == null ||
            cart.getUsername().isBlank() ||
            cart.getTotalPrice() == null ||
            cart.getAddressId() == null ||
            cart.getBillingId() == null ||
            cart.getItems().isEmpty()
        )
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart parameters must not be null or empty.");
        }


        CartDto cartDto = CartMapper.MAPPER.toDto(cart);
        OrderDto orderDto = orderServiceIntegration.createOrderFromCartContent(cartDto, username);
        clearCartContent(username, null);
        return orderDto;
    }

    private Cart getCartOrCreate(String cartId) {
        if (Boolean.TRUE.equals(carts.hasKey(cartId))) {
            return carts.opsForValue().get(cartId);
        } else {
            Cart cart = new Cart();
            carts.opsForValue().set(cartId, cart);
            return cart;
        }
    }


    public Cart addProductToCartById(String username, String guestCartId, Long productId, Integer amount) {
        String prefixedCartId = addPrefixTo(chooseId(username, guestCartId));

        Cart cart = getCartOrCreate(prefixedCartId);

        if (amount == null) {amount = 1;}

        if (!cart.getItems().isEmpty()) {
            for (CartItem item : cart.getItems()) {
                if (item.getProductDto().getId().equals(productId)) {
                    item.setAmount(item.getAmount() + amount);
                    BigDecimal total = item.getProductDto().getPrice().multiply(BigDecimal.valueOf(amount));
                    cart.setTotalPrice(cart.getTotalPrice().add(total));
                    item.setSum(item.getSum().add(total));

                    carts.opsForValue().set(prefixedCartId, cart);
                    return cart;
                }
            }
        }

        //cartItem not exists yet
//        ProductDto productDto = productServiceIntegration.getProductById(productId);
        ProductDto productDto = productServiceIntegrationCached.getProductById(productId);
        BigDecimal total = productDto.getPrice().multiply(BigDecimal.valueOf(amount));
        CartItem cartItem = new CartItem(productDto, amount,total);
        cart.getItems().add(cartItem);
        cart.setTotalPrice(cart.getTotalPrice().add(total));
        carts.opsForValue().set(prefixedCartId, cart);
        return cart;
    }


    public Cart removeProductFromCartById(String username, String guestCartId, Long productId, Integer amount) {
        String prefixedCartId = addPrefixTo(chooseId(username, guestCartId));
        Cart cart = getCartOrCreate(prefixedCartId);

        if (!cart.getItems().isEmpty()) {
            for (CartItem item : cart.getItems()) {
                System.out.println("before");
                if (item.getProductDto().getId().equals(productId)) {
                    System.out.println("inside");
                    BigDecimal minusTotal;
                    if (amount == null || item.getAmount().equals(amount)) {
                        minusTotal =
                                BigDecimal.valueOf(item.getAmount()).multiply(item.getProductDto().getPrice());
                        cart.getItems().remove(item);
                    } else {
                        item.setAmount(item.getAmount() - amount);
                        minusTotal = item.getProductDto().getPrice().multiply(BigDecimal.valueOf(amount));
                        item.setSum(item.getSum().subtract(minusTotal));
                    }

                    cart.setTotalPrice(cart.getTotalPrice().subtract(minusTotal));
                    carts.opsForValue().set(prefixedCartId, cart);
                    return cart;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Cart clearCartContent(String username, String guestCartId) {
        String prefixedCartId = addPrefixTo(chooseId(username, guestCartId));
        Cart cart = new Cart();
        carts.opsForValue().set(prefixedCartId, cart);
        return cart;
    }


}
