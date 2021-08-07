package cn.ieclipse.util.swing;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                    out.setPrintCallback(new SwingPrintStream.PrintCallback() {
                        @Override
                        public void onPrintBefore(Document document, String msg, int prefixLength) {
                            System.err.println(msg + prefixLength);
                        }
                    });
                    System.setOut(out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}