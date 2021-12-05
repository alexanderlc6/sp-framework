package com.sp.framework.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * MD5加密类（封装jdk自带的md5加密方法）
 *
 * @author lzb
 * @date
 */
public class UrlSignUtil {

    public static String encrypt(String source) {
        return encodeMd5(source.getBytes());
    }

    private static String encodeMd5(byte[] source) {
        try {
            return encodeHex(MessageDigest.getInstance("MD5").digest(source));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static String encodeHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buffer.append("0");
            }
            buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buffer.toString();
    }

    /**
     * 排序方法
     */
    public static String sort(String token, String timestamp, String body) {
        String[] strArray = {token, timestamp, body};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(",");
            sb.append(str);
        }
        return sb.toString().replaceFirst(",", "");
    }

    public static String format(String url, String sort, String signKey) {
        String arr[] = {url, sort, signKey};
        return StringUtils.join(arr, ",");
    }

}
