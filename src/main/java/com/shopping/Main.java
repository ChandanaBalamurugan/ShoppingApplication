package com.shopping;

import com.shopping.gui.MainFrame;
import javax.swing.SwingUtilities;

/** Entry point. Starts the Swing GUI on the Event Dispatch Thread. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
