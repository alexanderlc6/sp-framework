package com.sp.framework.common.utils.encrypt;

/**
 * Created by Alexa on 2017/8/7.
 */

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class AesUtil {
    private static final byte[] KEY = new byte[]{-76, -117, 50, 46, 99, -62, 86, 83, 28, 116, -117, -98, 95, 28, -72, 61, -12, 62, -97, -119, 110, 100, -77, 7, -78, -76, -29, 9, -16, -110, -75, -8};
    private static final byte[] IV = new byte[]{37, -83, -45, -45, 67, 57, 55, 23, -28, 69, 121, -61, -109, -85, -114, -16};

    public AesUtil() {
    }

    public static String encrypt(String sSrc) throws Exception {
        return encrypt(sSrc, KEY, IV);
    }

    public static String decrypt(String sSrc) throws Exception {
        return decrypt(sSrc, KEY, IV);
    }

    public static String encrypt(String sSrc, byte[] key, byte[] iv) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec _iv = new IvParameterSpec(iv);
        cipher.init(1, skeySpec, _iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String sSrc, byte[] key, byte[] iv) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec _iv = new IvParameterSpec(iv);
        cipher.init(2, skeySpec, _iv);
        byte[] encrypted = Base64.getDecoder().decode(sSrc);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original, "utf-8");
    }

    public static String encrypt(String sSrc, String keyStr) throws Exception {
        byte[] key = generalKey(keyStr);
        byte[] iv = generalIV(keyStr);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec _iv = new IvParameterSpec(iv);
        cipher.init(1, skeySpec, _iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String sSrc, String keyStr) throws Exception {
        byte[] key = generalKey(keyStr);
        byte[] iv = generalIV(keyStr);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec _iv = new IvParameterSpec(iv);
        cipher.init(2, skeySpec, _iv);
        byte[] encrypted = Base64.getDecoder().decode(sSrc);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original, "utf-8");
    }

    public static byte[] generalKey(String keyStr) throws Exception {
        byte[] bytes = keyStr.getBytes("utf-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);
        return md.digest();
    }

    public static byte[] generalIV(String keyStr) throws Exception {
        byte[] bytes = keyStr.getBytes("utf-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        return md.digest();
    }

    public static void main(String[] args) {
        String srcContent = "{\"data\":[{\"sku\":{\"attrList\":{\"_lx预设属性外部键值1574735313137\":\"[{\\\"null_SP159342\\\":\\\"this is SPU\\\"}]\"},\"code\":\"null18170_655\",\"extensionCode\":\"lxsku对象外部编码157473739287312\"},\"tenantCode\":\"88001908\"}],\"msgId\":\"1574761280067-686-804\",\"tenantCode\":\"88001908\"}";
        try {
            System.out.println(encrypt(srcContent));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
