package com.geekbrains.springms.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class OrderDto {

    private Long id;
    private String username;
    private List<OrderItemDto> orderItems;
    private Long addressId;
    private Long billingId;
    private BigDecimal orderTotal;

    public Builder newBuilder() {
        return this.new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder setId(Long id) {
            OrderDto.this.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            OrderDto.this.username = username;
            return this;
        }

        public Builder setOrderItems(List<OrderItemDto> orderItems) {
            OrderDto.this.orderItems = orderItems;
            return this;
        }

        public Builder setAddressId(Long addressId) {
            OrderDto.this.addressId = addressId;
            return this;
        }

        public Builder setBillingId(Long billingId) {
            OrderDto.this.billingId = billingId;
            return this;
        }

        public Builder setOrderTotal(BigDecimal orderTotal) {
            OrderDto.this.orderTotal = orderTotal;
            return this;
        }

        public OrderDto build() {
            return OrderDto.this;
        }
    }

}
