package com.sp.framework.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** MD5安全工具类
 * @author alexlu
 * @Date 2016年4月20日下午3:45:03
 */
public class MD5Util {
	public static final String CHARSET_UTF8 = "UTF-8";
	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 由于commons提供的方法不能满足
	 */
	public final static String MD5(String s, String charSet) {
		try {
			byte[] btInput = s.getBytes(charSet);

			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			// 使用指定的字节更新摘要
			mdInst.update(btInput);

			// 获得密文
			byte[] md = mdInst.digest();

			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;

			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>>4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int t;
		for (int i = 0; i < 16; i++) {
			t = bytes[i];
			if (t < 0) {
                t += 256;
            }
			sb.append(hexDigits[(t >>> 4)]);
			sb.append(hexDigits[(t % 16)]);
		}
		return sb.toString();
	}

	public static String md5(String input,int bit) throws Exception {
		return code(input, bit);
	}

	public static String md5(String input) throws Exception {
		return code(input, 16);
	}

	public static String code(String input, int bit) throws Exception {
		try {
			MessageDigest md = MessageDigest
					.getInstance(System.getProperty("MD5.algorithm", "MD5"));
			if (bit == 16) {
                return bytesToHex(md.digest(input.getBytes("utf-8"))).substring(8, 24);
            }
			return bytesToHex(md.digest(input.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("Could not found MD5 algorithm.", e);
		}
	}

	public static String md5_3(String b) throws Exception {
		MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
		byte[] a = md.digest(b.getBytes());
		a = md.digest(a);
		a = md.digest(a);

		return bytesToHex(a);
	}
}
