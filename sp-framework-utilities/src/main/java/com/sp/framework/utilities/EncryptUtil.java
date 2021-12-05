/**
 * Copyright (c) 2012 Yonghui All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yonghui.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Yonghui.
 *
 * YONGHUI MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. YONGHUI SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.sp.framework.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sp.framework.utilities.convertor.Base64Convertor;
import com.sp.framework.utilities.encryptor.EncryptionException;
import com.sp.framework.utilities.encryptor.Encryptor;

/**
 * 常用加解密，hash，digest
 * @author liuliu
 *
 */
public class EncryptUtil {
	private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);	
	
	public static final String ENCRYPTOR_PREFIX = "encryptor.";
	
	public static final int DEFAULT_ITERATIONS = 1024;
	
	public static final String DEFAULT_ENCRYPT_ALGORITHM = "AES";
	public static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA1withDSA";
	public static final String DEFAULT_RANDOM_ALGORITHM = "SHA1PRNG";
	public static final String DEFAULT_HASH_ALGORITHM = "SHA-512";
	public static final String DEFAULT_DIGEST_ALGORITHM = "MD5";
	
	private static EncryptUtil instance = null;
	
	
	private Encryptor encryptor = null;
	private Base64Convertor base64Convertor = new Base64Convertor();
	private ThreadLocal<MessageDigest> hasherContext = new ThreadLocal<MessageDigest>();
	private ThreadLocal<MessageDigest> digesterContext = new ThreadLocal<MessageDigest>();
	
	private EncryptUtil(){
		//init encryptor
		String encryptMethod = ConfigurationUtil.getInstance().getUtilityConfiguration(ConfigurationUtil.ENCRYPT_ALGORITHM_KEY);
		String encryptClassKey = ENCRYPTOR_PREFIX + (encryptMethod == null? DEFAULT_ENCRYPT_ALGORITHM : encryptMethod);
		String encryptClass = ConfigurationUtil.getInstance().getUtilityConfiguration(encryptClassKey);
		if(encryptClass == null){
			logger.error("Encryptor initialization fail. No registration found for {}" +
					" so no further encrypt/decrypt operation can be success.", encryptClassKey);
		}
		try {
			encryptor = (Encryptor)Class.forName(encryptClass).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Encryptor initialization fail. {} can not be initialized.", encryptClassKey);
		}
		
	}
	
	public static synchronized EncryptUtil getInstance(){
		if(instance == null)
			instance = new EncryptUtil();
		return instance;
	}
	
	public String hash(String plainText, String salt){
		return hash(plainText, salt, DEFAULT_ITERATIONS);
	}
	
	public String hash(String plainText, String salt, int iterations){
		byte[] bytes = hashNative(plainText, salt, iterations);
		if(bytes == null) return null;
		return base64Convertor.format(bytes);
	}
	
	private MessageDigest getHasher() {
		String hashAlgorithm = ConfigurationUtil.getInstance().getUtilityConfiguration(ConfigurationUtil.HASH_ALGORITHM_KEY);
		try {
			return MessageDigest.getInstance(hashAlgorithm == null? DEFAULT_HASH_ALGORITHM : hashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			logger.error("No algorithm found for digest with name {}", 
					hashAlgorithm == null? DEFAULT_HASH_ALGORITHM : hashAlgorithm);
			logger.error("Digester for Hash initialzation fail, so no further hash operation can be success");
		}
		return null;
	}
	
	private MessageDigest getDigester() {
		String digestAlgorithm = ConfigurationUtil.getInstance().getUtilityConfiguration(ConfigurationUtil.DIGEST_ALGORITHM_KEY);
		try {
			return MessageDigest.getInstance(digestAlgorithm == null? DEFAULT_DIGEST_ALGORITHM : digestAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			logger.error("No algorithm found for digest with name {}", 
					digestAlgorithm == null? DEFAULT_DIGEST_ALGORITHM : digestAlgorithm);
			logger.error("Digester initialzation fail, so no further digest operation can be success");
		}
		return null;
	}
	
	private byte[] hashNative(String plainText, String salt, int iterations){
		if(plainText == null) return null;
		try{
			MessageDigest hasher = hasherContext.get();
			if(hasher == null){
				hasher = getHasher();
				hasherContext.set(hasher);
			}
			
			hasher.reset();			
			if(salt != null)
				hasher.update(salt.getBytes(ConfigurationUtil.DEFAULT_ENCODING));
			hasher.update(plainText.getBytes(ConfigurationUtil.DEFAULT_ENCODING));
			
			byte[] bytes = hasher.digest();
			for (int i = 0; i < iterations; i++) {
				hasher.reset();
				bytes = hasher.digest(bytes);
			}
			return bytes;
		} catch (UnsupportedEncodingException e) {
			logger.error("Get Bytes for digest data error with UTF-8");
			throw new RuntimeException("Get Bytes for digest data error with UTF-8");
		}
	}
	
	public String encrypt(String plainText) throws EncryptionException{
		if(encryptor == null)
			throw new RuntimeException("Encryptor does not initiate properly.");
		return encryptor.encrypt(plainText);
	}
	
	public String decrypt(String cipherText) throws EncryptionException{
		if(encryptor == null)
			throw new RuntimeException("Encryptor does not initiate properly.");
		return encryptor.decrypt(cipherText);
	}
	
	public String encrypt(String plainText,String masterkey) throws EncryptionException{
		if(encryptor == null)
			throw new RuntimeException("Encryptor does not initiate properly.");
		return encryptor.encrypt(plainText,masterkey);
	}
	
	public String decrypt(String cipherText,String masterkey) throws EncryptionException{
		if(encryptor == null)
			throw new RuntimeException("Encryptor does not initiate properly.");
		return encryptor.decrypt(cipherText,masterkey);
	}
	
	public String sign(String data){
		//TODO
		return null;
	}
	
	public boolean verifySign(String signature, String data){
		//TODO
		return false;
	}
	
	public String digest(String data){
		if(data == null) return null;
		try {
			MessageDigest digester = digesterContext.get();
			if(digester == null){
				digester = getDigester();
				digesterContext.set(digester);
			}
			digester.reset();
			return StringUtil.bytes2String(digester.digest(data.getBytes(ConfigurationUtil.DEFAULT_ENCODING)));
		} catch (UnsupportedEncodingException e) {
			logger.error("Get Bytes for digest data error with UTF-8");
			throw new RuntimeException("Get Bytes for digest data error with UTF-8");
		}
	}
	
	public static SecureRandom getSecureRandom(){
		String randomAlgorithm = ConfigurationUtil.getInstance().
				getUtilityConfiguration(ConfigurationUtil.RANDOM_ALGORITHM_KEY);
		try {			
			if(randomAlgorithm == null) randomAlgorithm = DEFAULT_RANDOM_ALGORITHM;
			SecureRandom random = SecureRandom.getInstance(randomAlgorithm);		
			return random;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Get SecureRandom Implementation Error with Algorithem " + randomAlgorithm);
		}
	}
	
	public String base64Encode(String source){
		
		if(StringUtils.isBlank(source))
			return null;
		else
			return base64Convertor.format(source.getBytes());
	}
	
	public String base64Decode(String source){
		
		if(StringUtils.isBlank(source))
			return null;
		else
			return new String(base64Convertor.parse(source));
	}
	
}
