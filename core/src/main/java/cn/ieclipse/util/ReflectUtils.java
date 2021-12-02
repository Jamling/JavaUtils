package cn.ieclipse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ReflectUtils {
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
    private ReflectUtils() {
    }

    public static Optional<Class> forName(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            logger.error("{} not found", className);
        }
        return Optional.empty();
    }

    public static List<Field> getClassField(Class<?> clazz) {
        return getClassField(clazz, true, null);
    }

    public static Optional<Field> getClassField(Class<?> clazz, String fieldName, boolean searchSupper) {
        List<Field> fields = getClassField(clazz, searchSupper, f -> f.getName().equals(fieldName));
        if (fields.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(fields.get(0));
    }

    public static List<Field> getClassField(Class<?> clazz, FieldFilter filter) {
        return getClassField(clazz, true, filter);
    }

    public static List<Field> getClassField(Class<?> clazz, boolean searchSuper, FieldFilter filter) {
        return getClassField(clazz, true, searchSuper, filter);
    }

    public static List<Field> getClassField(Class<?> clazz, boolean declared, boolean searchSuper, FieldFilter filter) {
        List<Field> list = new ArrayList<>();
        List<String> names = new ArrayList<>();
        getClassField(clazz, list, names, declared, searchSuper, filter);
        return list;
    }

    private static void getClassField(Class<?> clazz, List<Field> list, List<String> names, boolean declared,
        boolean searchSuper, FieldFilter filter) {
        if (clazz != null && !clazz.equals(Object.class)) {
            Field[] fields = declared ? clazz.getDeclaredFields() : clazz.getFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (!names.contains(field.getName())) {
                        if (filter == null || filter.accept(field)) {
                            list.add(field);
                            names.add(field.getName());
                        }
                    }
                }
            }
            if (searchSuper) {
                getClassField(clazz.getSuperclass(), list, names, declared, true, filter);
            }
        }
    }

    public static Object get(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            logger.error("get field({}) failed", field.getName());
        }
        return Optional.empty();
    }

    public interface FieldFilter {
        boolean accept(Field f);
    }

    public interface MethodFilter {
        boolean accept(Method m);
    }

    public static List<Method> getClassMethod(Class<?> clazz) {
        return getClassMethod(clazz, true, null);
    }

    public static List<Method> getClassMethod(Class<?> clazz, MethodFilter filter) {
        return getClassMethod(clazz, true, filter);
    }

    public static List<Method> getClassMethod(Class<?> clazz, boolean searchSuper, MethodFilter filter) {
        return getClassMethod(clazz, true, searchSuper, filter);
    }

    public static List<Method> getClassMethod(Class<?> clazz, boolean declared, boolean searchSuper,
        MethodFilter filter) {
        List<Method> list = new ArrayList<>();
        List<String> names = new ArrayList<>();
        getClassMethod(clazz, list, names, declared, searchSuper, filter);
        return list;
    }

    private static void getClassMethod(Class<?> clazz, List<Method> list, List<String> names, boolean declared,
        boolean searchSuper, MethodFilter filter) {
        if (clazz != null && !clazz.equals(Object.class)) {
            Method[] methods = declared ? clazz.getDeclaredMethods() : clazz.getMethods();
            if (methods != null) {
                for (Method method : methods) {
                    if (!names.contains(method.getName())) {
                        if (filter == null || filter.accept(method)) {
                            list.add(method);
                            names.add(method.getName());
                        }
                    }
                }
            }
            if (searchSuper) {
                getClassMethod(clazz.getSuperclass(), list, names, declared, true, filter);
            }
        }
    }

    public static Object get(Method m, Object obj, Object... args) {
        m.setAccessible(true);
        try {
            return m.invoke(obj, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error("method {} call failed", m.getName());
        }
        return null;
    }

    public static <T> Optional<T> cast(Class<T> resultClass, Object result) {
        if (resultClass == null || result == null) {
            return Optional.empty();
        }
        if (resultClass.isAssignableFrom(result.getClass())) {
            return Optional.of(resultClass.cast(result));
        }
        return Optional.empty();
    }
}
