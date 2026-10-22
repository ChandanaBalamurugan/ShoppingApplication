package com.shopping.dao;

import com.shopping.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Read-only statistics for the Dashboard page. */
public class DashboardDAO {

    public int getProductCount() throws SQLException { return (int) scalar("SELECT COUNT(*) FROM products"); }
    public int getOrderCount() throws SQLException { return (int) scalar("SELECT COUNT(*) FROM orders"); }
    public double getTotalSales() throws SQLException { return scalar("SELECT COALESCE(SUM(total_amount), 0) FROM orders"); }

    /** Runs a query that returns one number. */
    private double scalar(String sql) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    /** Per-category summary: {category, number of products, total stock}. */
    public List<Object[]> getCategorySummary() throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT category, COUNT(*), SUM(stock) FROM products GROUP BY category ORDER BY category";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rows.add(new Object[]{rs.getString(1), rs.getInt(2), rs.getInt(3)});
        }
        return rows;
    }
}
