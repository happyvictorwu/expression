package com.yuxuan.admin.expression.activity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.activity
 * 文件名:   KDQueryActivity
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 16:34
 * 描述:     快递查询
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.adapter.CompaniesAdapter;
import com.yuxuan.admin.expression.entity.CompaniesNo;
import com.yuxuan.admin.expression.entity.KDQueryData;
import com.yuxuan.admin.expression.ui.BaseActivity;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.StaticClass;
import com.yuxuan.admin.expression.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.ErrorListener;

public class KDQueryActivity extends BaseActivity {

//    private Button btn_query;
//    private EditText et_number;
//    private ListView lv_display;
//    private Spinner sp_firm;
//
//    private List<KDQueryData> kDQueryData;
//    private List<CompaniesNo> companies = new ArrayList<>();
//    String[] mItems;
//
//    private CustomDialog dialog;
//
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler(){
//        public void handleMessage(Message msg) {
//            if (msg.what == StaticClass.LOAD_DATA_OK){
//                sp_firm.setAdapter(new CompaniesAdapter(KDQueryActivity.this, companies));
//            }
//        };
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_kd_query);
//        //加载数据
//        loadData();
        //初始化布局
//        initView();
    }

//    //初始化数据
//    private void loadData() {
//        //获取所有可以查询的快递名称及其编号
//        String url = "http://v.juhe.cn/exp/com?key=+" + StaticClass.KD_QUERY_KEY;
//        RequestQueue mQueue = Volley.newRequestQueue(KDQueryActivity.this);
//        StringRequest request = new StringRequest(url, new Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                L.i(response);
//                //解析json
//                parsingCompaniesJson(response);
//            }
//
//        }, new ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(KDQueryActivity.this, error + "", Toast.LENGTH_LONG).show();
//
//            }
//        });
//        mQueue.add(request);
//    }
//
//    //初始化布局
//    private void initView() {
//
//        dialog = new CustomDialog(this, WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER,
//                R.style.pop_anim_style);
//
//        dialog.setCancelable(false);
//
//        btn_query = (Button) findViewById(R.id.btn_query);
//        btn_query.setOnClickListener(this);
//        et_number = (EditText) findViewById(R.id.et_number);
//        sp_firm = (Spinner) findViewById(R.id.sp_firm);
//
//        lv_display = (ListView) findViewById(R.id.lv_display);
//        lv_display.setDividerHeight(0);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_query:
//                // String firm = et_firm.getText().toString().trim();
//                String firm = companies.get(sp_firm.getSelectedItemPosition()).getNo();
//                String number = et_number.getText().toString().trim();
//                if (!TextUtils.isEmpty(firm) && !TextUtils.isEmpty(number)) {
//                    queryKD(firm, number);
//
//                } else {
//                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            default:
//                break;
//        }
//    }


//}
