package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   USetingActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/31 8:22
 * 描述:     设置
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.MyUser;
import com.yuxuan.admin.expression.ui.BaseActivity;
import com.yuxuan.admin.expression.ui.LoginActivity;

import cn.bmob.v3.BmobUser;

public class USetingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_updata_password;
    private LinearLayout ll_updata_phone_number;

    private Button btn_logout;

    private TextView tv_verify_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);

        initView();
    }

    // 初始化布局
    private void initView() {
        ll_updata_password = (LinearLayout) findViewById(R.id.ll_updata_password);
        ll_updata_password.setOnClickListener(this);
        ll_updata_phone_number = (LinearLayout) findViewById(R.id.ll_updata_phone_number);
        ll_updata_phone_number.setOnClickListener(this);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);

        tv_verify_phone = (TextView) findViewById(R.id.tv_verify_phone);
        Boolean phoneVerified = BmobUser.getCurrentUser().getMobilePhoneNumberVerified();
        if (phoneVerified != null && phoneVerified){
            tv_verify_phone.setText("您的手机号码已经验证过了");
            tv_verify_phone.setTextColor(Color.RED);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_updata_password:
                startActivity(new Intent(this, UModifyActivity.class));
                break;
            case R.id.ll_updata_phone_number:
                startActivity(new Intent(this, UIdentifyPhoneActivity.class));
                break;

            case R.id.btn_logout:
                MyUser.logOut();   //清除缓存用户对象
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(this, "退出成功", Toast.LENGTH_LONG).show();
                //BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
                break;
            default:
                break;
        }

    }
}
