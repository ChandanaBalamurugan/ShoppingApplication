package com.shopping.gui;

import com.shopping.dao.ProductDAO;
import com.shopping.model.Product;
import java.awt.*;
import javax.swing.*;

/** Home page: welcome message, featured products and quick buttons. */
public class HomePanel extends JPanel {
    private final MainFrame frame;
    private final JPanel featured = new JPanel(new GridLayout(1, 4, 15, 0));

    public HomePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(MainFrame.LIGHT);

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setOpaque(false);
        top.add(MainFrame.heading("Welcome to ShopEasy"));
        JLabel sub = new JLabel("   Browse products, fill your cart and check out - a Java Swing + JDBC + MySQL demo.");
        sub.setFont(MainFrame.TEXT_FONT);
        top.add(sub);
        add(top, BorderLayout.NORTH);

        featured.setOpaque(false);
        featured.setBorder(BorderFactory.createTitledBorder("Featured Products"));
        JPanel mid = new JPanel(new BorderLayout());
        mid.setOpaque(false);
        mid.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        mid.add(featured, BorderLayout.CENTER);
        add(mid, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttons.setOpaque(false);
        JButton browse = MainFrame.button("Browse Products");
        JButton cart = MainFrame.button("Open Cart");
        browse.addActionListener(e -> frame.showPage("Products"));
        cart.addActionListener(e -> frame.showPage("Cart"));
        buttons.add(browse);
        buttons.add(cart);
        add(buttons, BorderLayout.SOUTH);
    }

    /** Loads 4 featured products from MySQL. */
    public void refresh() {
        featured.removeAll();
        try {
            for (Product p : new ProductDAO().getFeaturedProducts(4)) {
                JLabel card = new JLabel("<html><center><b>" + p.getName() + "</b><br>" + p.getCategory()
                        + "<br><br>" + MainFrame.money(p.getPrice()) + "</center></html>", SwingConstants.CENTER);
                card.setFont(MainFrame.TEXT_FONT);
                card.setOpaque(true);
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createLineBorder(new Color(200, 205, 215)));
                featured.add(card);
            }
        } catch (Exception e) {
            MainFrame.showDbError(this, e);
        }
        featured.revalidate();
        featured.repaint();
    }
}
