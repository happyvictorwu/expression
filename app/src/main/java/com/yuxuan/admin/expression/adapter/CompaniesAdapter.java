package com.yuxuan.admin.expression.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.CompaniesNo;

import java.util.List;

/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.adapter
 * 文件名:   CompaniesAdapter
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 16:42
 * 描述:     快递公司的 Spinner
 */

public class CompaniesAdapter extends BaseAdapter {
    private List<CompaniesNo> mList;
    private Context mContext;
    private LayoutInflater inflater;

    public CompaniesAdapter(Context pContext, List<CompaniesNo> pList) {
        this.mContext = pContext;
        this.mList = pList;
        inflater = LayoutInflater.from(this.mContext);
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

        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sp_company,parent, false);
            viewHolder.tv_sp_company = (TextView) convertView.findViewById(R.id.tv_sp_company);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tv_sp_company.setText(mList.get(position).getCom());



        return convertView;
    }

    class ViewHolder {
        private TextView tv_sp_company;
    }
}
