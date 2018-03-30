package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   KDZhuanQianActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 16:06
 * 描述:     bishe
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.ui.BaseActivity;

public class KDZhuanQianActivity extends BaseActivity {

    ImageView team_money;
    ImageView person_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kd_zhuanqian);

        team_money = (ImageView) findViewById(R.id.team_money);
        person_money = (ImageView) findViewById(R.id.person_money);

        team_money.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(KDZhuanQianActivity.this,KDTeamRegister.class);
                startActivity(intent1);
            }
        });

        person_money.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(KDZhuanQianActivity.this,KDGerenZhuceActivity.class);
                startActivity(intent1);
            }
        });

    }
}
