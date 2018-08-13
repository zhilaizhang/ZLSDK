package com.zlzhang.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhilaizhang on 17/7/8.
 */

public class TimeUtil {
    public static String getTimeString(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        date.setTime(time);
        String taskEndTimeString = simpleDateFormat.format(date.getTime());
        return taskEndTimeString;
    }

    public static long getTimeStampByDate(int year, int month, int day, int hour, int minu) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = year + "-" + month + "-" + day + " " + hour + ":" + minu;
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return date.getTime();
        } else {
            return 0;
        }
    }

    public static int[] getCurrentDateArray(){
        long timeStamp = System.currentTimeMillis();
        return getDateByTimeStamp(timeStamp);
    }

    public static int[] getDateByTimeStamp(long timeStamp) {
        int[] timeArray = new int[7];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        timeArray[0] = calendar.get(Calendar.YEAR);
        timeArray[1] = calendar.get(Calendar.MONTH);
        timeArray[2] = calendar.get(Calendar.DAY_OF_MONTH);
        timeArray[3] = calendar.get(Calendar.HOUR_OF_DAY);
        timeArray[4] = calendar.get(Calendar.MINUTE);
        timeArray[5] = calendar.get(Calendar.SECOND);
        timeArray[6] = calendar.get(Calendar.MILLISECOND);
        return timeArray;
    }

    public static String getCurrentTime() {
        String time;
        String monthString;
        String dayString;
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (month < 10) {
            monthString = "0" + (month + 1);
        } else {
            monthString = "" + (month + 1);
        }

        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = "" + day;
        }
        return year + "-" + monthString + "-" + dayString;
    }
}
