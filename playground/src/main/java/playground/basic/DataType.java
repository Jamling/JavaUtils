package playground.basic;

import java.util.Arrays;

public class DataType {
    // 基本数据类型
    private Integer intVal1;
    private int intVal2;

    // 数组
    private String[] strArray1; // right define
    private String strArray2[]; // bad define


    private static void printArray() {
        String[] array = {"1,2,3"};
        System.out.println(Arrays.toString(array));

        Arrays.stream(array).forEach(System.out::print);
    }

    public static void main(String[] args) {
        Arrays.asList(
                Integer.valueOf("1"),
                Float.valueOf("1"),
                Double.valueOf("1"),
                String.valueOf("1")
        ).stream().forEach(System.out::println);

        printArray();
    }
}
