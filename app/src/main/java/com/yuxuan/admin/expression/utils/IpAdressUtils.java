package com.yuxuan.admin.expression.utils;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.utils
 * 文件名:   IpAdressUtils
 * 创建者:   YUXUAN
 * 创建时间: 2018/3/30 7:24
 * 描述:     获取服务器IP地址
 */

public class IpAdressUtils {
    private final static String url = "http://192.168.1.107:8080";
    // private final static String url = "http://192.168.43.38:8080";
    // private final static String url = "http://192.168.43.38:8080";

    public IpAdressUtils() {

    }

    public static String getURL() {

        return url;
    }
}
