package playground.jdk5;

import java.util.ArrayList;
import java.util.List;

public class GeneralType {
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

    static class A{

    }
    static class B extends A {

    }
    static class C extends B {

    }

    static class Adapter1<T> {
        protected List<T> list;
    }


}
