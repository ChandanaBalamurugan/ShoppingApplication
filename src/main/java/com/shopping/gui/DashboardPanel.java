package com.shopping.gui;

import com.shopping.dao.DashboardDAO;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Dashboard: totals loaded from MySQL. */
public class DashboardPanel extends JPanel {
    private final JLabel products = statLabel(), orders = statLabel(), sales = statLabel();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Category", "Products", "Total Stock"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(MainFrame.LIGHT);
        add(MainFrame.heading("Dashboard"), BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(1, 3, 15, 0));
        stats.setOpaque(false);
        stats.add(card("Total Products", products));
        stats.add(card("Total Orders", orders));
        stats.add(card("Total Sales", sales));

        JTable table = new JTable(model);
        MainFrame.styleTable(table);
        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        center.add(stats, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private static JLabel statLabel() {
        JLabel l = new JLabel("-", SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 28));
        l.setForeground(MainFrame.BLUE);
        return l;
    }

    private static JPanel card(String title, JLabel value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.setPreferredSize(new Dimension(200, 90));
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    /** Re-reads all numbers from the database. */
    public void refresh() {
        try {
            DashboardDAO dao = new DashboardDAO();
            products.setText(String.valueOf(dao.getProductCount()));
            orders.setText(String.valueOf(dao.getOrderCount()));
            sales.setText(MainFrame.money(dao.getTotalSales()));
            model.setRowCount(0);
            for (Object[] row : dao.getCategorySummary()) model.addRow(row);
        } catch (Exception e) {
            MainFrame.showDbError(this, e);
        }
    }
}
