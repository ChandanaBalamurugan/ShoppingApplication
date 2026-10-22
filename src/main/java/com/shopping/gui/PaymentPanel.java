package com.shopping.gui;

import com.shopping.dao.OrderDAO;
import com.shopping.model.CartItem;
import com.shopping.model.Order;
import com.shopping.model.OrderItem;
import com.shopping.service.CartService;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/** Checkout page: order summary, demo payment method, Place Order. */
public class PaymentPanel extends JPanel {
    private final MainFrame frame;
    private final CartService cart;
    private final JTextArea summary = new JTextArea();
    private final JTextField nameField = new JTextField(18);
    private final JTextField upiField = new JTextField(18);
    private final JComboBox<String> method = new JComboBox<>(new String[]{"Cash on Delivery", "Demo UPI"});

    public PaymentPanel(MainFrame frame, CartService cart) {
        this.frame = frame;
        this.cart = cart;
        setLayout(new BorderLayout());
        setBackground(MainFrame.LIGHT);
        add(MainFrame.heading("Payment / Checkout"), BorderLayout.NORTH);

        summary.setEditable(false);
        summary.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        center.add(new JScrollPane(summary), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 8));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        form.add(new JLabel("Customer name:")); form.add(nameField);
        form.add(new JLabel("Payment method:")); form.add(method);
        form.add(new JLabel("Demo UPI ID (only for UPI):")); form.add(upiField);
        JLabel note = new JLabel("Payment is SIMULATED. No real transaction occurs.");
        note.setForeground(new Color(180, 40, 40));
        form.add(note); form.add(new JLabel(""));

        JButton place = MainFrame.button("Place Order");
        JButton back = MainFrame.button("Back to Cart");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        buttons.setOpaque(false);
        buttons.add(back); buttons.add(place);

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(form, BorderLayout.CENTER);
        south.add(buttons, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        method.addActionListener(e -> upiField.setEnabled("Demo UPI".equals(method.getSelectedItem())));
        back.addActionListener(e -> frame.showPage("Cart"));
        place.addActionListener(e -> placeOrder());
    }

    /** Fills the order summary text. */
    public void refresh() {
        StringBuilder sb = new StringBuilder("ORDER SUMMARY\n-------------\n");
        for (CartItem i : cart.getItems()) {
            sb.append(String.format("%-25s x%-3d %12s%n", i.getProduct().getName(), i.getQuantity(), MainFrame.money(i.getSubtotal())));
        }
        sb.append("\nGRAND TOTAL: ").append(MainFrame.money(cart.getTotal()));
        summary.setText(sb.toString());
        upiField.setEnabled("Demo UPI".equals(method.getSelectedItem()));
    }

    /** Validates input, then saves the order through OrderDAO (a database transaction). */
    private void placeOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the customer name.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String selected = (String) method.getSelectedItem();
        if ("Demo UPI".equals(selected) && !upiField.getText().trim().matches("[\\w.\\-]+@[\\w.\\-]+")) {
            JOptionPane.showMessageDialog(this, "Enter a demo UPI ID like name@bank.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<OrderItem> items = new ArrayList<>();
        for (CartItem i : cart.getItems()) {
            items.add(new OrderItem(i.getProduct().getId(), i.getQuantity(), i.getProduct().getPrice()));
        }
        Order order = new Order(cart.getTotal(), selected, items);
        try {
            int id = new OrderDAO().placeOrder(order);
            JOptionPane.showMessageDialog(this,
                    "Order placed successfully!\nOrder ID: " + id + "\nTotal: " + MainFrame.money(order.getTotalAmount())
                    + "\n\n(Payment was simulated - no real transaction occurred.)",
                    "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
            cart.clear();
            nameField.setText("");
            upiField.setText("");
            frame.showPage("Dashboard");
        } catch (Exception e) {
            MainFrame.showDbError(this, e); // transaction was rolled back
        }
    }
}
