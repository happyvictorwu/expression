package com.yuxuan.admin.expression.entity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.entity
 * 文件名:   UserDqInfomation
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 12:47
 * 描述:     bishe
 */

import cn.bmob.v3.BmobObject;

public class UserDqInfomation extends BmobObject {

    private static final long serialVersionUID = 1L;
    private String username;
    private String status;
    private String phone;
    private String other;
    private String name;
    private String addr;
    private Boolean success;
    private String dq_phone;
    private String comment;




    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDq_phone() {
        return dq_phone;
    }

    public void setDq_phone(String dq_phone) {
        this.dq_phone = dq_phone;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
