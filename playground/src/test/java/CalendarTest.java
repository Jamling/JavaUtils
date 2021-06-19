import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class CalendarTest {
    @Test
    public void testAddDay() {
        Calendar c = Calendar.getInstance();
        System.out.println(c.getTimeInMillis());
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        //c.set(Calendar.MILLISECOND, 0);

        long t = c.getTimeInMillis();
        System.out.println(Integer.MAX_VALUE);
        System.out.println(t);
        c.add(Calendar.DATE, 7);
        long t2 = c.getTimeInMillis();

        Assert.assertEquals(3600 * 24 * 1000, 86400000);

        long t3 = t + 86400000 * 7;
        Assert.assertEquals(t2, t3);

    }
}