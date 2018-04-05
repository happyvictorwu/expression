package com.yuxuan.admin.expression.ui;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.ui
 * 文件名:   LoginActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/1/7 11:41
 * 描述:     登陆
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yuxuan.admin.expression.MainActivity;
import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.MyUser;
import com.yuxuan.admin.expression.utils.IpAdressUtils;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.Md5Utils;
import com.yuxuan.admin.expression.utils.ShareUtils;
import com.yuxuan.admin.expression.utils.UtilTools;
import com.yuxuan.admin.expression.view.CustomDialog;

import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends Activity implements View.OnClickListener{
    private TextView tv_regiest;
    private Button bt_login;
    private EditText et_acconut;
    private EditText et_userpassword;
    private CircleImageView iv_head;

    private CheckBox cb_remberPWD;

    private CustomDialog dialog;

    ProgressDialog progress;

    private ImageButton ib_qq;

    // 初始化头像，从bomb读取的头像字符串
    private String bmobImgString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 初始化布局
        initView();
    }

    private void initView() {
        dialog = new CustomDialog(this, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_login, R.style.Theme_dialog, Gravity.CENTER,
                R.style.pop_anim_style);

        dialog.setCancelable(false);

        tv_regiest = (TextView) findViewById(R.id.bt_register);
        tv_regiest.setOnClickListener(this);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);

        et_acconut = (EditText) findViewById(R.id.et_acconut);
        et_userpassword = (EditText) findViewById(R.id.et_userpassword);

        cb_remberPWD = (CheckBox) findViewById(R.id.cb_remberPWD);

        iv_head = (CircleImageView) findViewById(R.id.iv_head);
        iv_head.setBorderWidth(2);
        iv_head.setBorderColor(Color.BLUE);
        // 初始化头像
        initImgHead();

        ib_qq = (ImageButton) findViewById(R.id.ib_qq);
        ib_qq.setOnClickListener(this);

        // 初始化用户名和密码（如果记住密码勾选）
        if (ShareUtils.getBoolean(this, "checkboxBoolean", false)) {
            String username = ShareUtils.getString(LoginActivity.this, "username", "");
            String password = ShareUtils.getString(LoginActivity.this, "password", "");
            et_acconut.setText(username);
            et_userpassword.setText(password);
            cb_remberPWD.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register: // 点击没有帐号按钮
                Intent intent = new Intent();
                intent.setAction("com.login.RegiestActivity");
                startActivityForResult(intent, 1);
                break;
            case R.id.bt_login: // 点击登录按钮
                String username = et_acconut.getText().toString();
                String password = et_userpassword.getText().toString();
                loginInBmob(username, password);

                break;
            case R.id.ib_qq:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 登录到Bmob 云平台
     *
     * @param username
     * @param password
     */
    private void loginInBmob(final String username, final String password) {
        dialog.show();
        MyUser myUser = new MyUser();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.login(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                dialog.dismiss();
                if (e == null) {
                    Intent intent = new Intent();
                    intent.setAction("com.index.IndexActivity");
                    intent.putExtra("username", username);
                    remberPWD(username, password);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ShareUtils.putString(this, "ObjectId", myUser.getObjectId() );
    }

    // 接受注册页面 RegiesActivity返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            String username = data.getStringExtra("username");
            et_acconut.setText(username);
            et_userpassword.setText("");
        }

    }


    /**
     * 实现记住密码功能
     *
     * @param username
     * @param password
     */
    private void remberPWD(String username, String password) {
        if (cb_remberPWD.isChecked()) {
            ShareUtils.putString(this, "username", username);
            ShareUtils.putString(this, "password", password);
            ShareUtils.putBoolean(this, "checkboxBoolean", true);
        } else {
            ShareUtils.putString(this, "username", null);
            ShareUtils.putString(this, "password", null);
            ShareUtils.putBoolean(this, "checkboxBoolean", false);
        }
    }

    // 初始化头像
    private void initImgHead() {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        if (BmobUser.getCurrentUser() != null) {
            query.getObject(BmobUser.getCurrentUser().getObjectId(), new QueryListener<MyUser>() {
                @Override
                public void done(MyUser user, BmobException e) {
                    if (e == null) {
                        bmobImgString = user.getImgHead();
                        if (bmobImgString != null) {
                            Bitmap bitmap = UtilTools.stringToBitmap(bmobImgString);
                            iv_head.setImageBitmap(bitmap);
                        } else {
                            iv_head.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                        }
                    } else {
                        L.i("initImgHead at bmob 失败" + e.getMessage() + "<<< LoginActivity");
                    }
                }
            });
        }

    }

}
