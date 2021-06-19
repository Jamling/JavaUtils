package cn.ieclipse.util.swing;

import org.slf4j.ILoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SwingLoggerFactory implements ILoggerFactory {
    Map<String, SwingLogger> loggerMap = new HashMap<>();

    @Override
    public SwingLogger getLogger(String name) {
        SwingLogger logger = loggerMap.get(name);
        if (logger == null) {
            synchronized (SwingLoggerFactory.class) {
                logger = new SwingLogger(name);
                loggerMap.put(name, logger);
            }
        }
        return logger;
    }
}
