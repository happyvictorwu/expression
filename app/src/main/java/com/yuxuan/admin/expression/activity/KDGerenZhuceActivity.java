package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   KDGerenZhuceActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 16:18
 * 描述:     找赚钱_个人注册
 */

import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class KDGerenZhuceActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_phoneNumber;
    private EditText et_mail;
    private EditText et_real_name;
    private EditText et_idcard;

    private Button btn_regiest;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kd_gerenzhuce);

        initView();
    }

    private void initView() {
        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
        et_mail = (EditText) findViewById(R.id.et_mail);
        et_real_name = (EditText) findViewById(R.id.et_real_name);
        et_idcard = (EditText) findViewById(R.id.et_idcard);

        btn_regiest = (Button) findViewById(R.id.btn_regiest);
        btn_regiest.setOnClickListener(this);

        dealResult();
    }

    //加入成功后处理当前页面
    private void dealResult() {
        List<BmobQuery<MyUser>> queries = new ArrayList<BmobQuery<MyUser>>();
        BmobQuery<MyUser> q1 = new BmobQuery<MyUser>();
        q1.addQueryKeys("teamFlag");
        BmobUser user = MyUser.getCurrentUser();
        String username = user.getUsername();
        BmobQuery<MyUser> q2 = new BmobQuery<MyUser>();
        q2.addWhereEqualTo("username", username);

        queries.add(q1);
        queries.add(q2);

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.and(queries);
        query.findObjects(new FindListener<MyUser>() {

            @Override
            public void done(List<MyUser> object, BmobException e) {

                if (e == null){
                    MyUser u = object.get(0);
                    if (!u.getTeamFlag().equals("")){
                        setET(u.getMobilePhoneNumber(), u.getEmail(), u.getRealName(), u.getIdCard());
                    }
                }else{
                    L.i("error dealResult" + e.getMessage() + e.getErrorCode());
                }

            }
        });
    }

    //设置页面的EditText 不可点击
    private void setET(String phone, String email, String realName, String idCard){
        et_phoneNumber.setText(phone);
        et_phoneNumber.setEnabled(false);
        et_phoneNumber.setBackgroundColor(Color.WHITE);

        et_mail.setText(email);
        et_mail.setEnabled(false);
        et_mail.setBackgroundColor(Color.WHITE);

        et_real_name.setText(realName);
        et_real_name.setEnabled(false);
        et_real_name.setBackgroundColor(Color.WHITE);

        et_idcard.setText(idCard);
        et_idcard.setEnabled(false);
        et_idcard.setBackgroundColor(Color.WHITE);

        btn_regiest.setText("你已经注册过了");
        btn_regiest.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regiest:
                regiestPerson();
                break;

            default:
                break;
        }

    }

    private void regiestPerson() {
        String phoneNumber = et_phoneNumber.getText().toString().trim();
        String email = et_mail.getText().toString().trim();
        String realName = et_real_name.getText().toString().trim();
        String idCard = et_idcard.getText().toString().trim();

        if (UtilTools.checkMobileNumber(phoneNumber)
                && !TextUtils.isEmpty(realName)
                && !TextUtils.isEmpty(idCard)
                && UtilTools.checkEmail(email)){
            //更新
            MyUser newUser = new MyUser();
            newUser.setEmail(email);
            newUser.setMobilePhoneNumber(phoneNumber);
            newUser.setRealName(realName);
            newUser.setIdCard(idCard);
            newUser.setTeamFlag("1");
            BmobUser bmobUser = MyUser.getCurrentUser();
            newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        Toast.makeText(KDGerenZhuceActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                        dealResult();
                    }else{
                        Toast.makeText(KDGerenZhuceActivity.this, "加入失败" + e, Toast.LENGTH_SHORT).show();
                        L.d( e.getErrorCode() + "");
                    }
                }
            });

        } else{
            if( !UtilTools.checkMobileNumber(phoneNumber) ){
                Toast.makeText(this, "电话输入格式不正确", Toast.LENGTH_SHORT).show();
            }else if ( !UtilTools.checkEmail(email) ){
                Toast.makeText(this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "信息填写不完整", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
