package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   KDFoundTeamAndPersonActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 12:26
 * 描述:     个人团队选择界面
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.ui.BaseActivity;

public class KDFoundTeamAndPersonActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ib_individual_choose;
    private ImageView ib_team_choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kd_foundteam_and_person);
        initView();
    }

    private void initView() {
        ib_team_choose = (ImageView) findViewById(R.id.ib_team_choose);
        ib_team_choose.setOnClickListener(this);

        ib_individual_choose = (ImageView) findViewById(R.id.ib_individual_choose);
        ib_individual_choose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_individual_choose:
                startActivity(new Intent(this, KDPersonDQActivity.class));
                break;

            case R.id.ib_team_choose:
                startActivity(new Intent(this, KDTeamDQActivity.class));
                break;
        }
    }
}

