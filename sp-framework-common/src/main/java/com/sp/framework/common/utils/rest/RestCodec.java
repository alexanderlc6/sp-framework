package com.sp.framework.common.utils.rest;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class RestCodec {
	public static String decodeData(String base64Data) throws RestException {
		try {
			String str = new String(Base64.decodeBase64(base64Data
					.getBytes("utf-8")), "utf-8");
			return str;
		} catch (UnsupportedEncodingException e) {
			throw new RestException(e.getMessage(), e.getCause());
		} finally {
		}
	}

	public static String encodeData(String binaryData) throws RestException {
		try {
			if (null == binaryData) {
				String str = null;
				return str;
			}

			String str = Base64
					.encodeBase64String(binaryData.getBytes("utf-8"));
			return str;
		} catch (UnsupportedEncodingException e) {
			throw new RestException(e.getMessage(), e.getCause());
		} finally {
		}
	}

	/**
	 * encodeData(这里用一句话描述这个方法的作用) 
	 * @param md5Byte
	 * @return 
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	public static String encodeData(byte[] md5Byte) {
		if (md5Byte.length == 0) {
			return null;
		}

		String str = Base64.encodeBase64String(md5Byte);
		return str;
	}
}