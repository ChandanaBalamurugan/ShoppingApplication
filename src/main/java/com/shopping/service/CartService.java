package com.shopping.service;

import com.shopping.model.CartItem;
import com.shopping.model.Product;
import java.util.ArrayList;
import java.util.List;

/** Holds the shopping cart in memory. One shared instance is used by all pages. */
public class CartService {
    private final List<CartItem> items = new ArrayList<>();

    /** Adds a product. If already in cart, quantities are added. Returns an error message, or null if OK. */
    public String add(Product p, int qty) {
        if (qty <= 0) return "Quantity must be at least 1.";
        CartItem existing = find(p.getId());
        int newQty = qty + (existing == null ? 0 : existing.getQuantity());
        if (newQty > p.getStock()) return "Only " + p.getStock() + " units of " + p.getName() + " in stock.";
        if (existing == null) items.add(new CartItem(p, qty)); else existing.setQuantity(newQty);
        return null;
    }

    /** Changes quantity by +1 or -1. Removes the item if it reaches 0. Returns an error message, or null. */
    public String change(int productId, int delta) {
        CartItem ci = find(productId);
        if (ci == null) return null;
        int q = ci.getQuantity() + delta;
        if (q > ci.getProduct().getStock()) return "No more stock available.";
        if (q <= 0) items.remove(ci); else ci.setQuantity(q);
        return null;
    }

    public void remove(int productId) { items.removeIf(i -> i.getProduct().getId() == productId); }
    public void clear() { items.clear(); }
    public boolean isEmpty() { return items.isEmpty(); }
    public List<CartItem> getItems() { return items; }

    public double getTotal() {
        double t = 0;
        for (CartItem i : items) t += i.getSubtotal();
        return t;
    }

    private CartItem find(int productId) {
        for (CartItem i : items) if (i.getProduct().getId() == productId) return i;
        return null;
    }
}
