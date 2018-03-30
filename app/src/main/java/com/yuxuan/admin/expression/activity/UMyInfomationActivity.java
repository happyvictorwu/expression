package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   UMyInfomationActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 19:22
 * 描述:     我的信息
 */

import android.graphics.Color;
import android.os.Bundle;
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
import com.yuxuan.admin.expression.view.CustomDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UMyInfomationActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_nichen;
    private EditText et_phoneNumber;
    private EditText et_idCard;
    private EditText et_mail;
    private EditText et_real_name;

    private Button btn_modify;
    private Button btn_updata;

    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_myinfomation);

        initView();
        setDataTOView();
    }

    // 设置数据到界面上
    private void setDataTOView() {
        // 获取缓存用户
        BmobUser userQuery = MyUser.getCurrentUser();
        // 获取缓存用户的用户名
        String cacheUsername = userQuery.getUsername();
        // 开始查询用户资料
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", cacheUsername);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object, BmobException e) {
                if (e == null) {
                    MyUser user = object.get(0);
                    String nichen = user.getUsername();
                    String phoneNumber = user.getMobilePhoneNumber();
                    String email = user.getEmail();
                    String idCard = user.getIdCard();
                    String realName = user.getRealName();

                    // 查询成功后将数据设置到EditText上
                    et_nichen.setText(nichen);
                    et_mail.setText(email);
                    et_idCard.setText(idCard);
                    et_phoneNumber.setText(phoneNumber);
                    et_real_name.setText(realName);

                } else {
                    Toast.makeText(UMyInfomationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // 初始化布局
    private void initView() {

        dialog = new CustomDialog(this, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER,
                R.style.pop_anim_style);

        dialog.setCancelable(false);

        et_nichen = (EditText) findViewById(R.id.et_nichen);
        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
        et_idCard = (EditText) findViewById(R.id.et_idCard);
        et_mail = (EditText) findViewById(R.id.et_mail);
        et_real_name = (EditText) findViewById(R.id.et_real_name);
        setEditTextEnable(false);

        btn_modify = (Button) findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(this);
        btn_modify.setVisibility(View.VISIBLE);

        btn_updata = (Button) findViewById(R.id.btn_updata);
        btn_updata.setOnClickListener(this);

    }

    // 设置编辑框的激活状态
    private void setEditTextEnable(boolean state) {
        et_nichen.setEnabled(state);
        et_phoneNumber.setEnabled(state);
        et_idCard.setEnabled(state);
        et_mail.setEnabled(state);
        et_real_name.setEnabled(state);

        if (!state) {
            et_nichen.setBackgroundColor(Color.WHITE);
            et_phoneNumber.setBackgroundColor(Color.WHITE);
            et_idCard.setBackgroundColor(Color.WHITE);
            et_mail.setBackgroundColor(Color.WHITE);
            et_real_name.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击修改资料
            case R.id.btn_modify:
                // 设置EditText 为激活状态
                setEditTextEnable(true);
                btn_updata.setVisibility(View.VISIBLE);
                btn_modify.setVisibility(View.GONE);
                break;

            // 点击确认修改
            case R.id.btn_updata:
                btn_modify.setVisibility(View.VISIBLE);
                btn_updata.setVisibility(View.GONE);
                dialog.show();
                // 修改资料
                updataUser();
                break;
        }

    }

    /**
     * 修改UserDqInfomation表中的username，da_phone字段
     *
     * @param cacheUsername
     *            通过缓存user的username查找修改
     */
    private void updataUserDqInfomation(String cacheUsername) {
        final String username = et_nichen.getText().toString().trim();
        final String phoneNumber = et_phoneNumber.getText().toString().trim();

        // 开始查找需要修改的数据
        BmobQuery<UserDqInfomation> query = new BmobQuery<UserDqInfomation>();
        L.i(cacheUsername + "<<<<< Usrname");
        query.addWhereEqualTo("username", cacheUsername);
        query.setLimit(50);
        // 执行查询方法
        query.findObjects(new FindListener<UserDqInfomation>() {
            @Override
            public void done(List<UserDqInfomation> object, BmobException e) {
                if (e == null) {
                    UserDqInfomation u = null;
                    for (int i = 0; i < object.size(); i++) {
                        u = new UserDqInfomation();
                        u.setValue("username", username);
                        u.setValue("dq_phone", phoneNumber);
                        u.update(object.get(i).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                } else {
                                    L.i("UserDQInfomation<<<<< no" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    }
                } else {
                    L.i("级联更新操作失败");
                }
            }
        });
    }

    /**
     * 修改User表，主要修改username, real_name, phone, email, idCard 字段
     */
    private void updataUser() {
        String username = et_nichen.getText().toString().trim();
        String real_name = et_real_name.getText().toString().trim();
        String phoneNumber = et_phoneNumber.getText().toString().trim();
        String idCard = et_idCard.getText().toString().trim();
        String email = et_mail.getText().toString().trim();
        // 获取缓存用户的信息
        final String cacheUsername = BmobUser.getCurrentUser().getUsername();

        BmobUser cachebUser = BmobUser.getCurrentUser();
        MyUser newUser = new MyUser();
        if (!email.equals(cachebUser.getEmail())) {
            newUser.setEmail(email);
        }
        if (!username.equals(cachebUser.getUsername())) {
            newUser.setUsername(username);
        }
        if (!phoneNumber.equals(cachebUser.getMobilePhoneNumber())) {
            newUser.setMobilePhoneNumber(phoneNumber);
        }
        newUser.setRealName(real_name);
        newUser.setIdCard(idCard);
        newUser.update(cachebUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dialog.dismiss();
                if (e == null) {
                    Toast.makeText(UMyInfomationActivity.this, "修改资料成功", Toast.LENGTH_SHORT).show();
                    // 更新UserDqInfomation表（cacheUsername 是修改关键字）
                    updataUserDqInfomation(cacheUsername);
                    // 设置 EditText 为 未激活状态
                    setEditTextEnable(false);
                } else {
                    Toast.makeText(UMyInfomationActivity.this, "修改失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
