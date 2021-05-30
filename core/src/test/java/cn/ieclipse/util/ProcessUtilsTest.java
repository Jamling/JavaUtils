package cn.ieclipse.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import junit.framework.TestSuite;

public class ProcessUtilsTest extends TestSuite {
    
    @Test
    public void test() {
        Process p = ProcessUtils.exec("cmd", "/c", "dir", "C:\\Users");
        assertNotNull(p);
        ProcessUtils.ExecResult ret = ProcessUtils.getResult(p, null);
        System.out.println(ret.getResults());
        assertEquals(true, ret.isSuccess());
        
        String[] args = "-i 2017-10-22_100913.mp4 1.mp4".split(" ");
        List<String> list = new ArrayList<>();
        p = ProcessUtils.exec("ffmpeg", Arrays.asList(args),
                new File("G:\\2017"));
        ProcessUtils.handleProcess(p, null, new ProcessUtils.ProcessHandler() {
            
            @Override
            public void info(String line) {
                System.out.println(line);
            }
            
            @Override
            public void error(String line) {
                System.err.println(line);
            }
        });
    }
}
