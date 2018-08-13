package com.zlzhang.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhilaizhang on 17/7/8.
 */

public class LogHelper {
    public static final String LOGF = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "test" + File.separator + "ids.log";

    public static final String VER = "0607";

    private static boolean DebugBuildTag = false;
    private static boolean Log2FileTag   = false;

    public static void setDebugBuildTag(boolean debugBuildTag) {
        if (!DebugBuildTag) {
            // first turn-on would prohibit turning off
            DebugBuildTag = debugBuildTag;
        }
    }

    public static boolean getDebugBuildTag() {
        return DebugBuildTag;
    }

    public static boolean getLog2FileTag() {
        return Log2FileTag;
    }

    public static void setLog2FileTag(boolean log2FileTag) {
        if (!Log2FileTag) {
            // first turn-on would prohibit turning off
            Log2FileTag = log2FileTag;
        }
    }

    private static String getFileLineMethod() {
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[4];
        return "(" + traceElement.getFileName() + ":" + traceElement.getLineNumber() + ") " + traceElement.getMethodName() + "()";
    }

    public static void v(String tag, String message) {
        String msg = getFileLineMethod() + ": " + message;
        if (DebugBuildTag) {
            Log.v(tag, msg);
        }
        Log2File(tag, msg);
    }

    public static void d(String tag, String message) {
        String msg = getFileLineMethod() + ": " + message;
        if (DebugBuildTag) {
            Log.d(tag, msg);
        }
        Log2File(tag, msg);
    }

    public static void i(String tag, String message) {
        String msg = getFileLineMethod() + ": " + message;
        if (DebugBuildTag) {
            Log.i(tag, msg);
        }
        Log2File(tag, msg);
    }

    public static void w(String tag, String message) {
        String msg = getFileLineMethod() + ": " + message;
        if (DebugBuildTag) {
            Log.w(tag, msg);
        }
        Log2File(tag, msg);
    }

    public static void e(String tag, String message) {
        String msg = getFileLineMethod() + ": " + message;
        if (DebugBuildTag) {
            Log.e(tag, msg);
        }
        Log2File(tag, msg);
    }

    private static void Log2File(String tag, String msg) {
        if (Log2FileTag) {
        }
    }

    public static void wtf(String tag, String message) {
        Log.wtf(tag, getFileLineMethod() + ": " + message);
    }

    public static String _FILE_() {
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
        return traceElement.getFileName();
    }

    public static String _FUNC_() {
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
        return traceElement.getMethodName();
    }

    public static int _LINE_() {
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
        return traceElement.getLineNumber();
    }

    public static String _TIME_() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }
}
