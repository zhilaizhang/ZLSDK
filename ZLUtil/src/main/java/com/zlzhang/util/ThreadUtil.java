package com.zlzhang.util;

/**
 * Created by zhilaizhang on 17/7/8.
 */

public class ThreadUtil {
    /**
     * 获取当前线程
     * @return
     */
    public static Thread getCurrentThread(){
        return Thread.currentThread();
    }

    /**
     * 获取当前线程id
     * @return
     */
    public static long getCurrentThreadId(){
        return Thread.currentThread().getId();
    }

    /**
     * 获取当前线程name
     * @return
     */
    public static String getCurrentThreadName(){
        return Thread.currentThread().getName();
    }

}
