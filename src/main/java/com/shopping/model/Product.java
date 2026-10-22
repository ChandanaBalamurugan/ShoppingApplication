package com.shopping.model;

/** One row of the products table. */
public class Product {
    private final int id;
    private final String name, category, description;
    private final double price;
    private final int stock;

    public Product(int id, String name, String category, double price, int stock, String description) {
        this.id = id; this.name = name; this.category = category;
        this.price = price; this.stock = stock; this.description = description;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getDescription() { return description; }
}
