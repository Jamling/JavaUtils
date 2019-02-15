package cn.ieclipse.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.ieclipse.util.ReflectUtils.FieldFilter;

public class ReflectUtilsTest {
    
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testGetClassFieldClassOfQBooleanFieldFilter() {
        List<Field> fs = ReflectUtils.getClassField(B.class);
        B b = new B();
        for (Field f : fs) {
            if (f.getName().equals("pri")) {
                assertEquals(B.class, f.getDeclaringClass());
                assertEquals(11, ReflectUtils.get(f, b));
            }
            else if (f.getName().equals("pro")) {
                assertEquals(B.class, f.getDeclaringClass());
                assertEquals(22, ReflectUtils.get(f, b));
            }
            else if (f.getName().equals("pub")) {
                assertEquals(B.class, f.getDeclaringClass());
                assertEquals(44, ReflectUtils.get(f, b));
            }
            else if (f.getName().equals("dft")) {
                assertEquals(A.class, f.getDeclaringClass());
                assertEquals(33, ReflectUtils.get(f, b));
            }
            FieldFilter filter = (field) -> {
                return (field.getModifiers() == Modifier.PUBLIC);
            };
            fs = ReflectUtils.getClassField(B.class, filter);
            assertEquals(1, fs.size());
        }
    }
    
    private static class A {
        private int pri = 1;
        protected int pro = 2;
        public int pub = 4;
        
        int dft = 3;
    }
    
    private static class B extends A {
        private int pri = 11;
        protected int pro = 22;
        public int pub = 44;
        
        public B() {
            dft = 33;
        }
    }
}
