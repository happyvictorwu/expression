package com.yuxuan.admin.expression.utils;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.utils
 * 文件名:   ShareUtils
 * 创建者:   YUXUAN
 * 创建时间: 2018/1/1 18:28
 * 描述:     对 SharePreferces 的封装，可以快速set get String boolean int 值
 *          删除单个或者全部  key--value
 */

import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtils {
    public static final String NAME = "config";

    // String
    public static void putString(Context mContext, String key, String value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context mContext, String key, String defValue){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    //boolean
    public static void putBoolean(Context mContext, String key, boolean value)
    {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context mContext, String key, boolean defValue){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    // int
    public static void putInt(Context mContext, String key, int value)
    {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context mContext, String key, int defValue){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    //根据 key 删除单个
    public static void deletShare(Context mContext, String key){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    //删除全部
    public static void deletAll(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().clear().clear();
    }
}
