package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   UMyPaiJianActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/31 15:24
 * 描述:     我的派件界面
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.adapter.MySendOrdersAdapter;
import com.yuxuan.admin.expression.entity.MyOrdersData;
import com.yuxuan.admin.expression.entity.MyUser;
import com.yuxuan.admin.expression.entity.UserDqInfomation;
import com.yuxuan.admin.expression.ui.BaseActivity;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.StaticClass;
import com.yuxuan.admin.expression.utils.UtilTools;
import com.yuxuan.admin.expression.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UMyPaiJianActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_send_orders;

    private TextView tv_send_sum;

    private CustomDialog dialog;

    private CustomDialog displayList;
    private TextView tv_username;
    private TextView tv_addr;
    private TextView tv_phone;
    private TextView tv_other;
    private TextView tv_status;

    private List<MyOrdersData> data = new ArrayList<MyOrdersData>();
    private List<String> discripte = new ArrayList<String>();

    private Handler hander = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticClass.LOAD_DATA_OK:
                    MySendOrdersAdapter adapter = new MySendOrdersAdapter(UMyPaiJianActivity.this, data);
                    adapter.notifyDataSetChanged();
                    lv_send_orders.setAdapter(adapter);
                    L.i("send dataSize = " + data.size());
                    tv_send_sum.setText("当前有" + data.size() + "个快递需要你去派送");
                    break;
                case StaticClass.LOAD_DATA_FAIL:
                    tv_send_sum.setText("还没有人找过你取件");
                    lv_send_orders.setAdapter(null);
                    dialog.dismiss();
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_my_paijian);

        initView();
        initData();

    }

    private void initView() {
        dialog = new CustomDialog(this, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER,
                R.style.pop_anim_style);

        int width = (int) (UtilTools.getWindowWidth(this) * 0.7);
        int height = (int) (UtilTools.getWindowHeigth(this) * 0.5);
        // 显示详细信息的dialog
        displayList = new CustomDialog(this, width, height, R.layout.dialog_send_display_list, R.style.Theme_dialog,
                Gravity.CENTER, R.style.pop_anim_style);

        tv_send_sum = (TextView) findViewById(R.id.tv_send_sum);

        tv_username = (TextView) displayList.findViewById(R.id.tv_username);
        tv_addr = (TextView) displayList.findViewById(R.id.tv_addr);
        tv_phone = (TextView) displayList.findViewById(R.id.tv_phone);
        tv_other = (TextView) displayList.findViewById(R.id.tv_other);
        tv_status = (TextView) displayList.findViewById(R.id.tv_status);

        lv_send_orders = (ListView) findViewById(R.id.lv_send_orders);
        lv_send_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayList.show();
                tv_username.setText(data.get(position).getUsername() + "的快件");
                tv_addr.setText(data.get(position).getAddr());
                tv_phone.setText(data.get(position).getPhoneNumber());
                tv_status.setText(data.get(position).getStatus());
                tv_other.setText(data.get(position).getOther());


            }
        });

    }

    // 从bmob获取数据
    private void initData() {
        dialog.show();
        // 查询所有下单代取的用户
        BmobQuery<UserDqInfomation> query = new BmobQuery<UserDqInfomation>();
        query.addWhereEqualTo("success", false);
        query.findObjects(new FindListener<UserDqInfomation>() {
            @Override
            public void done(List<UserDqInfomation> object, BmobException e) {
                if (e == null) {
                    L.i("queryAllData = " + object.size());

                    for (final UserDqInfomation u : object) {
                        String dq_phone = u.getDq_phone();

                        //查询当前用户需要派送的用户
                        BmobQuery<MyUser> q = new BmobQuery<MyUser>();
                        q.addWhereEqualTo("mobilePhoneNumber", dq_phone);
                        q.findObjects(new FindListener<MyUser>() {
                            @Override
                            public void done(List<MyUser> object, BmobException e) {
                                dialog.dismiss();
                                if (e == null){
                                    MyOrdersData buffer;
                                    String sendUsername = object.get(0).getUsername();
                                    String username = BmobUser.getCurrentUser().getUsername();
                                    L.i(username + "--" + sendUsername);
                                    if (sendUsername.equals(username)){
                                        String rusername = u.getUsername();
                                        String addr = u.getAddr();
                                        String phone = u.getPhone();
                                        String status = u.getStatus();
                                        String other = u.getOther();

                                        buffer = new MyOrdersData();
                                        buffer.setUsername(rusername);
                                        buffer.setAddr(addr);
                                        buffer.setPhoneNumber(phone);
                                        buffer.setOther(other);
                                        buffer.setStatus(status);
                                        data.add(buffer);
                                        hander.sendEmptyMessage(StaticClass.LOAD_DATA_OK);
                                    }
                                }else{
                                    L.i("querDqUsername fail:" + e.getMessage());
                                    hander.sendEmptyMessage(StaticClass.LOAD_DATA_FAIL);
                                    Toast.makeText(UMyPaiJianActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                } else {
                    L.i("queryteamFlag fail：" + e.getMessage());
                    hander.sendEmptyMessage(StaticClass.LOAD_DATA_FAIL);
                    Toast.makeText(UMyPaiJianActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0:

                break;

            default:
                break;
        }

    }
}
