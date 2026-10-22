
package com.shopping.gui;

import com.shopping.model.CartItem;
import com.shopping.service.CartService;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/** Cart page: change quantities, remove items, and view total. */
public class CartPanel extends JPanel {

    private final MainFrame frame;
    private final CartService cart;

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Product", "Price", "Quantity", "Subtotal"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private final JLabel total = new JLabel("Total: Rs. 0.00");

    public CartPanel(MainFrame frame, CartService cart) {
        this.frame = frame;
        this.cart = cart;

        setLayout(new BorderLayout());
        setBackground(MainFrame.LIGHT);

        add(MainFrame.heading("Your Cart"), BorderLayout.NORTH);

        MainFrame.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 0, 20)
        );
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // Cart action buttons
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 12)
        );
        actions.setOpaque(false);

        JButton plus = MainFrame.button("+");
        JButton minus = MainFrame.button("-");
        JButton remove = MainFrame.button("Remove");
        JButton shop = MainFrame.button("Continue Shopping");

        actions.add(plus);
        actions.add(minus);
        actions.add(remove);
        actions.add(shop);

        // Total and checkout button
        total.setFont(new Font("Segoe UI", Font.BOLD, 20));
        total.setForeground(MainFrame.NAVY);
        total.setHorizontalAlignment(SwingConstants.RIGHT);

        JButton pay = MainFrame.button("Proceed to Payment");

        JPanel right = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 15, 12)
        );
        right.setOpaque(false);
        right.add(total);
        right.add(pay);

        south.add(actions, BorderLayout.WEST);
        south.add(right, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);

        // Increase quantity
        plus.addActionListener(e -> change(1));

        // Decrease quantity
        minus.addActionListener(e -> change(-1));

        // Remove selected product
        remove.addActionListener(e -> {
            Integer id = selectedProductId();

            if (id != null) {
                cart.remove(id);
                refresh();
            }
        });

        // Return to product page
        shop.addActionListener(e -> frame.showPage("Products"));

        // Proceed to payment
        pay.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Your cart is empty.",
                        "Empty Cart",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                frame.showPage("Payment");
            }
        });

        refresh();
    }

    /** Returns the selected product ID, or null if no row is selected. */
    private Integer selectedProductId() {
        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an item in the cart.",
                    "No Item Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        return (Integer) model.getValueAt(row, 0);
    }

    /** Changes the quantity of the selected product. */
    private void change(int delta) {
        Integer id = selectedProductId();

        if (id == null) {
            return;
        }

        int row = table.getSelectedRow();

        String error = cart.change(id, delta);

        if (error != null) {
            JOptionPane.showMessageDialog(
                    this,
                    error,
                    "Cannot Change Quantity",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        refresh();

        // Restore the selected row if it still exists.
        if (row >= 0 && row < model.getRowCount()) {
            table.setRowSelectionInterval(row, row);
        }
    }

    /** Refreshes the cart table and recalculates the total. */
    public void refresh() {
        model.setRowCount(0);

        for (CartItem item : cart.getItems()) {
            model.addRow(new Object[]{
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    MainFrame.money(item.getProduct().getPrice()),
                    item.getQuantity(),
                    MainFrame.money(item.getSubtotal())
            });
        }

        total.setText("Total: " + MainFrame.money(cart.getTotal()));
    }
}

