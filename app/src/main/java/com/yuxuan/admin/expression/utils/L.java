/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.utils
 * 文件名:   L
 * 创建者:   YUXUAN
 * 创建时间: 2017/12/31 20:46
 * 描述:     Log工具类
 */
package com.yuxuan.admin.expression.utils;

import android.util.Log;

public class L {
    //开关
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "schoolbee";


    public static void d(String text){
        if (DEBUG){
            Log.d(TAG, text);
        }
    }

    public static void i(String text){
        if (DEBUG){
            Log.i(TAG, text);
        }
    }

    public static void w(String text){
        if (DEBUG){
            Log.w(TAG, text);
        }
    }

    public static void e(String text){
        if (DEBUG){
            Log.e(TAG, text);
        }
    }
}
