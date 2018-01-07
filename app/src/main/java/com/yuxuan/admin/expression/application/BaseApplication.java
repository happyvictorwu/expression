/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.application
 * 文件名:   BaseApplication
 * 创建者:   YUXUAN
 * 创建时间: 2017/12/31 20:43
 * 描述:     BaseApplication
 */
package com.yuxuan.admin.expression.application;
import android.app.Application;

import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.StaticClass;

import cn.bmob.v3.Bmob;


public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        L.d("init_OK");
        //初始化 Bmob 云服务
        Bmob.initialize(getApplicationContext(), StaticClass.BMOB_APPLICATION_ID);
    }
}
