package cn.ieclipse.util.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class ConsolePanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 5917974494015943419L;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(ConsolePanel.class.getSimpleName());
    private JTextComponent text;
    private Options options;

    /**
     * Create the panel.
     */
    public ConsolePanel(Options options) {
        this.options = options;
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        text = new JTextPane();
        text.setEditable(options.editable);
        initLogger(text.getDocument());
        scrollPane.setViewportView(text);

        if (options.menuClearName != null && options.menuClearName.length() > 0) {
            JPopupMenu popupMenu = new JPopupMenu();
            addPopup(text, popupMenu);

            JMenuItem miClear = new JMenuItem(options.menuClearName);
            miClear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    text.setText(null);
                }
            });
            popupMenu.add(miClear);
        }
    }

    public ConsolePanel() {
        this(new Options());
    }

    private void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    private void initLogger(Document doc) {
        System.out.println("init SwingLogger");
        StyleContext sc = StyleContext.getDefaultStyleContext();
        Style style = sc.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontSize(style, 16);
        if (doc instanceof StyledDocument) {
            ((StyledDocument) doc).setLogicalStyle(0, style);
        }

        SwingInternalLogger.init("console", doc);
        if (options.redirectSystemOut) {
            SwingInternalLogger.redirectSystemOut();
        }
        if (options.redirectSystemErr) {
            //System.setErr(swingLogger.getPrintStream());
        }

    }

    public void setPrintCallback(SwingPrintStream.PrintCallback callback) {
        if (SwingInternalLogger.getInstance() != null) {
            SwingInternalLogger.getInstance().setPrintCallback(callback);
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public static class Options {
        public boolean redirectSystemOut = true;
        public boolean redirectSystemErr = true;
        public String menuClearName = "Clear";
        public boolean editable = false;
    }
}
