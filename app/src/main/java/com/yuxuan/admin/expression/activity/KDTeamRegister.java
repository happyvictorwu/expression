package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   KDTeamRegister
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 16:12
 * 描述:     赚钱_团队注册
 */

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.MyUser;
import com.yuxuan.admin.expression.ui.BaseActivity;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.UtilTools;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class KDTeamRegister extends BaseActivity implements View.OnClickListener {

    private EditText et_team_teleohone;
    private EditText et_team_mail;
    private EditText et_tname;

    private Button bt_zhuce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kd_team_register);

        initView();
    }


    //初始化View
    private void initView() {
        et_team_teleohone = (EditText) findViewById(R.id.et_team_teleohone);
        et_team_mail = (EditText) findViewById(R.id.et_team_mail);
        et_tname = (EditText) findViewById(R.id.et_tname);

        bt_zhuce = (Button) findViewById(R.id.bt_zhuce);
        bt_zhuce.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_zhuce:
                regiestTeam();
                break;

            default:
                break;
        }

    }

    private void regiestTeam() {
        String team_phone = et_team_teleohone.getText().toString().trim();
        String team_mail = et_team_mail.getText().toString().trim();
        String tname = et_tname.getText().toString().trim();

        if ( !TextUtils.isEmpty(team_phone) && !TextUtils.isEmpty(team_mail)
                && !TextUtils.isEmpty(tname) ){
            if( UtilTools.checkMobileNumber(team_phone)
                    && UtilTools.checkEmail(team_mail)){

                MyUser u = new MyUser();
                u.setMobilePhoneNumber(team_phone);
                u.setEmail(team_mail);
                u.setTeamFlag(tname);
                BmobUser bmobUser = MyUser.getCurrentUser();
                u.update(bmobUser.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(KDTeamRegister.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(KDTeamRegister.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            L.i(e.getMessage() + e.getErrorCode());
                        }
                    }
                });

            }else{
                if ( !UtilTools.checkMobileNumber(team_phone)){
                    Toast.makeText(KDTeamRegister.this, "电话格式不正确", Toast.LENGTH_SHORT).show();
                }else if( !UtilTools.checkEmail(team_mail) ){
                    Toast.makeText(KDTeamRegister.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                }


            }


        }else{
            Toast.makeText(KDTeamRegister.this, "信息不能为空", Toast.LENGTH_SHORT).show();
        }
    }

}
