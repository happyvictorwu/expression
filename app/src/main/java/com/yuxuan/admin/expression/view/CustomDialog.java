package com.yuxuan.admin.expression.view;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.view
 * 文件名:   CustomDialog
 * 创建者:   YUXUAN
 * 创建时间: 2018/1/2 15:56
 * 描述:     bishe
 */

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.yuxuan.admin.expression.R;

public class CustomDialog extends Dialog {
    //定义模板
    public CustomDialog(Context context, int layout, int style) {
        this(context, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, layout, style, Gravity.CENTER);;
    }

    //定义属性
    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity, int anim){
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(anim);
    }

    //实例
    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity){

        this(context, width, height, layout, style, gravity, R.style.pop_anim_style);

    }
}
