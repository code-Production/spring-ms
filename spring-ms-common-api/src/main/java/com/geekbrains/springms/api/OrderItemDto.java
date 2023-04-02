package com.geekbrains.springms.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long productId;
    private String title;
    private BigDecimal price;
    private Integer amount;
    private BigDecimal sum;

    public Builder newBuilder() {
        return this.new Builder();
    }

    public class Builder {

        private Builder() {}

        public Builder setId(Long id) {
            OrderItemDto.this.id = id;
            return this;
        }

        public Builder setProductId(Long productId) {
            OrderItemDto.this.productId = productId;
            return this;
        }

        public Builder setTitle(String title) {
            OrderItemDto.this.title = title;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            OrderItemDto.this.price = price;
            return this;
        }

        public Builder setAmount(Integer amount) {
            OrderItemDto.this.amount = amount;
            return this;
        }

        public Builder setSum(BigDecimal sum) {
            OrderItemDto.this.sum = sum;
            return this;
        }

        public OrderItemDto build() {
            return OrderItemDto.this;
        }
    }
}
