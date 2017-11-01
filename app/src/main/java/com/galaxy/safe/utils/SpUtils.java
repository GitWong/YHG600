package com.galaxy.safe.utils;

/**
 * Created by Dell on 2016/2/29.
 */


import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {
    private static SharedPreferences sp;

    /**
     * 获取一个Boolean类型数据
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean getboolean(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, value);
    }

    /**
     * 存储一个Boolean类型
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 存储一个String类型的数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     * 根据key取出一个String类型的值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }
}

