package com.orders.reminder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.Properties;




public class TranslucentCircularDialog extends JDialog {
    final private Integer countOrd;
    private JLabel label;
    private JPanel panel;
    private Integer X=null, Y=null;
    final private float opacity=0.75f;
    final private Integer width,height;
    final Rectangle rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice().getDefaultConfiguration().getBounds();

    public TranslucentCircularDialog(Integer countOrders,Boolean isAlarm, Integer w,Integer h,Integer xShift,Integer yShift, Color backgroundColor, Color textColor) {
        this.width = w;
        this.height = h;
        countOrd=countOrders;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        addMouseListener(new TranslucentCircularDialogMListener());
        addMouseMotionListener(new TranslucentCircularDialogMListener());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new Ellipse2D.Float(0,0,getWidth(),getHeight()));
            }
        });
        loadProperties();
        if(X==null || Y==null) {
            X = rectangle.width - 100;
            Y = 45;
        }
        setUndecorated(true);
        setSize(width, height);
        setOpacity(opacity);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        getContentPane().setBackground(Color.red);
        setLocation(X + xShift, Y + yShift);
        checkPosition();
        if(isAlarm) {
            panel = new GradientPanel();
            setContentPane(panel);
        }else{
            label=new JLabel(countOrd.toString());
            label.setFont(new Font("Sherif",Font.BOLD,25));
            label.setForeground(textColor);
            panel = new Panel(backgroundColor);
            setContentPane(panel);
            add(Box.createHorizontalGlue());
            add(label);
            add(Box.createHorizontalGlue());
        }
        setVisible(true);
    }


    private class TranslucentCircularDialogMListener implements MouseListener, MouseMotionListener {
        @Override
        public void mouseClicked(MouseEvent e) {
                if(getContentPane().getClass().equals(GradientPanel.class)){
                    setVisible(false);
                    panel = new Panel(Color.blue);
                    label=new JLabel(countOrd.toString());
                    label.setFont(new Font("Sherif",Font.BOLD,25));
                    label.setForeground(Color.WHITE);
                    setContentPane(panel);
                    add(Box.createHorizontalGlue());
                    add(label);
                    add(Box.createHorizontalGlue());
                    setVisible(true);
                }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            checkPosition();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {
            saveProperties();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            X=getX()+e.getX()-width/2;
            Y=getY()+e.getY()-height/2;
            setLocation(X,Y);
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    private void saveProperties(){
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("user.properties");

            prop.setProperty("X", X.toString());
            prop.setProperty("Y", Y.toString());
            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    private void loadProperties(){
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("user.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            X=Integer.parseInt(prop.getProperty("X"));
            Y=Integer.parseInt(prop.getProperty("Y"));
        } catch (IOException ex) {
            //ex.printStackTrace();
            X=null;
            Y=null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void checkPosition(){
        boolean need_repaint=false;
        if(X<=0){
            X=0;
            need_repaint=true;
        }
        if(Y<=0){
            Y=0;
            need_repaint=true;
        }
        if(X+width>=rectangle.width){
            X=rectangle.width-width;
            need_repaint=true;
        }
        if(Y+height>=rectangle.height){
            Y=rectangle.height-height;
            need_repaint=true;
        }
        if(need_repaint){
            setLocation(X,Y);
        }
    }

}

