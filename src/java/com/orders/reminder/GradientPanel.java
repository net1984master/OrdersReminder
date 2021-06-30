package com.orders.reminder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class GradientPanel extends JPanel {

    public int yScale = 5;
    public int increment = 10;
    RadialGradientPaint paint;
    AffineTransform moveToOrigin;

    GradientPanel() {
        Point2D center = new Point2D.Float(40, 40);
        float radius = 35;
        //float[] dist = {0.05f, .95f};
        float[] dist = {0.1f, 0.95f};
        Color[] colors = {Color.GREEN, Color.RED};
        paint = new RadialGradientPaint(center, radius, dist, colors, MultipleGradientPaint.CycleMethod.REFLECT);
        moveToOrigin = AffineTransform.
                getTranslateInstance(-40d, -40d);
        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (increment < 0) {
                    increment = (yScale < 80 ? -increment : increment);
                } else {
                    increment = (yScale > 120 ? -increment : increment);
                }
                yScale += increment;
                repaint();
            }
        };

        Timer t = new Timer(15, listener);
        t.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform moveToCenter = AffineTransform.
                getTranslateInstance(getWidth() / 2d, getHeight() / 2d);
        g2.setPaint(paint);
        double y = yScale / 100d;
        double x = 1 / y;
        AffineTransform at = AffineTransform.getScaleInstance(x, y);

        // We need to move it to the origin, scale, and move back.
        // Counterintutitively perhaps, we concatentate 'in reverse'.
        moveToCenter.concatenate(at);
        moveToCenter.concatenate(moveToOrigin);
        g2.setTransform(moveToCenter);

        // fudge factor of 3 here, to ensure the scaling of the transform
        // does not leave edges unpainted.
        g2.fillRect(-getWidth(), -getHeight(), getWidth()*3, getHeight()*3);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }
}
