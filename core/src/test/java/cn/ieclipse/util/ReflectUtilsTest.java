package cn.ieclipse.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import cn.ieclipse.util.ReflectUtils.FieldFilter;

public class ReflectUtilsTest {
    
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testCast() {
        System.out.println(int.class);
        System.out.println(String.class);
        Optional<Integer> val = ReflectUtils.cast(Integer.class, 1);
        assertEquals(val, Optional.of(1));
    }
    
    @Test
    public void testGetClassFieldClassOfQBooleanFieldFilter() {
        List<Field> fs = ReflectUtils.getClassField(B.class);
        B b = new B();
        for (Field f : fs) {
            if (f.getName().equals("pri")) {
                assertEquals(B.class, f.getDeclaringClass());
                Object val = ReflectUtils.get(f, b);
                assertEquals(11, val);
            }
            else if (f.getName().equals("pro")) {
                assertEquals(B.class, f.getDeclaringClass());
                Object val = ReflectUtils.get(f, b);
                assertEquals(22, val);
            }
            else if (f.getName().equals("pub")) {
                assertEquals(B.class, f.getDeclaringClass());
                Object val = ReflectUtils.get(f, b);
                assertEquals(44, val);
            }
            else if (f.getName().equals("dft")) {
                assertEquals(A.class, f.getDeclaringClass());
                Object val = ReflectUtils.get(f, b);
                assertEquals(33, val);
            }
            FieldFilter filter = (field) -> (field.getModifiers() == Modifier.PUBLIC);
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
