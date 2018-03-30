package com.yuxuan.admin.expression.adapter;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.adapter
 * 文件名:   MyInfoAdapter
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 19:06
 * 描述:     bishe
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.InfoItem;

import java.util.List;

public class MyInfoAdapter extends BaseAdapter {
    private List<InfoItem> mdata;
    private Context context;

    public MyInfoAdapter(List<InfoItem> data, Context context) {
        this.mdata = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        Zhujian zhujian = null;
        View view = null;
        if (convertView != null) {
            view = convertView;
            zhujian = (Zhujian) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_myinfo, parent, false);
            zhujian = new Zhujian();
            zhujian.tv_item = (TextView) view.findViewById(R.id.tv_item_myinfo);
            zhujian.iv_item = (ImageView) view.findViewById(R.id.iv_item_util);
            view.setTag(zhujian);
        }

        zhujian.iv_item.setImageResource(mdata.get(position).getIv_item_util());
        zhujian.tv_item.setText(mdata.get(position).getTv_item_myinfo());

        return view;
    }

    class Zhujian {
        TextView tv_item;
        ImageView iv_item;
    }
}
