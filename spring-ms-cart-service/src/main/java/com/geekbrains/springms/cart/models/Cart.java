package com.geekbrains.springms.cart.models;

import com.geekbrains.springms.api.ProductDto;
import com.geekbrains.springms.cart.integrations.ProductServiceIntegration;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {

    private List<CartItem> items;
    private Double totalPrice;

    private ProductServiceIntegration productServiceIntegration;

    @Autowired
    public void setProductServiceIntegration(ProductServiceIntegration productServiceIntegration) {
        this.productServiceIntegration = productServiceIntegration;
    }

    @PostConstruct
    public void init(){
        items = new ArrayList<>();
        totalPrice = 0D;
    }

    public Cart addProductToCartById(Long id, Integer amount) {

        if (amount == null) {amount = 1;}

        if (!items.isEmpty()) {
            for (CartItem item : items) {
                if (item.getProductDto().getId().equals(id)) {
                    item.setAmount(item.getAmount() + amount);
                    Double total = item.getProductDto().getPrice() * amount;
                    totalPrice += total;
                    item.setSum(item.getSum() + total);
                    return this;
                }
            }
        }
        //cartItem not exists yet
        ProductDto productDto = productServiceIntegration.getProductById(id);
        double total = productDto.getPrice() * amount;
        CartItem cartItem = new CartItem(productDto, amount,total);
        items.add(cartItem);
        totalPrice += total;
        return this;
    }

    public Cart removeProductFromCartById(Long id, Integer amount) {

        if (!items.isEmpty()) {
            for (CartItem item : items) {
                if (item.getProductDto().getId().equals(id)) {
                    Double minusTotal;
                    if (amount == null || item.getAmount().equals(amount)) {
                        minusTotal = item.getAmount() * item.getProductDto().getPrice();
                        items.remove(item);
                    } else {
                        item.setAmount(item.getAmount() - amount);
                        minusTotal = item.getProductDto().getPrice() * amount;
                        item.setSum(item.getSum() - minusTotal);
                    }
                    totalPrice -= minusTotal;
                    return this;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Cart clearCartContent() {
        items.clear();
        totalPrice = 0D;
        return this;
    }



}
