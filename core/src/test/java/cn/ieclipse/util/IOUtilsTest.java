package cn.ieclipse.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class IOUtilsTest {
    
    String abc = "简体中文Abc";
    
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testClose() {
        InputStream fis;
        fis = ClassLoader.getSystemResourceAsStream("test.txt");
        IOUtils.close(fis);
    }
    
    @Test
    public void testCopyStreamInputStreamOutputStream() {
        InputStream in = ClassLoader.getSystemResourceAsStream("test.txt");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            IOUtils.copyStream(in, bos);
            String c = bos.toString("utf-8");
            assertEquals(abc, c);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        
    }
    
    @Test
    public void testReadSocketStream() {
        String url = "http://www.baidu.com";
        try {
            InputStream is = new URL(url).openConnection().getInputStream();
            ByteArrayInputStream bis = IOUtils.readSocketStream(is);
            assertTrue("response is not empty", bis.available() > 0);
            String line = "<!DOCTYPE html>";
            byte[] b = new byte[line.length()];
            bis.read(b);
            assertArrayEquals(line.getBytes(), b);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testRead2Byte() {
        InputStream in = ClassLoader.getSystemResourceAsStream("test.txt");
        byte[] actual = IOUtils.read2Byte(in);
        assertArrayEquals(abc.getBytes(), actual);
    }
    
}
