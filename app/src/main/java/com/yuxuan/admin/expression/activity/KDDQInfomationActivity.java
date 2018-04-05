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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.MyUser;
import com.yuxuan.admin.expression.entity.UserDqInfomation;
import com.yuxuan.admin.expression.ui.BaseActivity;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.UtilTools;
import com.yuxuan.admin.expression.view.CustomDialog;

import java.util.List;

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

    //百度地图地图
    RadarSearchManager mManager;
    //定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kd_dq_infomation);
        //初始化百度地图 周边雷达
        mManager = RadarSearchManager.getInstance();
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


        //声明LocationClient类
        mLocationClient = new LocationClient(KDDQInfomationActivity.this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);


        initLocation();

        //开启定位
        mLocationClient.start();
        L.e("开始定位");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dq_ok:
                if (dataIsRight()) {
                    dialog.show();
                    addInfo();
                }
                break;

            default:
                break;
        }

    }

    //将数据保存到数据库（Bmob）
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
        if (!TextUtils.isEmpty(dq_phone)) {
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


    //百度地图
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        ////可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        int span = 0;
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }


    //定位的回调
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            // GPS定位结果
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
                // 网络定位结果
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
                // 离线定位结果
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            // 位置语义化信息
            sb.append(location.getLocationDescribe());
            // POI数据
            List<Poi> list = location.getPoiList();
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    et_addr.setText(p.getName().trim() + "附近");
                }
            }
            //定位的结果
            L.i(sb.toString());
            L.e("结束定位");

        }
    }
}
