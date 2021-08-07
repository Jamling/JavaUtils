package playground.jdk8;

public class Lambada {

    public static void main(String[] args) {
        Listener listener = ()-> {System.out.println("onCallback");};
        System.out.println(listener.getClass());
    }

    interface Listener {
        void onCallback();
    }
}
