package playground.tool;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IO implements java.io.Serializable {
    String name = "abc";
    int age = 1;
    transient double weight; // 不进行序

    // 设计模式->包装者
    // 面向字节流
    // 面向字符流
    public static void main(String[] args) {
        InputStream inputStream;
        FileInputStream fis; // 文件
        ByteArrayInputStream bis; // byte[]
        ObjectInputStream ois; // 序列化之后的对象（文件）读取为一个具体的对象
        DataInputStream dis;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(""));
        // 代理

        OutputStream outputStream;
        FileOutputStream fos;


        // prop / mf
        File file;
        FileInputStream is1 = new FileInputStream(new File("a.prop"));
        //
        int b = is1.read();

        Reader; FileReader; StringReader;
        Writer;

        Reader in;
        BufferedReader br = new BufferedReader(new InputStreamReader(is1, StandardCharsets.UTF_8));
    }
}
