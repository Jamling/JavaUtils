package cn.ieclipse.util.swing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigItem {
    String name() default "";
    String desc() default "";
    String type() default "String";
    boolean empty() default true;
}
