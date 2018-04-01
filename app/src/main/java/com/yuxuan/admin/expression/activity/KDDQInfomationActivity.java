package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   KDDQInfomationActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 12:37
 * 描述:     bishe
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.MyUser;
import com.yuxuan.admin.expression.entity.UserDqInfomation;
import com.yuxuan.admin.expression.ui.BaseActivity;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.UtilTools;
import com.yuxuan.admin.expression.view.CustomDialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class KDDQInfomationActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_phoneNumber;
    private EditText et_name;
    private EditText et_addr;
    private EditText et_status;
    private EditText et_other;

    private Button btn_dq_ok;

    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kd_dq_infomation);
        initView();
    }

    private void initView() {
        dialog = new CustomDialog(this, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER,
                R.style.pop_anim_style);

        dialog.setCancelable(false);



        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
        et_name = (EditText) findViewById(R.id.et_name);
        et_addr = (EditText) findViewById(R.id.et_addr);
        et_status = (EditText) findViewById(R.id.et_status);
        et_other = (EditText) findViewById(R.id.et_other);

        btn_dq_ok = (Button) findViewById(R.id.btn_dq_ok);
        btn_dq_ok.setOnClickListener(this);

        String mPhone = BmobUser.getCurrentUser().getMobilePhoneNumber();
        et_phoneNumber.setText(mPhone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dq_ok:
                if ( dataIsRight() ) {
                    dialog.show();
                    addInfo();
                }
                break;

            default:
                break;
        }

    }

    //将数据保存到数据库（Bmob）   ？？？？？
    private void addInfo() {
        String phoneNumber = et_phoneNumber.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String addr = et_addr.getText().toString().trim();
        String status = et_status.getText().toString().trim();
        String other = et_other.getText().toString().trim();

        //获取代取用户的手机号，即送件人
        String dq_phone = getIntent().getStringExtra("dq_phone");

        BmobUser user = MyUser.getCurrentUser();
        String username = user.getUsername();
        UserDqInfomation DqInfo = new UserDqInfomation();
        DqInfo.setUsername(username);
        DqInfo.setPhone(phoneNumber);
        DqInfo.setName(name);
        DqInfo.setAddr(addr);
        DqInfo.setStatus(status);
        DqInfo.setOther(other);
        DqInfo.setSuccess(false);
        if ( !TextUtils.isEmpty(dq_phone) ){
            DqInfo.setDq_phone(dq_phone);
        }
        DqInfo.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    //Toast.makeText(KDDQInfomationActivity.this, "创建数据成功：" + objectId, Toast.LENGTH_SHORT).show();
                    L.i("添加ok");
                    dialog.dismiss();
                    new AlertDialog.Builder(KDDQInfomationActivity.this).setTitle("注意")
                            .setMessage("代取信息填写成功\n请到  '我的订单' 中查看详细信息\n我们稍后联系您，请保持信息畅通")
                            .setPositiveButton("我的订单？", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(KDDQInfomationActivity.this, UMyOrdersActivity.class));
                                    finish();

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();



                } else {
                    L.i("代取信息添加失败：" + e.getMessage());
                    Toast.makeText(KDDQInfomationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // 判断输入框中的数据是否正确
    private boolean dataIsRight() {
        String phoneNumber = et_phoneNumber.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String addr = et_addr.getText().toString().trim();
        String status = et_status.getText().toString().trim();
        String other = et_other.getText().toString().trim();

        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(addr)
                && !TextUtils.isEmpty(status) && !TextUtils.isEmpty(other)) {

            if (UtilTools.checkMobileNumber(phoneNumber)) {
                return true;
            } else {
                Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
        }

        return false;

    }
}
