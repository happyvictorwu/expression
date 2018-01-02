package com.yuxuan.admin.expression.adapter;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.adapter
 * 文件名:   KDTeamAdapter
 * 创建者:   YUXUAN
 * 创建时间: 2018/1/2 16:03
 * 描述:
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuxuan.admin.expression.R;
import com.yuxuan.admin.expression.entity.KDTeamData;
import com.yuxuan.admin.expression.utils.UtilTools;

import java.util.List;

public class KDTeamAdapter extends BaseAdapter {
    private Context mContext;
    private List<KDTeamData> data;
    private LayoutInflater inflater;

    public KDTeamAdapter(Context mContext, List<KDTeamData> data) {
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
        return data.get(position);
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
            convertView = inflater.inflate(R.layout.item_kd_team,parent, false);
            viewHolder.iv_kd_head = (ImageView) convertView.findViewById(R.id.iv_kd_head);
            viewHolder.tv_team_name = (TextView) convertView.findViewById(R.id.tv_team_name);
            viewHolder.tv_team_property = (TextView) convertView.findViewById(R.id.tv_team_property);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String imgString = data.get(position).getTeam_head();
        Bitmap bm = UtilTools.stringToBitmap(imgString);
        viewHolder.iv_kd_head.setImageBitmap(bm);
        viewHolder.tv_team_name.setText(data.get(position).getTeam_name());
        viewHolder.tv_team_property.setText(data.get(position).getTeam_property());



        return convertView;
    }

    class ViewHolder {
        private ImageView iv_kd_head;
        private TextView tv_team_name;
        private TextView tv_team_property;
    }
}
