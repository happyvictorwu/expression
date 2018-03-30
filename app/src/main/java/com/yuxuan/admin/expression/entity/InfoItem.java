package com.yuxuan.admin.expression.entity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.entity
 * 文件名:   InfoItem
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 18:54
 * 描述:     存储"更多"页面中的图片和文字
 */

public class InfoItem {

    private int iv_item_util;//图片
    private String tv_item_myinfo;//功能文字

    public InfoItem() {}
    public InfoItem(int iv_item_util, String tv_item_myinfo) {
        super();
        this.iv_item_util = iv_item_util;
        this.tv_item_myinfo = tv_item_myinfo;
    }

    public int getIv_item_util() {
        return iv_item_util;
    }
    public void setIv_item_util(int iv_item_util) {
        this.iv_item_util = iv_item_util;
    }
    public String getTv_item_myinfo() {
        return tv_item_myinfo;
    }
    public void setTv_item_myinfo(String tv_item_myinfo) {
        this.tv_item_myinfo = tv_item_myinfo;
    }
}
