package cn.ieclipse.util.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.PrintStream;

import static cn.ieclipse.util.swing.SwingLogger.*;

public class SwingInternalLogger {

    private Document document;
    private SwingPrintStream swingPrintStream;
    private static SwingInternalLogger instance = null;
    private static boolean isRedirectSystemOut;
    private static boolean isRedirectSystemErr;

    SimpleAttributeSet infoAttr = new SimpleAttributeSet();
    SimpleAttributeSet warnAttr = new SimpleAttributeSet();
    SimpleAttributeSet errorAttr = new SimpleAttributeSet();

    public static SwingInternalLogger getInstance() {
        return instance;
    }

    public static void init(String name, Document document) {
        instance = new SwingInternalLogger(name, document);
    }

    public SwingInternalLogger(String name, Document document) {
        this.document = document;
        swingPrintStream = new SwingPrintStream(System.out, null);
        swingPrintStream.setDocument(document);
        StyleConstants.setForeground(errorAttr, Color.RED);
        StyleConstants.setForeground(warnAttr, Color.ORANGE);
    }

    public Document getDocument() {
        return document;
    }

    public PrintStream getPrintStream() {
        return swingPrintStream;
    }

    public void setPrintCallback(SwingPrintStream.PrintCallback printCallback) {
        swingPrintStream.setPrintCallback(printCallback);
    }

    private AttributeSet getAttributeForLevel(int level) {
        if (level >= LOG_LEVEL_ERROR) {
            return errorAttr;
        } else if (level >= LOG_LEVEL_WARN) {
            return warnAttr;
        }
        return infoAttr;
    }

    void write(StringBuilder buf, Throwable t, int level, int prefixLength) {
        if (swingPrintStream != null) {
            swingPrintStream.setPrefixLength(prefixLength);
            swingPrintStream.setAttributeSet(getAttributeForLevel(level));
        }
        PrintStream targetStream = swingPrintStream;

        targetStream.print(buf.toString());
        if (t != null) {
            t.printStackTrace(targetStream);
        }
        targetStream.flush();
    }

    public static void redirectSystemOut() {
        System.setOut(new RedirectSystemPrintStream(LOG_LEVEL_INFO));
        isRedirectSystemOut = true;
    }

    public static void redirectSystemErr() {
        System.setOut(new RedirectSystemPrintStream(LOG_LEVEL_ERROR));
        isRedirectSystemErr = true;
    }

    private static class RedirectSystemPrintStream extends PrintStream {
        private static Logger logger = LoggerFactory.getLogger("System.out");
        ;
        private int level;

        public RedirectSystemPrintStream(int level) {
            super(System.out);
            this.level = level;
        }

        @Override
        public void println(String msg) {
            printInternal(msg);
        }

        @Override
        public void print(String s) {
            printInternal(s);
        }

        @Override
        public void println(Object x) {
            if (x == null) {
                printInternal(null);
            } else {
                printInternal(x.toString());
            }
        }

        @Override
        public void print(Object x) {
            if (x == null) {
                printInternal(null);
            } else {
                printInternal(x.toString());
            }
        }

        @Override
        public void println(int x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void print(int x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void println(char x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void print(char x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void println(boolean x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void print(boolean x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void println(float x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void print(float x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void println(double x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void print(double x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void println(long x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void print(long x) {
            printInternal(String.valueOf(x));
        }

        @Override
        public void println(char[] x) {
            printInternal(new String(x));
        }

        @Override
        public void print(char[] x) {
            printInternal(new String(x));
        }

        protected void printInternal(String s) {
            if (this.level >= LOG_LEVEL_ERROR) {
                logger.error(s);
            } else {
                logger.debug(s);
            }
        }
    }
}
