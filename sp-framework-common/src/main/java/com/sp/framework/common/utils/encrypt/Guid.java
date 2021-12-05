package com.sp.framework.common.utils.encrypt;

import com.sp.framework.common.utils.MD5Util;

import java.util.UUID;

public class Guid {
    public String app_key;

//    public static void main(String[] args) {
//        Guid gd = new Guid();
//        String app_key = gd.App_key();
//        System.out.println("app_key: " + app_key);
//        String app_secrect = gd.App_secret();
//        System.out.println("app_screct: " + app_secrect);
//    }

    /**
     * @return
     * @description:随机获取key值
     */
    public String guid() {
        UUID uuid = UUID.randomUUID();
        String key = uuid.toString();
        return key;
    }

    /**
     * 这是其中一个url的参数，是GUID的，全球唯一标志符
     *
     * @param
     * @return
     */
    public String App_key() {
        Guid g = new Guid();
        String guid = g.guid();
        app_key = guid;
        return app_key;
    }

    /**
     * 根据md5加密
     *
     * @param
     * @return
     */
    public String App_secret() {
        String mw = "key" + app_key;
        // 得到以后还要用MD5加密
        String app_sign = MD5Util.MD5(mw, "UTF-8").toUpperCase();
        return app_sign;
    }
}
