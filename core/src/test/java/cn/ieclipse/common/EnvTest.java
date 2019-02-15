package cn.ieclipse.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EnvTest {
    
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void test() {
        if (Env.isWindows()) {
            assertEquals("\r\n", Env.LF);
        }
        Env.dumpEnv();
        Env.dumpProps();
    }
    
}
