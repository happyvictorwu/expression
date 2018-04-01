package com.yuxuan.admin.expression.entity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.entity
 * 文件名:   MyOrdersData
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/31 15:00
 * 描述:     订单实体
 */

public class MyOrdersData {
    private String username;
    private String addr;
    private String phoneNumber;

    private String other;
    private String status;



    public String getOther() {
        return other;
    }
    public void setOther(String other) {
        this.other = other;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
