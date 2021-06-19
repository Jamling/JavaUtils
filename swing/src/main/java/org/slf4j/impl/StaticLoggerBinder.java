package org.slf4j.impl;

import cn.ieclipse.util.swing.SwingLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {
    /** The unique instance of this class. */
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    /**
     * Return the singleton of this class.
     *
     * @return the StaticLoggerBinder singleton
     */
    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return new SwingLoggerFactory();
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return SwingLoggerFactory.class.getName();
    }
}
