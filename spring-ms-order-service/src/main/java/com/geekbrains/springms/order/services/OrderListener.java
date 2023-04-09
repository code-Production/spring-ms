package com.geekbrains.springms.order.services;

import com.geekbrains.springms.order.entities.Order;
import com.geekbrains.springms.order.entities.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OrderListener implements Listener{

    private Integer orderCounter;
    private BigDecimal orderTotalAvg;

    private Float orderItemAmountAvg;

    private BigDecimal avgPrice;
    private Float avgAmount;
    private BigDecimal avgSum;

    public OrderListener() {
        orderCounter = 0;
        orderTotalAvg = BigDecimal.ZERO;
        avgPrice = BigDecimal.ZERO;
        avgAmount = 0f;
        avgSum = BigDecimal.ZERO;
        orderItemAmountAvg = 0f;
    }

    @Override
    public void update(Object arg) {
        Order order = (Order) arg;
        List<OrderItem> orderItems = order.getOrderItems();
        BigDecimal avgItemPrice = BigDecimal.ZERO;
        Integer avgItemAmount = 0;
        BigDecimal avgItemSum = BigDecimal.ZERO;

        for (OrderItem orderItem : orderItems) {
            avgItemPrice = avgItemPrice.add(orderItem.getPrice());
            avgItemAmount += orderItem.getAmount();
            avgItemSum = avgItemSum.add(orderItem.getSum());
        }
        Integer orderItemsAmount = avgItemAmount; //before averaging
        avgItemPrice = avgItemPrice.divide(BigDecimal.valueOf(order.getOrderItems().size()), RoundingMode.DOWN);
        avgItemAmount = avgItemAmount / order.getOrderItems().size();
        avgItemSum = avgItemSum.divide(BigDecimal.valueOf(order.getOrderItems().size()), RoundingMode.DOWN);


        avgPrice = avgPrice
                .multiply(BigDecimal.valueOf(orderCounter))
                .add(avgItemPrice)
                .divide(BigDecimal.valueOf(orderCounter + 1), RoundingMode.DOWN);

        avgAmount = (avgAmount * orderCounter + avgItemAmount) / (orderCounter + 1);

        orderItemAmountAvg = (orderItemAmountAvg * orderCounter + orderItemsAmount) / (orderCounter + 1);

        avgSum = avgSum
                .multiply(BigDecimal.valueOf(orderCounter))
                .add(avgItemSum)
                .divide(BigDecimal.valueOf(orderCounter + 1), RoundingMode.DOWN);

        orderTotalAvg = orderTotalAvg
                .multiply(BigDecimal.valueOf(orderCounter))
                .add(order.getOrderTotal())
                .divide(BigDecimal.valueOf(orderCounter + 1), RoundingMode.DOWN);

        orderCounter++;

        System.out.println("Since order service started >>>>>\n" +
                "\tTotal order count: " + orderCounter + "\n" +
                "\tTotal order average: " + orderTotalAvg + "\n" +
                "\tTotal order items amount average: " + orderItemAmountAvg + "\n" +
                "\tAverage order item prices: " + avgPrice + "\n" +
                "\tAverage order item amounts: " + avgAmount + "\n" +
                "\tAverage order item sums: " + avgSum + "\n" +
                "\n");




    }
}
