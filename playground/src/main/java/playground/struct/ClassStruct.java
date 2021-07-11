// 包名可选
package playground.struct;
// 可选的：import

// 类定义，必选

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文档注释
 *
 * @author Jamling
 * @version 2021-07-11
 */
public class ClassStruct {
    // 类属性,field
    private String name;
    private int age;

    // 方法区 member
    public static void main(String[] args){
        int x = 1;
        if (x > 10) {

        } else if (x > 5 ) {

        } else {

        }
        // 先判断再执行
        while( x > 0) {

        }

        switch (x) {
            case 1:
                break;
            case 2:
                break;
            case 3:
            case 4:
                break;
            default:
                break;
        }

        // 先执行，再判断
        do {

        } while (x >0);

        for(int i=0;i<10;i++) {

        }
        for (int i=0;;) {
            if (i > 0) {
                break;
            }
        }

        Error error;
        Throwable throwable;

        ClassStruct instance = new ClassStruct();
        instance.add(1,2);

        try {
            instance.devider(1, 2);
            int y = 0;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        //

        System.out.println("Hello Java");
    }

    /*
    public void fun1() {

    }*/

    /**
     * 加法
     * @param x 加数
     * @param y 被加数
     * @return 和
     */
    public int add(int x, int y) throws NullPointerException , IndexOutOfBoundsException, IllegalArgumentException {
        if (x < 0 ) {
            // 行内注释

        }
        return x+y;
    }

    public int devider(int x,int y) throws IOException {
        FileInputStream fis = new FileInputStream("test.file");
        fis.close();
        return 0;
    }
}
