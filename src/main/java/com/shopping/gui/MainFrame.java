package com.shopping.gui;

import com.shopping.service.CartService;
import java.awt.*;
import javax.swing.*;

/** The main window: a dark-blue navigation bar on top and a CardLayout that swaps pages. */
public class MainFrame extends JFrame {
    // Shared colours and fonts used by every page
    public static final Color NAVY = new Color(18, 42, 84);
    public static final Color BLUE = new Color(30, 100, 200);
    public static final Color BLUE_HOVER = new Color(20, 75, 160);
    public static final Color LIGHT = new Color(244, 246, 249);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 15);

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final CartService cart = new CartService(); // one cart shared by all pages

    private final HomePanel home = new HomePanel(this);
    private final DashboardPanel dashboard = new DashboardPanel();
    private final ProductsPanel products = new ProductsPanel(this, cart);
    private final CartPanel cartPanel = new CartPanel(this, cart);
    private final PaymentPanel payment = new PaymentPanel(this, cart);
    private final AboutPanel about = new AboutPanel();

    public MainFrame() {
        setTitle("Shopping Application");
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildNavBar(), BorderLayout.NORTH);
        content.add(home, "Home");
        content.add(dashboard, "Dashboard");
        content.add(products, "Products");
        content.add(cartPanel, "Cart");
        content.add(payment, "Payment");
        content.add(about, "About");
        add(content, BorderLayout.CENTER);
        showPage("Home");
    }

    /** Builds the reusable navigation bar. */
    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        nav.setBackground(NAVY);
        JLabel logo = new JLabel("  ShopEasy");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nav.add(logo);
        for (String name : new String[]{"Home", "Dashboard", "Products", "Cart", "About"}) {
            JButton b = button(name);
            b.addActionListener(e -> showPage(name));
            nav.add(b);
        }
        return nav;
    }

    /** Shows a page and refreshes its data from the database/cart. */
    public void showPage(String name) {
        switch (name) {
            case "Home": home.refresh(); break;
            case "Dashboard": dashboard.refresh(); break;
            case "Products": products.refresh(); break;
            case "Cart": cartPanel.refresh(); break;
            case "Payment": payment.refresh(); break;
            default: break;
        }
        cards.show(content, name);
    }

    /** Creates a blue button with a hover effect. */
    public static JButton button(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(BLUE);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(BLUE_HOVER); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(BLUE); }
        });
        return b;
    }

    /** Creates a page heading label. */
    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(TITLE_FONT);
        l.setForeground(NAVY);
        l.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        return l;
    }

    /** Applies the common look to a JTable. */
    public static void styleTable(JTable t) {
        t.setRowHeight(28);
        t.setFont(TEXT_FONT);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(NAVY);
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(190, 215, 250));
    }

    /** Shows a database error in a dialog with a helpful hint. */
    public static void showDbError(Component parent, Exception e) {
        JOptionPane.showMessageDialog(parent,
                "Database error: " + e.getMessage()
                + "\n\nCheck that MySQL is running, shopping_db.sql was executed,"
                + "\nand the password in db.properties is correct.",
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    public static String money(double v) { return String.format("Rs. %.2f", v); }
}
