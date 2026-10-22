package com.shopping.dao;

import com.shopping.database.DBConnection;
import com.shopping.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** All SQL related to the products table. */
public class ProductDAO {

    /** Returns every product. */
    public List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product_id, product_name, category, price, stock, description FROM products";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /** Returns the first few products to show as "featured" on the Home page. */
    public List<Product> getFeaturedProducts(int limit) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product_id, product_name, category, price, stock, description FROM products ORDER BY product_id LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    /** Converts the current ResultSet row into a Product object. */
    private Product map(ResultSet rs) throws SQLException {
        return new Product(rs.getInt("product_id"), rs.getString("product_name"), rs.getString("category"),
                rs.getDouble("price"), rs.getInt("stock"), rs.getString("description"));
    }
}
