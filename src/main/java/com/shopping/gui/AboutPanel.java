
package com.shopping.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(MainFrame.LIGHT);

        add(MainFrame.heading("About Our Shopping Application"),
                BorderLayout.NORTH);

        JTextArea text = new JTextArea(
                "SHOPPING APPLICATION\n\n"
                + "Project Overview:\n"
                + "The Shopping Application is a Java desktop application "
                + "that allows users to browse products, add items to a "
                + "shopping cart, and complete a simulated checkout.\n\n"
                + "Main Features:\n"
                + "- View available products and their prices.\n"
                + "- Add products to the shopping cart.\n"
                + "- Manage cart items and quantities.\n"
                + "- Place orders through a simulated checkout.\n"
                + "- Store and retrieve product and order data using MySQL.\n\n"
                + "Technologies Used:\n"
                + "- Java\n"
                + "- Java Swing and AWT for the graphical interface\n"
                + "- JDBC for database connectivity\n"
                + "- MySQL for data storage\n"
                + "- Maven for project and dependency management\n\n"
                + "How It Works:\n"
                + "The user interacts with the Java Swing interface. "
                + "The application uses JDBC to communicate with the "
                + "MySQL database. Product information is retrieved from "
                + "the database, and order details are stored during checkout.\n\n"
                + "Project Objective:\n"
                + "To demonstrate the development of a desktop shopping "
                + "system using Java programming and database connectivity.\n\n"
                + "Payment Notice:\n"
                + "Checkout is for demonstration purposes only. "
                + "No real payment is processed."
        );

        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font("SansSerif", Font.PLAIN, 14));
        text.setOpaque(false);
        text.setBorder(
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        );

        add(text, BorderLayout.CENTER);
    }
}

