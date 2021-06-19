package cn.ieclipse.util.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Created by Jamling on 2017/7/3.
 */
public class ClosableTabHost extends JTabbedPane implements ChangeListener {
    private Insets insets = new Insets(0, 0, 0, 0);
    private Callback callback;
    
    public ClosableTabHost() {
        super(JTabbedPane.TOP);
        addChangeListener(this);
    }
    
    public ClosableTabHost(Callback callback) {
        this();
        setCallback(callback);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    
    @Override
    public Border getBorder() {
        return BorderFactory.createEmptyBorder();
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        int index = getSelectedIndex();
        if (index >= 0) {
            setBackgroundAt(index, null);
        }
    }
    
    @Override
    public void insertTab(String title, Icon icon, Component component,
            String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        setTabComponentAt(index, createTab(title));
    }
    
    public Component createTab(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(text);
        label.setOpaque(false);
        panel.add(label);
        TabButton btn = new TabButton();
        panel.add(btn);
        panel.setBackground(Color.black);
        panel.setOpaque(false);
        return panel;
    }
    
    public void bling(final int index, String name) {
        if (index >= 0 && index < getTabCount()
                && index != getSelectedIndex()) {
            Component tab = getTabComponentAt(index);
            if (tab != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setBackgroundAt(index,
                                UIManager.getColor("TabbedPane.selected"));
                    }
                });
                new BlingTimer(tab, name).start();
            }
        }
    }
    
    private class BlingTimer extends Timer implements ActionListener {
        Component component;
        JLabel label;
        String src;
        Dimension d;
        int count = 4;
        
        public BlingTimer(Component tab, String name) {
            super(300, null);
            addActionListener(this);
            component = tab;
            if (component != null && component instanceof JPanel) {
                label = (JLabel) ((JPanel) component).getComponent(0);
                src = label.getText();
                if (src == null || src.isEmpty()) {
                    src = name;
                }
                d = label.getSize();
                label.setPreferredSize(d);
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                label.setText(count % 2 == 0 ? "" : src);
                count--;
                if (count <= 0) {
                    stop();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        @Override
        public void stop() {
            label.setText(src);
            super.stop();
        }
    }
    
    private class TabButton extends JButton implements ActionListener {
        public TabButton() {
            // super(SmartIcons.close);
            int size = 16;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close");
            // Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            setOpaque(false);
            // Make it transparent
            setContentAreaFilled(false);
            // No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEmptyBorder());
            setBorderPainted(false);
            // Making nice rollover effect
            // we use the same listener for all buttons
            // addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            // Close the proper tab by clicking the button
            addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent e) {
            int i = indexOfTabComponent(getParent());
            if (i != -1) {
                if (callback != null) {
                    callback.removeTabAt(i);
                } else {
                    removeTabAt(i);
                }
            }
        }
        
        // we don't want to update UI for this button
        public void updateUI() {
        }
        
        // paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            // shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.GRAY);
            if (getModel().isRollover()) {
                g2.setColor(Color.LIGHT_GRAY);
            }
            int delta = 5;
            g2.drawLine(delta, delta, getWidth() - delta, getHeight() - delta);
            g2.drawLine(getWidth() - delta, delta, delta, getHeight() - delta);
            g2.dispose();
        }
    }
    
    public interface Callback {
        void removeTabAt(int index);
    }
    
    public interface Closeable {
    	void onClose(Component c);
    }
}
