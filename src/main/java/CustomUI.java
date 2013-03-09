import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTextFieldUI;

public class CustomUI extends JFrame {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                UIManager.getDefaults().put("TextFieldUI", CustomTextFieldUI.class.getName());
                new CustomUI().setVisible(true);
            }

        });
    }

    public CustomUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        add(new JTextField(10));
        add(new JTextField(10));
        pack();
    }

    public static class CustomTextFieldUI extends MetalTextFieldUI implements FocusListener {

        public static ComponentUI createUI(JComponent c) {
            return new CustomTextFieldUI();
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.addFocusListener(this);
        }

        public void focusGained(FocusEvent e) {
            getComponent().setBackground(Color.YELLOW.brighter());
        }

        public void focusLost(FocusEvent e) {
            getComponent().setBackground(UIManager.getColor("TextField.background"));
        }

    }

}