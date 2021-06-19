package cn.ieclipse.util.swing;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PropConfEditor extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = -3023322634538922011L;
    Color green = new Color(46, 164, 79);
    JTextPane textPane;
    StyledDocument doc;
    private boolean mutex;
    SimpleAttributeSet commentAttr = new SimpleAttributeSet();
    SimpleAttributeSet labelAttr = new SimpleAttributeSet();
    SimpleAttributeSet valueAttr = new SimpleAttributeSet();
    SimpleAttributeSet normalAttr = new SimpleAttributeSet();
    Options options;

    /**
     * Create the panel.
     */
    public PropConfEditor(Options opt) {
        if (opt == null) {
            opt = new Options();
        }
        this.options = opt;
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        textPane = new JTextPane();
        // scrollPane.setPreferredSize(new Dimension(-1, 400));
        scrollPane.setViewportView(textPane);

        doc = textPane.getStyledDocument();
        StyleConstants.setForeground(commentAttr, green);
        StyleConstants.setForeground(labelAttr, Color.RED);
        StyleConstants.setForeground(valueAttr, Color.BLUE);
        StyleConstants.setForeground(normalAttr, Color.BLACK);

        StyleContext sc = StyleContext.getDefaultStyleContext();
        Style style = sc.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontSize(style, 16);
        doc.setLogicalStyle(0, style);

        load();

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        add(buttons, BorderLayout.SOUTH);

        if (options.withSaveBtn) {
            JButton btnOk = new JButton("保存");
            btnOk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    save();
                    parse();
                }
            });
            buttons.add(btnOk, BorderLayout.SOUTH);
        }

        if (options.withRestoreBtn) {
            JButton btnOk = new JButton("还原");
            btnOk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        PropConfigable conf = options.conf.getClass().newInstance();
                        textPane.setText(conf.generatePropertiesContent());
                        parse();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            buttons.add(btnOk, BorderLayout.SOUTH);
        }

        doc.addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update(e);
            }
        });
    }

    private void parse() {
        Element root = doc.getDefaultRootElement();
        int count = root.getElementCount();
        for (int i = 0; i < count; i++) {
            Element e = root.getElement(i);
            deal(e.getStartOffset(), e.getEndOffset());
        }
    }

    private void deal(int startOffset, int endOffset) {
        mutex = true;
        try {
            String text = doc.getText(startOffset, endOffset - startOffset);
            if (text == null || "".equals(text)) {
                return;
            }
            if (text.trim().startsWith("#")) {
                doc.setCharacterAttributes(startOffset, endOffset - startOffset, commentAttr, true);
            } else {
                int pos = text.indexOf("=");
                if (pos >= 0) {
                    if (pos > 0) {
                        doc.setCharacterAttributes(startOffset, pos, labelAttr, true);
                    }
                    if (pos + 1 < endOffset) {
                        doc.setCharacterAttributes(startOffset + pos + 1, endOffset - startOffset - pos - 1, valueAttr,
                                true);
                    }
                } else {
                    doc.setCharacterAttributes(startOffset, endOffset - startOffset, normalAttr, true);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        mutex = false;
    }

    private void update(DocumentEvent e) {
        if (mutex) {
            return;
        }
        Element ele = doc.getParagraphElement(e.getOffset());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                deal(ele.getStartOffset(), ele.getEndOffset());
            }
        });
    }

    public void load() {
        try {
            String text = options.conf.generatePropertiesContent();
            textPane.setText(text);
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            options.conf.saveContent(textPane.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Options {
        public PropConfigable conf;
        public boolean withSaveBtn = true;
        public boolean withRestoreBtn = true;
    }
}
