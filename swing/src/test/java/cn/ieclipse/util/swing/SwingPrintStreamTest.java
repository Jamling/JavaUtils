package cn.ieclipse.util.swing;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SwingPrintStreamTest {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame frame = new JFrame();
                    frame.setTitle("SwingPrintStreamTest");
                    frame.setBounds(100, 100, 450, 300);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    JTextPane jTextPane = new JTextPane();
                    frame.getContentPane().add(jTextPane, BorderLayout.CENTER);
                    JButton btn = new JButton("System.out");
                    frame.add(btn, BorderLayout.SOUTH);
                    btn.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("简体中文ABC");
                        }
                    });
                    frame.setVisible(true);
                    SwingPrintStream out = new SwingPrintStream(System.out, jTextPane.getDocument());
                    System.setErr(out);
                    System.setOut(out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}