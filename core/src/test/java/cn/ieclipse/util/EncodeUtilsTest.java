package cn.ieclipse.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class EncodeUtilsTest {
    String abc = "简体中文Abc";
    
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testEncodeXml() {
        String src = "<font style='1'>font</font>";
        String dst = EncodeUtils.encodeXml(src);
        assertEquals(src, EncodeUtils.decodeXml(dst));
    }
    
    @Test
    public void testEncodeUrl() {
        String actual = EncodeUtils.encodeUrl(abc, null);
        String src = EncodeUtils.decodeUrl(actual, null);
        assertEquals(abc, src);
    }
    
    @Test
    public void testEncode() {
        Info2 info2 = new Info2();
        Map<String, Object> map = EncodeUtils.encode(info2, false);
        assertEquals(3, map.size());
        map = EncodeUtils.encode(info2, true);
        assertEquals(1, map.size());
    }
    
    @Test
    public void testEncodeRequestParam() {
        String encode = EncodeUtils.encodeUrl(abc, null);
        String actual = EncodeUtils.encodeRequestParam(abc, null);
        assertEquals(encode, actual);
        
        actual = EncodeUtils.encodeRequestParam(new String[] { abc, abc },
                null);
        assertEquals(encode + "%2C" + encode, actual);
        
        ArrayList<String> list = new ArrayList<>();
        list.add(abc);
        list.add(abc);
        actual = EncodeUtils.encodeRequestParam(new String[] { abc, abc },
                null);
        assertEquals(encode + "%2C" + encode, actual);
    }
    
    @Test
    public void testEncodeRequestBodyObjectStringBoolean() {
        Info2 info2 = new Info2();
        info2.p = "b";
        String actual = EncodeUtils.encodeRequestBody(info2, null, true);
        assertEquals("p=b", actual);
    }
    
    @Test
    public void testEncodeRequestBodyMapStringBoolean() {
        Info2 info2 = new Info2();
        Map<String, Object> map = EncodeUtils.encode(info2, false);
        assertEquals(3, map.size());
        map.put("p1", "p1");
        String actual = EncodeUtils.encodeRequestBody(map, null, true);
        assertEquals("p=a&p1=p1", actual);
    }
    
    @Test
    public void testEncodeUnicode() {
        String actual = EncodeUtils.encodeUnicode(abc);
        String src = EncodeUtils.decodeUnicode(actual);
        assertEquals(abc, src);
        System.out.println(actual);
    }
    
    private static class Info {
        public String p = "a";
    }
    
    private static class Info2 extends Info {
        public String p1;
        public String p2;
        public static final int i = 1;
    }
}
