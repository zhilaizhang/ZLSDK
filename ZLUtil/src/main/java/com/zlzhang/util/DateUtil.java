package com.zlzhang.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zhangzhilai on 2018/5/29.
 * 时间的相关方法
 */

public class DateUtil {

    /**
     * 获取当前时区
     *
     * @return
     */
    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName(false, TimeZone.SHORT);
    }

    /**
     * <p>将时间戳转换为标准的时间   yyyyMMdd_HHmmss</p>
     * <p>shang</p>
     *
     * @param timestamp 时间戳
     * @return
     */
    public static String SwitchTimestamp_ymdhm(long timestamp) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyyMMdd_HHmm");
        @SuppressWarnings("unused")//  编译过程中，忽略这种类型的警告
                String times = sdr.format(new Date(timestamp * 1000L));
        return times;
    }

    /**
     * 获取当前时间的string类型
     *
     * @return
     */
    public static String getCurrentDateString(String format) {
        Date date = new Date();
        return getDateString(date, format);
    }

    /**
     * 根据Date和类型获取时间String
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateString(Date date, String format) {
        SimpleDateFormat sdfd = new SimpleDateFormat(format);
        return sdfd.format(date);
    }

    /**
     * 获取当前时间之前多少天的时间
     *
     * @param currentDate
     * @param days
     * @return
     */
    public static Date getBeforeDate(Date currentDate, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int i = 0;
        while (i < days) {
            calendar.add(Calendar.DATE, 1);
            i++;
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                    calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                i--;
            }
        }
        return calendar.getTime();
    }
}
