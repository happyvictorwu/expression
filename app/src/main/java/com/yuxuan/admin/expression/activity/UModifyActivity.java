package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   UModifyActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/31 8:26
 * 描述:     修改密码
 */

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.ui.BaseActivity;
import com.yuxuan.admin.expression.utils.IpAdressUtils;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.Md5Utils;
import com.yuxuan.admin.expression.utils.RemoteServiceUtils;
import com.yuxuan.admin.expression.utils.ShareUtils;
import com.yuxuan.admin.expression.utils.UtilTools;

import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UModifyActivity extends BaseActivity implements View.OnClickListener {

    private EditText passwordest;
    private EditText repasswordest;
    private EditText reppasswordest;
    private CheckBox check;

    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_modify);

        initView();

    }

    private void initView() {
        passwordest = (EditText) findViewById(R.id.passwordest);
        repasswordest = (EditText) findViewById(R.id.repasswordest);
        reppasswordest = (EditText) findViewById(R.id.reppasswordest);

        check = (CheckBox) findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.isChecked()) {
                    passwordest.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    repasswordest.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordest.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    repasswordest.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String beforPwd = passwordest.getText().toString().trim();
        String pwd = repasswordest.getText().toString().trim();
        String rePwd = reppasswordest.getText().toString().trim();
        //	String username = getIntent().getStringExtra("username");
        switch (v.getId()) {
            case R.id.btn_login:
                if (pwd.equals(rePwd) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(rePwd)
                        && !TextUtils.isEmpty(beforPwd)) {


                    modifyPWDFromBmob(beforPwd, rePwd);// 从Bmob 云后端修改

                } else if (!pwd.equals(rePwd)) {
                    Toast.makeText(UModifyActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(UModifyActivity.this, "请检查信息填写是否完整哟", Toast.LENGTH_SHORT).show();
                }

            default:
                break;
        }
    }

    /**
     * 修改密码从Bmob
     * @param beforPwd
     * @param rePwd
     */
    private void modifyPWDFromBmob(String beforPwd, final String rePwd) {
        String password = ShareUtils.getString(UModifyActivity.this, "password", "");
        if(beforPwd.equals(password)) {
            BmobUser newUser = new BmobUser();
            newUser.setPassword(rePwd);
            BmobUser bmobUser = BmobUser.getCurrentUser();
            newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        Toast.makeText(UModifyActivity.this, "密码修改成功，可以用新密码进行登录啦", Toast.LENGTH_LONG).show();
                        ShareUtils.putString(UModifyActivity.this, "password", rePwd);
                        finish();
                    }else{
                        Toast.makeText(UModifyActivity.this, "密码异常！" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else {
            Toast.makeText(UModifyActivity.this, "原密码输入错误！" , Toast.LENGTH_LONG).show();

        }

    }
    

}
