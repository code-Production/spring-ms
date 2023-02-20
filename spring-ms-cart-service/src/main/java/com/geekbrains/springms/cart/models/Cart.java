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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {

    private List<CartItem> items;
    private BigDecimal totalPrice;

    private ProductServiceIntegration productServiceIntegration;

    @Autowired
    public void setProductServiceIntegration(ProductServiceIntegration productServiceIntegration) {
        this.productServiceIntegration = productServiceIntegration;
    }

    @PostConstruct
    public void init(){
        items = new ArrayList<>();
        totalPrice = BigDecimal.ZERO;
    }

    public Cart addProductToCartById(Long id, Integer amount) {

        if (amount == null) {amount = 1;}

        if (!items.isEmpty()) {
            for (CartItem item : items) {
                if (item.getProductDto().getId().equals(id)) {
                    item.setAmount(item.getAmount() + amount);
                    BigDecimal total = item.getProductDto().getPrice().multiply(BigDecimal.valueOf(amount));
                    totalPrice = totalPrice.add(total);
                    item.setSum(item.getSum().add(total));
                    return this;
                }
            }
        }
        //cartItem not exists yet
        ProductDto productDto = productServiceIntegration.getProductById(id);
        BigDecimal total = productDto.getPrice().multiply(BigDecimal.valueOf(amount));
        CartItem cartItem = new CartItem(productDto, amount,total);
        items.add(cartItem);
        totalPrice = totalPrice.add(total);
        return this;
    }

    public Cart removeProductFromCartById(Long id, Integer amount) {

        if (!items.isEmpty()) {
            for (CartItem item : items) {
                if (item.getProductDto().getId().equals(id)) {
                    BigDecimal minusTotal;
                    if (amount == null || item.getAmount().equals(amount)) {
                        minusTotal =
                                BigDecimal.valueOf(item.getAmount()).multiply(item.getProductDto().getPrice());
                        items.remove(item);
                    } else {
                        item.setAmount(item.getAmount() - amount);
                        minusTotal = item.getProductDto().getPrice().multiply(BigDecimal.valueOf(amount));
                        item.setSum(item.getSum().subtract(minusTotal));
                    }
                    totalPrice = totalPrice.subtract(minusTotal);
                    return this;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Cart clearCartContent() {
        items.clear();
        totalPrice = BigDecimal.ZERO;
        return this;
    }



}
