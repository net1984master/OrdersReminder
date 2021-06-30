//package com.orders.reminder;
//
//import javax.swing.*;
//
//import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
//
//public class Message{
//    public static void setWarningMsg(String text){
////        Toolkit.getDefaultToolkit().beep();
////        JOptionPane optionPane = new JOptionPane(text,JOptionPane.WARNING_MESSAGE);
////        JDialog dialog = optionPane.createDialog("Warning!");
////        dialog.setAlwaysOnTop(true);
////        dialog.setVisible(true);
////        dialog.setModal(true);
//        JFrame frame=new JFrame();
//        JDialog dialog = new JDialog(frame, "Превышение лимита", false);
//        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        dialog.setSize(180, 90);
//    }
//}
package com.orders.reminder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Message extends JDialog implements ActionListener {
    public Message(JFrame parent, String title, String message) {
        super(parent, title, true);
        if (parent != null) {
//            Dimension parentSize = parent.getSize();
//            Point p = parent.getLocation();
            //setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
            setSize(200, 400);
            setResizable(false);
            final Toolkit toolkit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = toolkit.getScreenSize();
            final int x = (screenSize.width - getWidth()) / 2;
            final int y = (screenSize.height - getHeight()) / 2;
            setLocation(x, y);
//            setLocationRelativeTo(null);
            setModal(false);
            setAlwaysOnTop(true);
        }
        JPanel messagePane = new JPanel();
        messagePane.add(new JLabel(message));
        getContentPane().add(messagePane);
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("ЯСНО");
        buttonPane.add(button);
        button.addActionListener(this);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }
//    public static void main(String[] a) {
//        AboutDialog dlg = new AboutDialog(new JFrame(), "title", "message");
//    }
}


