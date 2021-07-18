package playground.tool;

import cn.ieclipse.util.FileUtils;
import cn.ieclipse.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.TreeMap;

public abstract class ConfigParser {

    protected TreeMap configs = new TreeMap<>();

    public void parser(File file) throws IOException {
        LineNumberReader reader =
                new LineNumberReader(
                        new InputStreamReader(
                                new FileInputStream(file), StandardCharsets.UTF_8));
        String line = reader.readLine();
        try {
            while (line != null) {
                if (!isComment(line)) {
                    parseLine(line);
                }
                line = reader.readLine();
            }
        } catch (IllegalArgumentException e) {
            System.err.println(String.format("parser error in line %d", reader.getLineNumber()));
            throw e;
        }
    }

    protected abstract boolean isComment(String line);

    protected abstract char getSeparator();

    protected void parseLine(String line) {
        int pos = line.indexOf(getSeparator());
        if (pos > 0) {
            String name = line.substring(0, pos);
            String value = line.substring(pos + 1);
            configs.put(name, value);
        } else {
            throw new IllegalArgumentException("wrong format " + line);
        }
    }

    public static class PropParser extends ConfigParser {

        @Override
        protected boolean isComment(String line) {
            return line.startsWith("#");
        }

        @Override
        protected char getSeparator() {
            return '=';
        }
    }

    public static class MfParser extends ConfigParser {

        @Override
        protected boolean isComment(String line) {
            return line.startsWith("//");
        }

        @Override
        protected char getSeparator() {
            return ':';
        }
    }

    public static class ConfigParserFactory {
        private static ConfigParserFactory instance;

        private ConfigParserFactory() {
        }

        public static ConfigParserFactory getInstance() {
            if (instance == null) {
                synchronized (ConfigParserFactory.class) {
                    instance = new ConfigParserFactory();
                }
            }
            return instance;
        }

        public ConfigParser getParser(String name) {
            if (StringUtils.isEmpty(name)) {
                return null;
            }
            if ("prop".equalsIgnoreCase(name)) {
                return new PropParser();
            } else if ("mf".equalsIgnoreCase(name)) {
                return new MfParser();
            }
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        Arrays.asList("sample.prop", "sample.mf").forEach(file -> {
            ConfigParser parser = ConfigParserFactory.getInstance().getParser(FileUtils.getExtension(file));
            if (parser != null) {
                try {
                    parser.parser(new File(file));
                    System.out.println(parser.configs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
