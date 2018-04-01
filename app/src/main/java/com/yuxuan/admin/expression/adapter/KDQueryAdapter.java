package com.yuxuan.admin.expression.adapter;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.adapter
 * 文件名:   KDQueryAdapter
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/31 10:28
 * 描述:     快递查询
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.CourierData;

import java.util.List;

public class KDQueryAdapter extends BaseAdapter{

    private Context mContext;
    private List<CourierData> mList;
    //布局加载器
    private LayoutInflater inflater;
    private CourierData data;

    public KDQueryAdapter(Context mContext, List<CourierData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //第一次加载
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_courier_item, null);
            viewHolder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            viewHolder.tv_zone = (TextView) convertView.findViewById(R.id.tv_zone);
            viewHolder.tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime);
            //设置缓存
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置数据
        data = mList.get(position);
        viewHolder.tv_remark.setText(data.getRemark());
        viewHolder.tv_zone.setText(data.getZone());
        viewHolder.tv_datetime.setText(data.getDatatime());
        return convertView;
    }

    class ViewHolder {
        private TextView tv_remark;
        private TextView tv_zone;
        private TextView tv_datetime;


    }
}
