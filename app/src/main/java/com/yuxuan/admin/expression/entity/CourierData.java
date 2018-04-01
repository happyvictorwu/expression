package com.yuxuan.admin.expression.entity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.entity
 * 文件名:   CourierData
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/31 10:23
 * 描述:     快递查询的实体
 */

public class CourierData {
    //时间
    private String datatime;
    //状态
    private String remark;
    //城市
    private String zone;

    public String getDatatime() {
        return datatime;
    }

    public void setDatetime(String datatime) {
        this.datatime = datatime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "datatime='" + datatime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
