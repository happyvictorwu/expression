package com.yuxuan.admin.expression.ui;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.ui
 * 文件名:   BaseActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/1/1 18:03
 * 描述:     Activity基类 统一Activity
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示返回键
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    //菜单栏操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
