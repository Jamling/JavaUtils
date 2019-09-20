package cn.ieclipse.util;

import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description
 *
 * @author Jamling
 */
public class DateUtilsTest {
    @Test
    public void now() throws Exception {
        String s1 = DateUtils.now();
        String s2 = new SimpleDateFormat(DateUtils.DATE_FORMAT_NOW).format(System.currentTimeMillis());
        Assert.assertNotNull(s1);
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void now1() throws Exception {
        System.out.println(String.format("|%1$2s|", "f"));
    }

    @Test
    public void format() throws Exception {
        
    }

    @Test
    public void format1() throws Exception {

    }

    @Test
    public void format2() throws Exception {

    }

    @Test
    public void format3() throws Exception {

    }

    @Test
    public void formatShort() throws Exception {

    }

    @Test
    public void format4() throws Exception {

    }

    @Test
    public void formatDuration() throws Exception {

    }

    @Test
    public void getTimeOver() throws Exception {

    }

    @Test
    public void getTodayTimeBucket() throws Exception {

    }

    @Test
    public void getWeekOfDate() throws Exception {

    }

    @Test
    public void getWeekOfMillis() throws Exception {

    }

    @Test
    public void isSameDay() throws Exception {

    }

    @Test
    public void isSameDay1() throws Exception {

    }

    @Test
    public void getTimeDesc() throws Exception {

    }
}