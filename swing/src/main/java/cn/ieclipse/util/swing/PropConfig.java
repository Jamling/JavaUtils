package cn.ieclipse.util.swing;

import cn.ieclipse.util.swing.annotation.Config;
import cn.ieclipse.util.swing.annotation.ConfigItem;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropConfig {
    Properties properties = new Properties();
    File file;
    String content;
    boolean restrictMode;

    public boolean read() {
        try {
            if (file != null && file.exists()) {
                properties.clear();
                properties.load(new FileInputStream(file));
                getConfFields(getClass()).forEach(cw -> {
                    final String name = cw.confItem.name().isEmpty() ? cw.field.getName() : cw.confItem.name();
                    if (properties.containsKey(name)) {
                        try {
                            cw.field.setAccessible(true);
                            Class<?> paramClass = cw.field.getType();
                            cw.field.set(this, getFieldValue(properties.getProperty(name), paramClass));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("parse " + name + " failed", e);
                        }
                    }
                });
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveContent(String content) {
        try {
            properties.clear();
            properties.load(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            getConfFields(getClass()).forEach(cw -> {
                final String name = cw.confItem.name().isEmpty() ? cw.field.getName() : cw.confItem.name();
                boolean hasConf = properties.containsKey(name);
                String confValue = properties.getProperty(name);
                if (!cw.confItem.empty()) {
                    if (!hasConf || confValue == null || confValue.isEmpty()) {
                        throw new NullPointerException(name + " can't be empty");
                    }
                }
                if (hasConf) {
                    try {
                        cw.field.setAccessible(true);
                        Class<?> paramClass = cw.field.getType();
                        cw.field.set(this, getFieldValue(confValue, paramClass));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("set " + name + " failed", e);
                    }
                }
            });

            FileOutputStream fos = new FileOutputStream(file);
            if (this.restrictMode) {
                this.content = generatePropertiesContent();
                fos.write(this.content.getBytes(StandardCharsets.UTF_8));
            } else {
                fos.write(content.getBytes(StandardCharsets.UTF_8));
            }
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generatePropertiesContent() {
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

    private static Object getFieldValue(String value,
                                        Class<?> paramClass) {
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


    static class ConfItemWrapper {
        public Field field;
        public ConfigItem confItem;

        public ConfItemWrapper(Field field, ConfigItem confItem) {
            this.field = field;
            this.confItem = confItem;
        }
    }
}
