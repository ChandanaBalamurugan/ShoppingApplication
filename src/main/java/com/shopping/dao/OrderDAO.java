package com.shopping.dao;

import com.shopping.database.DBConnection;
import com.shopping.model.Order;
import com.shopping.model.OrderItem;
import java.sql.*;

/** Saves orders using a database transaction. */
public class OrderDAO {

    /**
     * Saves the order, its items, and reduces stock - all or nothing.
     * Throws SQLException (after rollback) if anything fails, e.g. not enough stock.
     */
    public int placeOrder(Order order) throws SQLException {
        String insertOrder = "INSERT INTO orders (total_amount, payment_method, order_status) VALUES (?, ?, 'PLACED')";
        String insertItem = "INSERT INTO order_items (order_id, product_id, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?)";
        // The "stock >= ?" condition makes sure stock never goes negative
        String updateStock = "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false); // start transaction
            try {
                int orderId;
                try (PreparedStatement ps = con.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDouble(1, order.getTotalAmount());
                    ps.setString(2, order.getPaymentMethod());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        orderId = keys.getInt(1); // the new order_id
                    }
                }
                try (PreparedStatement item = con.prepareStatement(insertItem);
                     PreparedStatement stock = con.prepareStatement(updateStock)) {
                    for (OrderItem oi : order.getItems()) {
                        stock.setInt(1, oi.getQuantity());
                        stock.setInt(2, oi.getProductId());
                        stock.setInt(3, oi.getQuantity());
                        if (stock.executeUpdate() == 0) {
                            throw new SQLException("Insufficient stock for product ID " + oi.getProductId());
                        }
                        item.setInt(1, orderId);
                        item.setInt(2, oi.getProductId());
                        item.setInt(3, oi.getQuantity());
                        item.setDouble(4, oi.getPrice());
                        item.setDouble(5, oi.getSubtotal());
                        item.executeUpdate();
                    }
                }
                con.commit(); // everything worked
                order.setOrderId(orderId);
                return orderId;
            } catch (SQLException e) {
                con.rollback(); // undo everything
                throw e;
            }
        }
    }
}
