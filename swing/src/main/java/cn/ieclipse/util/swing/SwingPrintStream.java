package cn.ieclipse.util.swing;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class SwingPrintStream extends PrintStream {
    private Document document;
    private AttributeSet attributeSet;

    public SwingPrintStream(OutputStream out, Document document) {
        super(out);
        this.document = document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void setAttributeSet(AttributeSet attributeSet) {
        this.attributeSet = attributeSet;
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        if (document != null) {
            String msg = new String(buf, off, len, StandardCharsets.UTF_8);
            if (SwingUtilities.isEventDispatchThread()) {
                write2Document(msg);
            } else {
                SwingUtilities.invokeLater(() -> write2Document(msg));
            }
            return;
        }
        super.write(buf, off, len);
    }

    private void write2Document(String msg) {
        try {
            if (printCallback != null) {
                printCallback.onPrintBefore(document, msg, prefixLength);
            }
            int offset = document.getLength();
            System.err.println("len:" + offset);
            document.insertString(offset, msg, attributeSet);
        } catch (BadLocationException e) {
            // TODO the e will be printed to this?
            e.printStackTrace();
        }
    }

    private int prefixLength;
    public void setPrefixLength(int prefixLength) {
        this.prefixLength = prefixLength;
    }

    private PrintCallback printCallback;

    public void setPrintCallback(PrintCallback printCallback) {
        this.printCallback = printCallback;
    }

    public interface PrintCallback {
        void onPrintBefore(Document document, String msg, int prefixLength);
    }
}
