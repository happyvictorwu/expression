package com.yuxuan.admin.expression.entity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.entity
 * 文件名:   KDQueryData
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 16:40
 * 描述:     快递查询信息实体类
 */

public class KDQueryData {

    private String datetime;
    private String remark;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
