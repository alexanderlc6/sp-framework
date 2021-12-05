package com.sp.framework.common.utils;


import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.UUID;

/**
 * Created by wangwei on 2016/11/7.
 */
public class SecurityUtil {
    /**
     * SIGNAL 用在token结尾做验证用的
     */
    public static final String SIGNAL = "USER_CENTER";
    /**
     * 对称加密密码
     */
    public static final String AES_PASSWORD = "513423$@#$SDF$%^^%*&_GBVCX+XBDSA";
    static byte[] ivBuff = {10, 1, 11, 5, 4, 15, 7, 9, 23, 3, 1, 6, 8, 12, 13, 91};
    static byte[] saltBuff = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static String hexStr = "0123456789ABCDEF";
    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public static String generateShortUuid(String token) {
        StringBuffer shortBuffer = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            String str = token.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * AES对称加密
     *
     * @return
     */
    public static String generateToken(String content,String expireTime) {
        String t = content + ";" + "TIMESHOT=" + System.currentTimeMillis()+ ";" + "APPEXPIRE=" +expireTime + ";"+ "UUID=" +UUID.randomUUID() + ";"+ SIGNAL;
        return encrypt(t, AES_PASSWORD);
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encrypt(String content, String password) {
        try {
            PBEParametersGenerator generator = new PKCS5S2ParametersGenerator();
            generator.init(password.getBytes(), saltBuff, 10000);
            KeyParameter params = (KeyParameter) generator.generateDerivedParameters(128);

            byte[] endcoded = params.getKey();
            SecretKeySpec key = new SecretKeySpec(endcoded, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

            cipher.init(1, key, new IvParameterSpec(ivBuff));
            byte[] result = cipher.doFinal(content.getBytes("utf-8"));
            return binaryToHexString(result);
        } catch (Exception e) {
            System.out.println("Encrypt failed.");
        }
        return null;
    }

    public static String decrypt(String content, String password) {
        byte[] bytes = HexStringToBinary(content);
        try {
            PBEParametersGenerator generator = new PKCS5S2ParametersGenerator();
            generator.init(password.getBytes(), saltBuff, 10000);
            KeyParameter params = (KeyParameter) generator.generateDerivedParameters(128);
            byte[] endcoded = params.getKey();
            SecretKeySpec key = new SecretKeySpec(endcoded, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(2, key, new IvParameterSpec(ivBuff));
            byte[] result = cipher.doFinal(bytes);
            return new String(result);
        } catch (Exception e) {
            System.out.println("Decrypt failed.");
        }
        return null;
    }

    public static String binaryToHexString(byte[] bytes) {

        String result = "";
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            // 字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
            // 字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
            result += hex;
        }
        return result;
    }

    public static byte[] HexStringToBinary(String hexString) {
        // hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;// 字节高四位
        byte low = 0;// 字节低四位

        for (int i = 0; i < len; i++) {
            // 右移四位得到高位
            high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);// 高地位做或运算
        }
        return bytes;
    }


}
