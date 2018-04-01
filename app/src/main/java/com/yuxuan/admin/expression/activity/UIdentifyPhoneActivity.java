package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   UIdentifyPhoneActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/31 8:46
 * 描述:     手机验证
 */

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.MyUser;
import com.yuxuan.admin.expression.ui.BaseActivity;
import com.yuxuan.admin.expression.utils.CountDownTimerUtils;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.UtilTools;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class UIdentifyPhoneActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_get_identy_code;

    private EditText et_write_phone;
    private EditText et_identify_code;

    private TextView tv_verify;

    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_identify_phone);

        initView();
    }

    // 初始化布局
    private void initView() {
        btn_get_identy_code = (Button) findViewById(R.id.btn_get_identy_code);
        btn_get_identy_code.setOnClickListener(this);
        btn_get_identy_code.setBackground(getResources().getDrawable(R.drawable.bg_identify_code_press));

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        et_write_phone = (EditText) findViewById(R.id.et_write_phone);
        //如果有手机号，则获取手机号
        String myPhone = null;
        myPhone = BmobUser.getCurrentUser().getMobilePhoneNumber();
        L.i(BmobUser.getCurrentUser().getMobilePhoneNumber() + "");
        if (myPhone != null){
            et_write_phone.setText(myPhone);
            btn_get_identy_code.setEnabled(true);
            btn_get_identy_code.setBackground(getResources().getDrawable(R.drawable.selector));
        }
        //如果手机号已经被验证，给出该提示
        Boolean verifyPhone = BmobUser.getCurrentUser().getMobilePhoneNumberVerified();
        if (verifyPhone != null && verifyPhone){
            tv_verify = (TextView) findViewById(R.id.tv_verify);
            tv_verify.setVisibility(View.VISIBLE);
        }

        et_write_phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btn_get_identy_code.setEnabled(true);
                    btn_get_identy_code.setBackground(getResources().getDrawable(R.drawable.selector));

                } else {
                    btn_get_identy_code.setEnabled(false);
                    btn_get_identy_code.setBackground(getResources().getDrawable(R.drawable.bg_identify_code_press));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        et_identify_code = (EditText) findViewById(R.id.et_identify_code);



    }

    @Override
    public void onClick(View v) {
        String mobileNumber = et_write_phone.getText().toString().trim();
        String identify_code = et_identify_code.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_get_identy_code:

                if (UtilTools.checkMobileNumber(mobileNumber)) {
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(btn_get_identy_code, 60000, 1000);
                    mCountDownTimerUtils.start();
                    // 获取验证码
                    getSMSVerifyCode(mobileNumber);
                } else {
                    Toast.makeText(this, "电话号码格式不正确", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btn_submit:
                if (!TextUtils.isEmpty(identify_code) && !TextUtils.isEmpty(mobileNumber)) {
                    // 开始验证手机号
                    submitVerify(mobileNumber, identify_code);
                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;
        }
    }

    // 获取验证码
    private void getSMSVerifyCode(String phone) {
        L.i("开始获取短信验证码");
        BmobSMS.requestSMSCode(phone, "phone", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {// 验证码发送成功
                    L.i("smile 短信id：" + smsId);// 用于查询本次短信发送详情
                } else {
                    L.i("短信验证失败 " + ex.getMessage());
                    Toast.makeText(UIdentifyPhoneActivity.this, "sorry 验证失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // 提交验证
    private void submitVerify(final String mobileNumber, String identify_code) {
        L.i("开始验证手机");
        BmobSMS.verifySmsCode(mobileNumber, identify_code, new UpdateListener() {

            @Override
            public void done(BmobException ex) {
                if (ex == null) {// 短信验证码已验证成功
                    L.i("smile 验证通过 开始绑定");
                    //验证手机成功，绑定用户
                    bindMobilePhone(mobileNumber);
                } else {
                    L.i("smile 验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                }
            }
        });
    }

    // 绑定手机
    private void bindMobilePhone(String phone) {
        // 开发者在给用户绑定手机号码的时候需要提交两个字段的值：mobilePhoneNumber、mobilePhoneNumberVerified
        MyUser user = new MyUser();
        user.setMobilePhoneNumber(phone);
        user.setMobilePhoneNumberVerified(true);
        MyUser cur = BmobUser.getCurrentUser(MyUser.class);
        user.update(cur.getObjectId(),new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    L.i("手机号码绑定成功");
                    Toast.makeText(UIdentifyPhoneActivity.this, "手机号码验证成功", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    L.i("手机号码失败:" + e.getMessage());
                    Toast.makeText(UIdentifyPhoneActivity.this, "sorry 失败了", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
