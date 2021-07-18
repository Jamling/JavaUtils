/*
 * 文件注释，一般为版权信息，可使用IDE自动生成
 */
// 包名可选（不推荐）
package playground.basic;
// 可选的：import

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

// 类定义，必选

/**
 * 类注释，属于文件注释
 *
 * @author Jamling
 * @version 2021-07-11
 */
public/*修饰符:public|protected|default|private*/ /**/ class ClassStruct /* extend OneClass */ /*implements A,B...*/ {
    // 类属性,field
    private String name;
    private int age;

    // 方法区 member
    public static void main(String[] args) {
        int x = 1;
        // 条件
        if (x > 10) {

        } else if (x > 5) {

        } else {

        }

        // switch条件
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

        // 循环：先判断再执行
        while (x > 0) {

        }

        // 先执行，再判断
        do {

        } while (x > 0);

        for (int i = 0; i < 10; i++) {

        }
        // for
        for (int i = 0; ; ) {
            if (i > 0) {
                break;
            }
        }

        Error error; // 不可恢复, 不可捕捉
        Exception exception; // 可捕捉
        RuntimeException runtimeException;// 可不捕捉，编译不报错

        ClassStruct instance = new ClassStruct();
        instance.add(1, 2);

        ClassStruct.main(null);

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
     *
     * @param x 加数
     * @param y 被加数
     * @return 和
     */
    public int add(int x, int y) throws NullPointerException, IndexOutOfBoundsException, IllegalArgumentException {
        if (x < 0) {
            // 行内注释

        }
        return x + y;
    }

    public int devider(int x, int y) throws IOException {
        FileInputStream fis = new FileInputStream("test.file");
        fis.close();
        return 0;
    }

    private void innerAnnoClass() {
        JButton btn = new JButton();

        // ClassStruct$1
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
            }
        });
        // lambda : ClassStruct$x3d9x3
        Arrays.asList("1").forEach(s -> {

        });

    }

    // ClassStruct$StaticInnerClass.class
    public static class StaticInnerClass {

    }
    // ClassStruct$InnerClass1

    public class InnerClass1 {

    }

    // ClassStruct$InnerClass2
    public class InnerClass2 {

    }
}
