package com.yuxuan.admin.expression.adapter;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.adapter
 * 文件名:   MyOrdersAdapter
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/31 15:01
 * 描述:     bishe
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.MyOrdersData;

import java.util.List;

public class MyOrdersAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyOrdersData> data;
    private LayoutInflater inflater;

    public MyOrdersAdapter(Context mContext, List<MyOrdersData> data ) {
        this.mContext = mContext;
        this.data = data;
        inflater = LayoutInflater.from(this.mContext);
    }


    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_my_orders, parent, false);
            holder = new ViewHolder();
            holder.tv_nichen = (TextView) convertView.findViewById(R.id.tv_nichen);
            holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_nichen.setText(data.get(position).getUsername());
        holder.tv_addr.setText(data.get(position).getAddr());
        holder.tv_phone.setText(data.get(position).getPhoneNumber());


        return convertView;
    }

    class ViewHolder {
        TextView tv_nichen;
        TextView tv_addr;
        TextView tv_phone;


    }

}
