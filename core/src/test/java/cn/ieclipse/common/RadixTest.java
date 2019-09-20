 package cn.ieclipse.common;

import org.junit.Assert;
import org.junit.Test;

import cn.ieclipse.util.StringUtils;

public class RadixTest {

    @Test
    public void fromPercent() {
        int v = (int)(0xff * 0.2f);
        String s = StringUtils.getFixWidthString(Integer.toHexString(v),2);
        
        String a = Radix.fromPercent(0.2f, 16);
        
        Assert.assertEquals(s, a);
    }

}
