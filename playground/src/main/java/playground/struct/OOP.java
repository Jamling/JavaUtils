package playground.struct;

/**
 * 面向对象的三大特性
 * 封装，继承，多态
 */
public class OOP {
    public static void main(String[] args) {
        // int
        Math.abs(0);
        Car car = new Car(); // audi/
        car.color();
        //

        // 重写（Overwrite)，重载(Overload)
        Car audi = new Audi();
        System.out.println(audi.color()); // 2

        // 重写， 实现方法


        // 凡是父类调用的地方，都可以用子类进行代替

        // 设计原则：自上而下
        // prop，mf, 把相同逻辑放父类
        //
        //
    }
}

class Car {
    int wheel;
    int color(){
       return 0;
    }
    int color(int c) {
        return 1;
    }
}

class OilCar extends Car{

}

class Audi extends OilCar implements Drivable {

    @Override
    int color() {
        return 2;
    }

    int color(int y, int z) {
        return super.color(1);
    }

    @Override
    public int driver() {
        return 0;
    }
}

interface Drivable {
    int driver();
}