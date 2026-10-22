package com.shopping.model;

/** One row of order_items. */
public class OrderItem {
    private final int productId, quantity;
    private final double price, subtotal;

    public OrderItem(int productId, int quantity, double price) {
        this.productId = productId; this.quantity = quantity;
        this.price = price; this.subtotal = price * quantity;
    }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getSubtotal() { return subtotal; }
}
