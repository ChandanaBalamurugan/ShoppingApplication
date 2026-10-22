package com.shopping.gui;

import com.shopping.dao.ProductDAO;
import com.shopping.model.Product;
import com.shopping.service.CartService;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Products page: table of products with quantity selector and Add to Cart. */
public class ProductsPanel extends JPanel {
    private final MainFrame frame;
    private final CartService cart;
    private final List<Product> productList = new ArrayList<>(); // same order as table rows
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Name", "Category", "Price", "Stock"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

    public ProductsPanel(MainFrame frame, CartService cart) {
        this.frame = frame;
        this.cart = cart;
        setLayout(new BorderLayout());
        setBackground(MainFrame.LIGHT);
        add(MainFrame.heading("Products"), BorderLayout.NORTH);

        MainFrame.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        center.setOpaque(false);
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        bottom.setOpaque(false);
        JLabel q = new JLabel("Quantity:");
        q.setFont(MainFrame.TEXT_FONT);
        JButton add = MainFrame.button("Add to Cart");
        JButton go = MainFrame.button("Go to Cart");
        bottom.add(q);
        bottom.add(qty);
        bottom.add(add);
        bottom.add(go);
        add(bottom, BorderLayout.SOUTH);

        add.addActionListener(e -> addSelected());
        go.addActionListener(e -> frame.showPage("Cart"));
    }

    /** Loads products from MySQL into the table. */
    public void refresh() {
        model.setRowCount(0);
        productList.clear();
        try {
            for (Product p : new ProductDAO().getAllProducts()) {
                productList.add(p);
                model.addRow(new Object[]{p.getId(), p.getName(), p.getCategory(),
                        MainFrame.money(p.getPrice()), p.getStock()});
            }
        } catch (Exception e) {
            MainFrame.showDbError(this, e);
        }
    }

    /** Adds the selected row to the cart after validating. */
    private void addSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product first.", "No product selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Product p = productList.get(row);
        String error = cart.add(p, (Integer) qty.getValue());
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Cannot add", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, qty.getValue() + " x " + p.getName() + " added to cart.");
        }
    }
}
