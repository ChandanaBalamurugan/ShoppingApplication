package com.shopping.model;

import java.util.List;

/** An order with its items, ready to be saved. */
public class Order {
    private int orderId;
    private final double totalAmount;
    private final String paymentMethod;
    private final List<OrderItem> items;

    public Order(double totalAmount, String paymentMethod, List<OrderItem> items) {
        this.totalAmount = totalAmount; this.paymentMethod = paymentMethod; this.items = items;
    }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public List<OrderItem> getItems() { return items; }
}
