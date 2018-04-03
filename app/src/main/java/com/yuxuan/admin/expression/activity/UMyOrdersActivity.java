package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   UMyOrdersActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/31 14:59
 * 描述:     订单
 */

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.adapter.MyOrdersAdapter;
import com.yuxuan.admin.expression.demo.AuthResult;
import com.yuxuan.admin.expression.demo.PayResult;
import com.yuxuan.admin.expression.entity.MyOrdersData;
import com.yuxuan.admin.expression.entity.MyUser;
import com.yuxuan.admin.expression.entity.UserDqInfomation;
import com.yuxuan.admin.expression.ui.BaseActivity;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.OrderInfoUtil2_0;
import com.yuxuan.admin.expression.utils.UtilTools;
import com.yuxuan.admin.expression.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UMyOrdersActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_orders;
    private List<MyOrdersData> data = new ArrayList<MyOrdersData>();

    //数据加载成功
    private static final int CACHE_DATA_OK = 200;
    //数据加载失败
    private static final int CACHE_DATA_FAIL = 201;

    private TextView tv_sum;

    private CustomDialog dialog;

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2016080300154232";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA_PRIVATE = "";
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCEhpux2pSgv3bKO9/XtdAwLHsHALjrx3M9OVF4/KlI2ONEPW+fcllhAZpS0vUiw/s9UJXizY1WRkv0bxkRKcZn6k5DhEgsw8QgwL5sjRZlX0C3b6Cfw4IPoRI/jTu9z0bU5n3dODgrP2xYwPs274q+xD+Z5UP8ZXofL1OWutmY2kJsp2XQyLJGMBj6h3voNwHOMqSrlUStDluiAkDnsCwa0vnRAdPJbKFx+K05C5NbfcatmqJyivl4O9hK2qDYUffK3k5ofMGMByP0WLHpLU1MJORUNBSdi8i32PpH2TxI/AvdcIrUspMRarKuoKSUzaWOSlvIXBXUaCyR4Xv3UvpHAgMBAAECggEAS9WzNlz7+9hUI3mLp6Y0kbyCUgmJx/ID8Z7hDjiWrkbeFeiJ/VWz65y9MHPjLpezJuz8OCKjpCDpyd/hGj8rd79ONbh2jJchnhRtYzSBAlU1dUJ9Sa7YuBTVmmQ5MW5w+9dBYtuO2MCcQVV863y7ilpGgsv80WtILJwqWvBlH9/BAGWdEnJrgTh0r+DppiHOVa/GZKiopwC2aXC575t8xaja5/NtHwtWEXx662YfhWGvjZylWjcf/1QWv0xlUPIRmFt1kQNc+aVyZaBagN3PyIhYG6r9UgU3OybdZvNPmya8AJP/79RgoPSEghAgZDOHvjJRMiv3u1bS+O1Ma221UQKBgQDQkDV6IsFdvfs1gLpSvoM38IrAbiCW0c9ZXwB2jy1bpvNSHxCoVsC7G5l+rhYbBhUAY86dt5iepXPpuckfRsdwfy+m6pYkU8OhECC0dP5Gp/k7+F1DNVwVpxSzm+nRHWdOC36eVn3caaV8BUYiNpzOeKq/fkJ2tImt9rzgLyU/LQKBgQCiqwuxi+2Kb1vvh1x2NgjJVjwhiYxYAttOz5SBbMaAVNEHfWsRBuUKvkctgYuL/yvSrQzOofyJxYYSO0EDU87pJBUeGjeBgwJD5Bq3zbcf0/eqBjdVzZSf6IukKnnypFKEusbKrTksvcVCFbW/Gyuxiqq21yaemMXV+ILl2yQnwwKBgCjhe3n7QgfL62erWdpKOZJHdgsOCxlE+u27r22N1wg2C0+3gx5cEt8aI9Yu7WpSoN84sRE9IY9HQxc3MXMN/p3QcFi5dsCcIqZ5cM+udwEGf9X/cthvpyMfYvFpyEUTDGLB4gnLTlRb6PIMXpjqN4ZJJL7wUrrN3Iba7F7NHvHpAoGBAI294XiZ5yhjlIaJOoY+2zUlEUxf4IyoIJTxyANm1yjnvyR5opu5mEbeOoNqiEA63uK6q5g6CiW6mfK08xeFJZx9NnF+grM1hvnFf4p9lH4FY58hv4HimwkjxtoLsSs/CDBj1+ivPEzCJ2pIJJAbit0vaUXc/+feUzT5UIYdh8mlAoGARu0h7rAkSAr1ZAJZtg5w0ynfM2fouWT+FyD4fkA7TzOfuBGgQmvugDK6vyTRIRhNlAvNsbC4UTjGiDwWvNJJ3ZReoZNZP2a4gH0xmRkt8aaGv8B8nkc61ljc1ElEbG52bYhmjA5wYWyZKJMgeYBTqcCWC6D6eN4UjoUlY7LFRJ4=";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    //点击listview item 后弹出的dialog变量
    private CustomDialog displayList;
    private TextView tv_username;
    private TextView tv_addr;
    private TextView tv_phone;
    private Button bt_end;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CACHE_DATA_OK:
                    MyOrdersAdapter adapter = new MyOrdersAdapter(UMyOrdersActivity.this, data);
                    adapter.notifyDataSetChanged();
                    lv_orders.setAdapter(adapter);
                    tv_sum.setText("您当前有" + data.size() + "件快递正在派送");
                    break;
                case CACHE_DATA_FAIL:
                    tv_sum.setText("您还没有下单，赶紧去找人取件吧");
                    lv_orders.setAdapter(null);
                    dialog.dismiss();
                    break;
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(UMyOrdersActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        dealResult();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(UMyOrdersActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(UMyOrdersActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(UMyOrdersActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;

            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_my_orders);
        initView();

    }

    private void initView() {

        dialog = new CustomDialog(this, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_loading, R.style.loading_dialog, Gravity.CENTER,
                R.style.pop_anim_style);
        dialog.setCancelable(true);
        int width = (int) (UtilTools.getWindowWidth(this) * 0.7);
        int height = (int) (UtilTools.getWindowHeigth(this) * 0.5);
        //显示详细信息的dialog
        displayList = new CustomDialog(this, width,
                height, R.layout.dialog_display_list, R.style.Theme_dialog, Gravity.CENTER,
                R.style.pop_anim_style);

        tv_username = (TextView) displayList.findViewById(R.id.tv_username);
        tv_addr = (TextView) displayList.findViewById(R.id.tv_addr);
        tv_phone = (TextView) displayList.findViewById(R.id.tv_phone);
        bt_end = (Button) displayList.findViewById(R.id.bt_end);
        bt_end.setOnClickListener(this);

        tv_sum = (TextView) findViewById(R.id.tv_sum);
        lv_orders = (ListView) findViewById(R.id.lv_orders);

        queryData();
        lv_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayList.show();
                tv_username.setText(data.get(position).getUsername());
                tv_addr.setText(data.get(position).getAddr());
                tv_phone.setText(data.get(position).getPhoneNumber());
            }
        });
    }

    // 查数据
    private void queryData() {
        dialog.show();
        List<BmobQuery<UserDqInfomation>> queries = new ArrayList<BmobQuery<UserDqInfomation>>();
        BmobQuery<UserDqInfomation> q1 = new BmobQuery<UserDqInfomation>();
        q1.addWhereEqualTo("success", false);
        BmobUser user = MyUser.getCurrentUser();
        String username = user.getUsername();
        BmobQuery<UserDqInfomation> q2 = new BmobQuery<UserDqInfomation>();
        q2.addWhereEqualTo("username", username);

        queries.add(q1);
        queries.add(q2);

        BmobQuery<UserDqInfomation> query = new BmobQuery<UserDqInfomation>();
        query.and(queries);
        query.findObjects(new FindListener<UserDqInfomation>() {

            @Override
            public void done(List<UserDqInfomation> object, BmobException e) {

                if (e == null) {
                    if (object.size() == 0) {
                        handler.sendEmptyMessage(CACHE_DATA_FAIL);
                        return;
                    }
                    data.clear();
                    L.i("queryData = " + object.size());
                    MyOrdersData buffer;

                    for (UserDqInfomation u : object) {
                        buffer = new MyOrdersData();
                        buffer.setAddr(u.getAddr());
                        buffer.setPhoneNumber(u.getDq_phone());
                        queryUsername(u.getDq_phone(), buffer);

                    }
                } else {
                    L.i("queryData失败：" + e.getMessage());
                    handler.sendEmptyMessage(CACHE_DATA_FAIL);
                    //Toast.makeText(UMyOrdersActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //查询代取人的用户名（用代取电话查询）
    private void queryUsername(String dq_phone, final MyOrdersData buffer) {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("mobilePhoneNumber", dq_phone);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object, BmobException e) {
                if (e == null) {

                    buffer.setUsername(object.get(0).getUsername() + "  正在为你取件中...");
                    L.i(buffer.getAddr() + "--" + buffer.getPhoneNumber() + "--" + buffer.getUsername());
                    data.add(buffer);
                    handler.sendEmptyMessage(CACHE_DATA_OK);
                } else {
                    L.i("query username fail" + e.getMessage() + e.getErrorCode());
                    Toast.makeText(UMyOrdersActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_end:
                //弹出提示框提醒
                new AlertDialog.Builder(this).setTitle("确认收获？")
                        .setMessage("收到包裹之后才点击确定哦~")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                payV2();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        displayList.dismiss();
                    }
                }).show();
                break;

            default:
                break;
        }

    }

    private void dealResult() {
        L.i("da_phone = " + tv_phone.getText().toString());
        L.i("addr = " + tv_addr.getText().toString());

        List<BmobQuery<UserDqInfomation>> queries = new ArrayList<BmobQuery<UserDqInfomation>>();
        BmobQuery<UserDqInfomation> q1 = new BmobQuery<UserDqInfomation>();
        q1.addWhereEqualTo("dq_phone", tv_phone.getText().toString());
        BmobQuery<UserDqInfomation> q2 = new BmobQuery<UserDqInfomation>();
        q2.addWhereEqualTo("addr", tv_addr.getText().toString());

        BmobQuery<UserDqInfomation> q3 = new BmobQuery<UserDqInfomation>();
        BmobUser user = MyUser.getCurrentUser();
        String username = user.getUsername();
        q3.addWhereEqualTo("username", username);

        queries.add(q1);
        queries.add(q2);
        queries.add(q3);


        //找到objId
        BmobQuery<UserDqInfomation> query = new BmobQuery<UserDqInfomation>();
        query.and(queries);
        //执行查询方法 获得 objId
        query.findObjects(new FindListener<UserDqInfomation>() {

            @Override
            public void done(List<UserDqInfomation> object, BmobException e) {
                if (e == null) {

                    UserDqInfomation udi = new UserDqInfomation();
                    udi.setSuccess(true);
                    //执行更新方法
                    udi.update(object.get(0).getObjectId(), new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                L.i(" success 更新成功");
                                displayList.dismiss();
                                queryData();
                                Toast.makeText(UMyOrdersActivity.this, "代取成功，我们将一直为您服务", Toast.LENGTH_SHORT).show();
                            } else {
                                L.i("bmob 更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                Toast.makeText(UMyOrdersActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    L.i("query dq_phone at UMyOrderActivity 失败：" + e.getMessage() + "," + e.getErrorCode());
                    Toast.makeText(UMyOrdersActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                PayTask alipay = new PayTask(UMyOrdersActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
