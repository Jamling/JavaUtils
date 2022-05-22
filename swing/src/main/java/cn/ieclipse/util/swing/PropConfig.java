package cn.ieclipse.util.swing;

import cn.ieclipse.util.ReflectUtils;
import cn.ieclipse.util.swing.annotation.Config;
import cn.ieclipse.util.swing.annotation.ConfigItem;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropConfig {
    private Builder builder;

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return builder;
    }

    public boolean read() throws IOException, ConfigParseException {
        LineNumberReader reader =
                new LineNumberReader(
                        new InputStreamReader(
                                new FileInputStream(builder.getFile()), StandardCharsets.UTF_8));
        return parseConfig(reader);
    }

    private boolean parseConfig(LineNumberReader reader) throws ConfigParseException, IOException {
        List<ConfItemWrapper> metaList = getConfFields(getClass());
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            line = line.trim();
            if (!line.isEmpty()) {
                if (line.startsWith("#")) {
                    continue;
                }
                int pos = line.indexOf("=");
                if (pos > 0) {
                    String name = line.substring(0, pos).trim();
                    String value = line.substring(pos + 1).trim();
                    try {
                        setValue2Bean(metaList, name, value);
                    } catch (ConfigParseException e) {
                        e.lineNumber = reader.getLineNumber();
                    }
                } else {
                    String msg = String.format("wrong config item at line %d: %s", reader.getLineNumber(), line);
                    throw new ConfigParseException(reader.getLineNumber(), msg);
                }
            }
        }
        return true;
    }

    private void setValue2Bean(List<ConfItemWrapper> metaList, String key, String value) throws ConfigParseException {
        Object config = this;
        for (ConfItemWrapper cw : metaList) {
            final String name = cw.confItem.name().isEmpty() ? cw.field.getName() : cw.confItem.name();
            if (name.equalsIgnoreCase(key)) {
                Field field = ReflectUtils.getClassField(getClass(), name, true).get();
                if (field != null) {
                    try {
                        field.setAccessible(true);
                        Object obj = getFieldValue(field, value);
                        field.set(config, obj);
                    } catch (Exception e) {
                        throw new ConfigParseException(String.format("can't set %s from %s", key, value));
                    }
                }
            }
        }
    }

    /**
     * 将内容保存到配置文件，创建的文件使用utf-8编码
     *
     * @param content 配置文件内容
     * @return 是否保存成功
     * @throws IOException IO异常
     * @throws ConfigParseException 配置文件解析异常
     */
    public boolean saveContent(String content) throws IOException, ConfigParseException {
        LineNumberReader reader =
                new LineNumberReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(content.getBytes())));
        parseConfig(reader);

        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(builder.file), StandardCharsets.UTF_8))) {
            if (builder.restrictMode) {
                builder.content = generateContent();
                bufferedWriter.write(builder.content);
            } else {
                bufferedWriter.write(content);
            }
        }
        return true;
    }

    public PropConfig getDefault() {
        return getBuilder().create(getClass());
    }

    public String generateContent() {
        StringBuilder sb = new StringBuilder();
        Config conf = getClass().getAnnotation(Config.class);
        if (conf != null) {
            sb.append('#');
            sb.append(conf.desc());
            sb.append(System.lineSeparator());
        }
        List<ConfItemWrapper> list = getConfFields(getClass());
        for (ConfItemWrapper cw : list) {
            String desc = cw.confItem.desc();
            if (!desc.isEmpty()) {
                sb.append("#");
                sb.append(desc);
                sb.append(System.lineSeparator());
            }
            String name = cw.confItem.name();
            if (name.isEmpty()) {
                name = cw.field.getName();
            }
            String value = "";
            try {
                cw.field.setAccessible(true);
                Object obj = cw.field.get(this);
                if (obj != null) {
                    value = obj.toString();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            sb.append(String.format("%s=%s", name, value));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static Object getFieldValue(Field field, String value) {
        Class<?> paramClass = field.getType();
        Object paramValue = null;
        if (paramClass == int.class || paramClass == Integer.class) {
            paramValue = Integer.parseInt(value);
        } else if (paramClass == byte[].class || paramClass == Byte[].class) {
            paramValue = Byte.parseByte(value);
        } else if (paramClass == short.class || paramClass == Short.class) {
            paramValue = Short.parseShort(value);
        } else if (paramClass == long.class || paramClass == Long.class) {
            paramValue = Long.parseLong(value);
        } else if (paramClass == float.class || paramClass == Float.class) {
            paramValue = Float.parseFloat(value);
        } else if (paramClass == double.class || paramClass == Double.class) {
            paramValue = Double.parseDouble(value);
        } else if (paramClass == boolean.class || paramClass == Boolean.class) {
            paramValue = Boolean.parseBoolean(value);
        } else if (paramClass == String.class) {
            paramValue = value;
        } else if (paramClass == String[].class) {
            paramValue = value.split(",");
        } else if (paramClass == Integer[].class) {
            paramValue = Arrays.stream(value.split(",")).map(str -> Integer.parseInt(str)).toArray(Integer[]::new);
        } else if (paramClass == List.class) {
            Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            value = value.replaceFirst("^\\s*\\[\\s*", "").replaceFirst("\\s*]\\s*$", "");
            String[] array = value.split(",");
            List list = new ArrayList<>();
            Arrays.stream(array).forEach(str -> {
                list.add(type.getClass().cast(str));
            });
        }
        return paramValue;
    }

    private static List<ConfItemWrapper> getConfFields(Class<?> clazz) {
        List<ConfItemWrapper> result = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ConfigItem confItem = field.getAnnotation(ConfigItem.class);
            if (confItem != null) {
                result.add(new ConfItemWrapper(field, confItem));
            }
        }
        return result;
    }


    private static class ConfItemWrapper {
        public Field field;
        public ConfigItem confItem;

        public ConfItemWrapper(Field field, ConfigItem confItem) {
            this.field = field;
            this.confItem = confItem;
        }
    }

    public static class Builder {
        private File file;
        private String content;
        private boolean restrictMode;

        public Builder setFile(File file) {
            this.file = file;
            return this;
        }

        public File getFile() {
            return file;
        }

        public Builder setRestrictMode(boolean restrictMode) {
            this.restrictMode = restrictMode;
            return this;
        }

        public boolean getRestrictMode() {
            return this.restrictMode;
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public <T extends PropConfig> T create(Class<T> clazz) {
            try {
                T t = clazz.newInstance();
                t.setBuilder(this);
                return t;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("can't construct clazz");
            }
        }
    }

    public static class ConfigParseException extends Exception {
        private int lineNumber;

        public ConfigParseException(String message) {
            this(0, message);
        }

        public ConfigParseException(int lineNumber, String message) {
            super(message);
            this.lineNumber = lineNumber;
        }

        public int getLineNumber() {
            return lineNumber;
        }
    }
}
