package com.zlzhang.util;

/**
 * Created by zhilaizhang on 17/7/8.
 */

public class TextUtil {

    public static boolean isTextEmpty(String text){
        if (text == null || text.trim().equals("") || text.equals("null")
                || text.equals("NULL")) {
            return true;
        } else {
            return false;
        }
    }
}
