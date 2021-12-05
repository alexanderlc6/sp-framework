package com.sp.framework.common.utils;

import com.sp.framework.common.utils.encrypt.MD5;

import java.util.Random;

/**
 * 登录Token的生成以及校验工具类
 * @author alexlu
 * @version 1.0
 */
public class TokenUtil {
    /**
     * 根据AccountId获取token
     * @param accountId
     * @return
     */
    public static String getToken(String accountId) {
    	StringBuilder str = new StringBuilder("");
        Long day = 15 * 24 * 60 * 60 * 1000L;
        Long time = System.currentTimeMillis();
        time += day;
        String nonce = StringUtil.getRandString(5);
        StringBuffer temp = new StringBuffer().append(Base64Util.encode(accountId + "-"
                                                                               + time + "-" + nonce
                                                                               + "-"
                                                                               + nonce.hashCode()
                                                                               * time));
        try {
            str.append(MD5Util.md5(temp.toString()) + time + nonce);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    /**
     * 生成Access Token Key
     * @param accTokenValue
     * @return
     */
    public static String getAccessTokenKey(String accTokenValue){
        StringBuilder stbAccTokenKey = new StringBuilder("");
        Long day = 15 * 24 * 60 * 60 * 1000L;
        Long time = System.currentTimeMillis();
        time += day;
        String nonce = StringUtil.getRandString(5);
        try {
            stbAccTokenKey.append(MD5Util.md5(accTokenValue + time + nonce));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stbAccTokenKey.toString();
    }

    /**
     * 验证Token是否正确
     * @param accountId
     * @param token
     * @return
     */
    public static boolean checkToken(String accountId, String token) {
        boolean result = false;
        try {
            if (StringUtil.isEmpty(accountId) || StringUtil.isEmpty(token)
                || accountId.length() != 32 || token.length() != 34) {
                return false;
            }
            Long time = Long.valueOf(token.substring(16, 29));
            String nonce = token.substring(29);
            StringBuffer temp = new StringBuffer()
                .append(MD5Util.md5(Base64Util.encode(accountId + "-" + time + "-"
                                                                 + nonce + "-" + nonce.hashCode()
                                                                 * time))
                        + time + nonce);
            if (temp.toString().equals(token) && time >= System.currentTimeMillis()) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
    /**
     * 根据AccountId获取Token
     * @param accountId
     * @return
     */
    public static String getToken(String accountId,String eqId,long day,String key) {
        StringBuffer str = new StringBuffer("");
        Long ts = day * 24 * 60 * 60 * 1000L;
        Long time = System.currentTimeMillis();
        time += ts;
        String nonce = getRandString(5);
        StringBuffer temp = new StringBuffer().append(new MD5().getMD5ofStr(accountId + new MD5().getMD5ofStr(eqId)+key+ "-"
                                                                               + time + "-" + nonce
                                                                               + "-"
                                                                               + nonce.hashCode()
                                                                               * time));
        try {
            str.append(MD5Util.md5(temp.toString()) + time + nonce);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public static String getRandString(int length) {
        char[] CHARS = "abcdehjkmnpqrsuvwxyzhs0123456789ABCDEFGHIJKMLNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(CHARS[new Random().nextInt(CHARS.length)]);
        }
        return sb.toString();
    }
}
