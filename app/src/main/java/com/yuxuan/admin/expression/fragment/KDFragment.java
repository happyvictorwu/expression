package com.yuxuan.admin.expression.fragment;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.fragment
 * 文件名:   KDFragment
 * 创建者:   YUXUAN
 * 创建时间: 2018/1/1 19:30
 * 描述:     代取页面Fragment
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.activity.KDDQInfomationActivity;
import com.yuxuan.admin.expression.activity.KDFoundTeamAndPersonActivity;
import com.yuxuan.admin.expression.activity.KDQueryActivity;
import com.yuxuan.admin.expression.activity.KDZhuanQianActivity;
import com.yuxuan.admin.expression.adapter.KDTeamAdapter;
import com.yuxuan.admin.expression.entity.KDTeamData;
import com.yuxuan.admin.expression.entity.MyUser;
import com.yuxuan.admin.expression.utils.L;
import com.yuxuan.admin.expression.utils.UtilTools;
import com.yuxuan.admin.expression.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class KDFragment extends Fragment implements View.OnClickListener {


    private ListView lv_deliveTeam;
    private List<KDTeamData> data = new ArrayList<KDTeamData>();

    // 找代取
    private Button bt_find_collection;
    //我来赚钱
    private Button bt_give_money;
    //领蜜豆
    private Button bt_card;

    //点击 listView item 后显示的Dialog
    private CustomDialog dialog;

    //加载数据时候显示的Dialog
    private CustomDialog loadDialog;

    private EditText et_nichen;
    private EditText et_mail;
    private EditText et_property;
    private EditText et_phoneNumber;

    // 就选他了 按钮
    private Button bt_ok;

    // 初始化数据完成
    private static final int INIT_DATA_OK = 201;

    // 下面是图片滚动所需要的变量
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private ViewPager advPager = null;
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INIT_DATA_OK:
                    L.i("flagIsOnedata----" + data.size());
                    KDTeamAdapter adapter = new KDTeamAdapter(getActivity(), data);
                    adapter.notifyDataSetChanged();
                    lv_deliveTeam.setAdapter(adapter);
                    // 获取listView 的高度，设置所有item显示出来
                    UtilTools.setListViewHeightBasedOnChildren(lv_deliveTeam);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kd, container, false);
        findView(view);
        initViewPager(view);  //广告加载
        return view;
    }



    // 初始化布局 未完成
    private void findView(View view) {

        loadDialog = new CustomDialog(getActivity(), WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_loading, R.style.loading_dialog,
                Gravity.CENTER, R.style.pop_anim_style);
        loadDialog.setCancelable(true);

        bt_find_collection = (Button) view.findViewById(R.id.bt_find_collection);
        bt_find_collection.setOnClickListener(this);

        bt_give_money = (Button) view.findViewById(R.id.bt_give_money);
        bt_give_money.setOnClickListener(this);

        bt_card = (Button) view.findViewById(R.id.bt_card);
        bt_card.setOnClickListener(this);

        // 获取屏幕宽高
        int width = (int) (UtilTools.getWindowWidth(getActivity()) * 0.8);
        int height = (int) (UtilTools.getWindowHeigth(getActivity()) * 0.6);

        dialog = new CustomDialog(getActivity(), width, height, R.layout.dialog_team_dq, R.style.Theme_dialog,
                Gravity.CENTER, R.style.pop_anim_style);

        dialog.setCancelable(true);

        et_nichen = (EditText) dialog.findViewById(R.id.et_nichen);
        et_mail = (EditText) dialog.findViewById(R.id.et_mail);
        et_property = (EditText) dialog.findViewById(R.id.et_property);
        et_phoneNumber = (EditText) dialog.findViewById(R.id.et_phoneNumber);

        bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);

        lv_deliveTeam = (ListView) view.findViewById(R.id.lv_team);
        getData();
        lv_deliveTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String team_name = data.get(position).getTeam_name();
                setDataToView(team_name);
                dialog.show();

            }
        });

    }

    /**
     * 设置数据到点击 listView item 后显示的Dialog 上
     * 设置昵称
     * 设置邮箱
     * 设置手机号
     * 设置代取性质
     * @param team_name 团队名称
     */
    private void setDataToView(String team_name) {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", team_name);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> users, BmobException e) {
                if (e == null) {
                    MyUser user = users.get(0);
                    String username = user.getUsername();
                    String phoneNumber = user.getMobilePhoneNumber();
                    String email = user.getEmail();
                    String property = user.getTeamFlag();
                    if (property.equals("1")) {
                        property = "个人代取";
                    } else {
                        property = "团队代取";
                    }
                    et_nichen.setText(username);
                    et_mail.setText(email);
                    et_phoneNumber.setText(phoneNumber);
                    et_property.setText(property);

                    String mPhone = BmobUser.getCurrentUser().getMobilePhoneNumber();
                    if( phoneNumber.equals(mPhone)){
                        bt_ok.setText("不能选择自己");
                        bt_ok.setEnabled(false);
                    }else{
                        bt_ok.setText("就选他了");
                        bt_ok.setEnabled(true);
                    }

                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 从服务器获取数据
     * 获取头像
     * 获取用户名
     * 获取代取的性质（团队代取 or 个人代取）
     */
    private void getData() {
        loadDialog.show();
        List<BmobQuery<MyUser>> queries = new ArrayList<BmobQuery<MyUser>>();
        BmobQuery<MyUser> q2 = new BmobQuery<MyUser>();
        q2.addQueryKeys("teamFlag");
        queries.add(q2);

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.or(queries).order("-createdAt").findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object, BmobException e) {
                loadDialog.dismiss();
                if (e == null) {
                    L.i("getData size = " + object.size());
                    data.clear();
                    KDTeamData bufferData = null;
                    MyUser user = null;
                    for (int i = 0; i < object.size(); i++) {
                        bufferData = new KDTeamData();
                        user = object.get(i);
                        //判断是否注册
                        if (user.getTeamFlag() == null ){
                            continue;
                        }
                        bufferData.setTeam_name(user.getUsername().toString());
                        //判断是个人还是团队
                        if (user.getTeamFlag().equals("1")) {
                            bufferData.setTeam_property("个人代取");
                        } else {
                            bufferData.setTeam_property(user.getTeamFlag() + "团队");
                        }
                        //判断是否有头像，如果没有，则用logo代替
                        if(user.getImgHead() == null){
                            BitmapDrawable bitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.logo);
                            String logoString = UtilTools.bitmapToString(getActivity(), bitmap);
                            bufferData.setTeam_head(logoString);
                        }else{
                            bufferData.setTeam_head(user.getImgHead().toString());
                        }
                        data.add(bufferData);
                    }
                    handler.sendEmptyMessage(INIT_DATA_OK);
                } else {
                    L.i("getData 失败");
                }
            }
        });
    }


    // 初始化滚动图片中的图片
    private void initViewPager(View view) {
        advPager = (ViewPager) view.findViewById(R.id.adv_pager);
        ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup);

        List<View> advPics = new ArrayList<View>();

        ImageView img1 = new ImageView(getActivity());
        img1.setBackgroundResource(R.drawable.advertising_default_3);
        advPics.add(img1);

        ImageView img2 = new ImageView(getActivity());
        img2.setBackgroundResource(R.drawable.advertising_default_1);
        advPics.add(img2);

        ImageView img3 = new ImageView(getActivity());
        img3.setBackgroundResource(R.drawable.advertising_default_2);
        advPics.add(img3);

        ImageView img4 = new ImageView(getActivity());
        img4.setBackgroundResource(R.drawable.advertising_default);
        advPics.add(img4);

        imageViews = new ImageView[advPics.size()];

        for (int i = 0; i < advPics.size(); i++) {
            imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            imageView.setPadding(5, 5, 5, 5);
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.banner_dian_focus);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
            }
            group.addView(imageViews[i]);
        }

        advPager.setAdapter(new AdvAdapter(advPics));
        advPager.addOnPageChangeListener(new GuidePageChangeListener());
        advPager.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (isContinue) {
                        viewHandler.sendEmptyMessage(what.get());
                        whatOption();
                    }
                }
            }

        }).start();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击找代取按钮
            case R.id.bt_find_collection:
                startActivity(new Intent(getActivity(), KDFoundTeamAndPersonActivity.class));
                break;
            // 点击我来赚钱按钮
            case R.id.bt_give_money:
                startActivity(new Intent(getActivity(), KDZhuanQianActivity.class));
                break;
            // 点击快递查询
            case R.id.bt_card:
                startActivity(new Intent(getActivity(), KDQueryActivity.class));
                break;
            //点击 "就选他了"按钮
            case R.id.bt_ok:
                String dq_phone = et_phoneNumber.getText().toString().trim();
                Intent intent = new Intent(getActivity(), KDDQInfomationActivity.class);
                intent.putExtra("dq_phone", dq_phone);
                startActivity(intent);
                dialog.dismiss();
                break;

            default:
                break;
        }

    }

    


    //以下的是广告来自initViewPager
    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

    }
    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            what.getAndSet(arg0);
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0].setBackgroundResource(R.drawable.banner_dian_focus);
                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
                }
            }

        }

    }
    @SuppressLint("HandlerLeak")
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            advPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };
    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {

        }
    }
}
