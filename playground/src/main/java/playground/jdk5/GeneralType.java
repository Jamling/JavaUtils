package playground.jdk5;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralType {

    List<String> listField;
    String stringField;
    String[] arrayField;
    public static void main(String[] args) throws Exception {
        System.out.println("string Field==>");
        printFieldInfo("stringField");
        System.out.println("list Field==>");
        printFieldInfo("listField");
        System.out.println("array Field==>");
        printFieldInfo("arrayField");

        Class<?> clazz = java.lang.Integer[].class;
        System.out.println(clazz.getComponentType());
        System.out.println(Arrays.toString(Arrays.stream("1,2,3".split(",")).toArray()));
    }

    // 泛型边界
    public void bound() {
        List<? super B> list = new ArrayList<>();
        //list.add(new A());
        list.add(new B());
        list.add(new C());
        Object a = list.get(0);
        //super的意义在于放大模板方法接受的泛型参数类型，同时提供向这个泛型写入的合法性。
        //extends的意义也是在于放大模板方法接受的泛型参数类型，牺牲向泛型写入的可能性，提供保证读取出的类型有具体类型的便利性。



        List<? extends B> list2 = new ArrayList<>();
        list2 = (List<? extends B>)list;
        //list2.add(new A());
        //list2.add(new B());
        //list2.add(new C());
        A a2 = list2.get(0);
        B b2 = list2.get(0);
        //C c2 = list2.get(0);


        Adapter1<? super B> adapter1 = new Adapter1<>();
        adapter1.list.add(new B());
        adapter1.list.add(new C());
        //adapter1.list.add(new A());
        Object o = adapter1.list.get(0);

        Adapter1<? extends B> adapter2 = new Adapter1<>();
        //adapter2.list.add(new B());
        //adapter2.list.add(new A());
        A b  = adapter2.list.get(0);
    }

    private static void printFieldInfo(String name) throws Exception {
        Field field = GeneralType.class.getDeclaredField(name);
        Type type = field.getType();
        System.out.println("type class: " + type.getClass());
        System.out.println("type name: " +type.getTypeName());
        Type superType = type.getClass().getGenericSuperclass();
        System.out.println("super type: " + superType);

        Type generalType = field.getGenericType();
        System.out.println("general type class: " + generalType.getClass());
        System.out.println("general type name: " +generalType.getTypeName());
        if (generalType instanceof ParameterizedType) {
            System.out.println("is ParameterizedType");
            ParameterizedType parameterizedType = (ParameterizedType) generalType;
            System.out.println("raw: " + parameterizedType.getRawType());
            System.out.println("owner: " + parameterizedType.getOwnerType());
            System.out.println("arguments: ");
            Arrays.stream(parameterizedType.getActualTypeArguments()).forEach(System.out::println);
        }
        // wrong judge
        else if (generalType instanceof GenericArrayType) {
            System.out.println("is GenericArrayType");
            GenericArrayType arrayType = GenericArrayType.class.cast(generalType);
            System.out.println("array item type: " + arrayType.getGenericComponentType());
        }
    }

    static class A {

    }
    static class B extends A {

    }
    static class C extends B {

    }

    static class Adapter1<T> {
        protected List<T> list;
    }
}
