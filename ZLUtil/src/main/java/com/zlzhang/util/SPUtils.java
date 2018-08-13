package com.zlzhang.util;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPrefrences的工具类
 *
 * @author Administrator
 */
public class SPUtils {
    /**
     * 万能的put方法
     *
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, Object value) {
        if (context == null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        //instanceof 用于判断数据类型的
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (int) value);
        } else if (value instanceof Byte) {
            edit.putInt(key, (int) value);
        } else if (value instanceof Double) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (boolean) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        }
        edit.commit();
    }

    /**
     * 获取字符串
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        if (context == null) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 获取long
     *
     * @param context
     * @param key
     * @return
     */
    public static Long getLong(Context context, String key) {
        if (context == null) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    /**
     * 获取整数
     *
     * @param context
     * @param key
     * @return
     */
    public static Float getFloat(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getFloat(key, 0);
    }

    /**
     * 获取Int
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        if (context == null) {
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * 获取Int
     *
     * @param context
     * @param key
     * @return
     */
    public static int getCustomInt(Context context, String key, int custom) {
        if (context == null) {
            return 0;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(key, custom);
    }

    /**
     * 获取Boolean
     * <P>默认为 false </P>
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        if (context == null) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 获取Boolean
     * <P>设置自定义 Boolean </P>
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getCustomBoolean(Context context, String key, boolean isOpen) {
        if (context == null) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, isOpen);
    }

    /**
     * 获取Boolean
     * <P>默认为 false </P>
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getFalseBoolean(Context context, String key) {
        if (context == null) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 获取Boolean
     * <P>默认为 true </P>
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getTrueBoolean(Context context, String key) {
        if (context == null) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }

    /**
     * <p>shang</p>
     * <p>获取有默认值的字符串</p>
     * <p>是固件版本用来对比的</p>
     *
     * @param context
     * @param key
     * @return
     */
    public static String getDefaultString(Context context, String key) {
        if (context == null) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key, "0");
    }
}















