/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com.
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.util.swing;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger for log runtime information.
 *
 * @author melord
 * @version 1.0
 */
public class SwingWrapperLogger extends MarkerIgnoringBase {
    protected static final int LOG_LEVEL_TRACE = LocationAwareLogger.TRACE_INT;
    protected static final int LOG_LEVEL_DEBUG = LocationAwareLogger.DEBUG_INT;
    protected static final int LOG_LEVEL_INFO = LocationAwareLogger.INFO_INT;
    protected static final int LOG_LEVEL_WARN = LocationAwareLogger.WARN_INT;
    protected static final int LOG_LEVEL_ERROR = LocationAwareLogger.ERROR_INT;
    private static int currentLogLevel;

    @Override
    public boolean isTraceEnabled() {
        return isLevelEnabled(LOG_LEVEL_TRACE);
    }

    @Override
    public void trace(String msg) {
        log(LOG_LEVEL_TRACE, msg, null);
    }

    @Override
    public void trace(String format, Object arg) {
        formatAndLog(LOG_LEVEL_TRACE, format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_TRACE, format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_TRACE, format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(LOG_LEVEL_TRACE, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(LOG_LEVEL_DEBUG);
    }

    @Override
    public void debug(String msg) {
        log(LOG_LEVEL_DEBUG, msg, null);
    }

    @Override
    public void debug(String format, Object arg) {
        formatAndLog(LOG_LEVEL_DEBUG, format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_DEBUG, format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_DEBUG, format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(LOG_LEVEL_DEBUG, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(LOG_LEVEL_INFO);
    }

    @Override
    public void info(String msg) {
        log(LOG_LEVEL_INFO, msg, null);
    }

    @Override
    public void info(String format, Object arg) {
        formatAndLog(LOG_LEVEL_INFO, format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_INFO, format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_INFO, format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        log(LOG_LEVEL_INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(LOG_LEVEL_WARN);
    }

    @Override
    public void warn(String msg) {
        log(LOG_LEVEL_WARN, msg, null);
    }

    @Override
    public void warn(String format, Object arg) {
        formatAndLog(LOG_LEVEL_WARN, format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_WARN, format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_WARN, format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(LOG_LEVEL_WARN, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(LOG_LEVEL_ERROR);
    }

    @Override
    public void error(String msg) {
        log(LOG_LEVEL_ERROR, msg, null);
    }

    @Override
    public void error(String format, Object arg) {
        formatAndLog(LOG_LEVEL_ERROR, format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_ERROR, format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        formatAndLog(LOG_LEVEL_ERROR, format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(LOG_LEVEL_ERROR, msg, t);
    }

    private void log(int level, String message, Throwable t) {
        if (!isLevelEnabled(level)) {
            return;
        }

        StringBuilder buf = new StringBuilder(32);

        // Append date-time if so configured
        buf.append(dateTimeFormatter.format(LocalDateTime.now()));
        buf.append(' ');

        if (document == null) {
            buf.append(name);
            buf.append(' ');
        }

        // Append current thread name if so configured
        if (false) {
            buf.append('[');
            buf.append(Thread.currentThread().getName());
            buf.append("] ");
        }

        int prefixLength = buf.length();
        // Append the message
        buf.append(message);
        buf.append(System.lineSeparator());

        write(buf, t, level, prefixLength);
    }

    /**
     * For formatted messages, first substitute arguments and then log.
     *
     * @param level
     * @param format
     * @param arg1
     * @param arg2
     */
    private void formatAndLog(int level, String format, Object arg1, Object arg2) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    /**
     * For formatted messages, first substitute arguments and then log.
     *
     * @param level
     * @param format
     * @param arguments a list of 3 ore more arguments
     */
    private void formatAndLog(int level, String format, Object... arguments) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    /**
     * Is the given log level currently enabled?
     *
     * @param logLevel is this level enabled?
     */
    protected boolean isLevelEnabled(int logLevel) {
        // log level are numerically ordered so can use simple numeric
        // comparison
        return (logLevel >= currentLogLevel);
    }

    void write(StringBuilder buf, Throwable t, int level, int prefixLength) {
        if (swingPrintStream != null) {
            swingPrintStream.setPrefixLength(prefixLength);
            swingPrintStream.setAttributeSet(getAttributeForLevel(level));
        }
        PrintStream targetStream = swingPrintStream;

        targetStream.print(buf.toString());
        writeThrowable(t, targetStream);
        targetStream.flush();
    }

    protected void writeThrowable(Throwable t, PrintStream targetStream) {
        if (t != null) {
            t.printStackTrace(targetStream);
        }
    }

    private Document document;
    private SwingPrintStream swingPrintStream;
    private String name;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public SwingWrapperLogger(String name) {
        this.name = name;
        swingPrintStream = new SwingPrintStream(System.out, null);
    }

    public void setDocument(Document document) {
        this.document = document;
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
        }
        else if (level >= LOG_LEVEL_WARN) {
            return warnAttr;
        }
        return infoAttr;
    }
    SimpleAttributeSet infoAttr = new SimpleAttributeSet();
    SimpleAttributeSet warnAttr = new SimpleAttributeSet();
    SimpleAttributeSet errorAttr = new SimpleAttributeSet();

    public void redirectSysout() {
        System.setOut(new RedirectSystemPrintStream(this));
    }
    private static class RedirectSystemPrintStream extends PrintStream {
        private SwingWrapperLogger logger;
        public RedirectSystemPrintStream(SwingWrapperLogger logger) {
            super(logger.getPrintStream());
            this.logger = logger;
        }

        @Override
        public void println(String msg) {
            logger.info(msg);
        }

        @Override
        public void print(String s) {
            logger.info(s);
        }

        @Override
        public void println(Object x) {
            if (x == null) {
                logger.info(null);
            } else {
                logger.info(x.toString());
            }
        }

        @Override
        public void print(Object obj) {
            println(obj);
        }
    }
}