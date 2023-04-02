package com.geekbrains.springms.order.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor

@Entity
@Table(schema = "spring_shop", name = "orders_content")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private Long id;

    @ManyToOne(targetEntity = Order.class)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    @Getter
    private Order order;

    @Column(name = "product_id")
    @Getter
    private Long productId;

    @Column(name = "price")
    @Getter
    private BigDecimal price;

    @Column(name = "amount")
    @Getter
    private Integer amount;

    @Column(name = "sum")
    @Getter
    private BigDecimal sum;

    public static Builder newBuilder() {
        return new OrderItem().new Builder();
    }

    public class Builder {

        private Builder() {}

        public Builder setId(Long id) {
            OrderItem.this.id = id;
            return this;
        }

        public Builder setOrder(Order order) {
            OrderItem.this.order = order;
            return this;
        }

        public Builder setProductId(Long productId) {
            OrderItem.this.productId = productId;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            OrderItem.this.price = price;
            return this;
        }

        public Builder setAmount(Integer amount) {
            OrderItem.this.amount = amount;
            return this;
        }

        public Builder setSum(BigDecimal sum) {
            OrderItem.this.sum = sum;
            return this;
        }

        public OrderItem build() {
            return OrderItem.this;
        }
    }
}
