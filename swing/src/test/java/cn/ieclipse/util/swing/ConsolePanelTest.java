package cn.ieclipse.util.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ConsolePanelTest {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsolePanelTest();
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ConsolePanelTest() {
        JFrame frame = new JFrame();
        frame.setTitle("PropConfEditorTest");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("new ConsolePanel");
        ConsolePanel.Options options = new ConsolePanel.Options();
        options.redirectSystemErr = false;
        ConsolePanel consolePanel = new ConsolePanel(options);
        frame.getContentPane().add(consolePanel, BorderLayout.CENTER);
        JButton btn = new JButton("Test");
        frame.add(btn, BorderLayout.SOUTH);
        btn.addActionListener((e) -> {
            Logger logger = consolePanel.getLogger();
            System.out.println("test");
            //new NullPointerException("test NPE to System.err").printStackTrace(System.err);
            logger.info("info");
            logger.error("NPE", new NullPointerException("xx can't be null"));
            logger.warn("warning");

            new ProgressThread().start();
        });
        frame.setVisible(true);

        consolePanel.setPrintCallback(callback);
    }


    private class ProgressThread extends Thread {
        @Override
        public void run() {
            Logger logger = LoggerFactory.getLogger("progess thread");
            logger.info("start");
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("progress " + i);
            }
            System.out.println("Done");
        }
    }

    private SwingPrintStream.PrintCallback callback = new SwingPrintStream.PrintCallback() {
        private String last;

        @Override
        public void onPrintBefore(Document document, String msg, int prefixLength) {
            System.err.println("len:" + prefixLength + " msg " + msg);
            if (msg.length() > prefixLength) {
                String content = msg.substring(prefixLength);
                if (content.startsWith("progress")) {
                    if (last != null && last.length() > 0) {
                        try {
                            System.err.println(document.getLength());
                            System.err.println(last.length());
                            int offs = document.getLength() - last.length();
                            System.err.println("remove:" + document.getText(offs, last.length()));
                            document.remove(offs, last.length());
                            System.err.println(document.getLength());
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    }
                    last = msg;
                }
            }
        }
    };
}
