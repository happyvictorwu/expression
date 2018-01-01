package com.yuxuan.admin.expression.fragment;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.fragment
 * 文件名:   KDFragment
 * 创建者:   YUXUAN
 * 创建时间: 2018/1/1 19:30
 * 描述:     bishe
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuxuan.admin.expression.R;

public class KDFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kd, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
