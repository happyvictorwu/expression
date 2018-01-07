package com.yuxuan.admin.expression.ui;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.ui
 * 文件名:   LoginActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/1/7 11:41
 * 描述:     登陆
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.yuxuan.admin.expression.R;

public class LoginActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // UtilTools.setStrategy();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        // 初始化布局
        //initView();
    }
}
