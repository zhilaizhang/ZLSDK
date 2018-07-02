package com.zlzhang.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zhangzhilai on 2018/5/29.
 * 时间的相关方法
 */

public class DateUtil {

    /**
     * 获取当前时区
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
}
