package com.orders.reminder;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    public Panel(Color panelColor) {
        super();
        setVisible(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(panelColor);
        setVisible(true);
    }
}

