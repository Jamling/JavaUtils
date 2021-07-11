package playground.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateTime {

    public static void main(String[] args) {
        // 日期时间
        Date date = new Date();
        // Calendar
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = calendar.getInstance();
        System.out.println(date);
        System.out.println(calendar);
        //
        calendar.set(Calendar.HOUR, 16);
        calendar.get(Calendar.YEAR); // 2021
        // YYYY-MM-dd
        date = calendar.getTime();
        // 格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 解析
        try {
            sdf.parse("2021-07-11");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 线程安全的。华为
        LocalDateTime localDateTime = LocalDateTime.now();

    }
}
