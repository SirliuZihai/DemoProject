package com.zihai.h2Client.util.crypte;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES 对称加/解密
 * 
 * @author jianjun.deng
 *
 */
public class AesCryptoUtil {

	private static final Logger log = LoggerFactory.getLogger(RsaCryptoUtil.class);

	private static final String AES_ECB = "AES/ECB/PKCS5Padding"; // 算法/模式/补码方式

	/**
	 * 加密
	 */
	public static byte[] encrypt(byte[] sSrc, String sKeyByBase64) {
		if (sSrc == null || sSrc.length == 0 || StringUtils.isBlank(sKeyByBase64)) {
			throw new IllegalArgumentException("AES加密数据或Key为空");
		}
		
		byte[] raw = Base64.decode(sKeyByBase64);
		if (raw.length != 16) {
			throw new IllegalArgumentException("AES加密Key长度不是16位");
		}

		try {
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance(AES_ECB);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			return cipher.doFinal(sSrc);
		} catch (Exception ex) {
			log.error("AES加密异常", ex);
			return null;
		}
	}

	/**
	 * 加密(Base64)
	 */
	public static String encrypt(String textSrc, String sKey) {
		if (StringUtils.isBlank(textSrc) || StringUtils.isBlank(sKey)) {
			throw new IllegalArgumentException("AES加密数据或Key为空");
		}
		byte[] encryptBytes = encrypt(textSrc.getBytes(StandardCharsets.UTF_8), sKey);
		return new String(Base64.encode(encryptBytes), StandardCharsets.UTF_8);
	}

	/**
	 * 解密
	 */
	public static byte[] decrypt(byte[] sSrc, String sKeyByBase64) {
		if (sSrc == null || sSrc.length == 0 || StringUtils.isBlank(sKeyByBase64)) {
			throw new IllegalArgumentException("AES解密数据或Key为空");
		}
		byte[] raw = Base64.decode(sKeyByBase64);
		if (raw.length != 16) {
			throw new IllegalArgumentException("AES加密Key长度不是16位");
		}

		try {
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance(AES_ECB);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			return cipher.doFinal(sSrc);
		} catch (Exception ex) {
			log.error("AES解密异常", ex);
			return null;
		}
	}

	/**
	 * 解密(Base64)
	 */
	public static String decrypt(String base64Src, String sKey) {
		if (StringUtils.isBlank(base64Src) || StringUtils.isBlank(sKey)) {
			throw new IllegalArgumentException("AES解密数据或Key为空");
		}
		return new String(decrypt(Base64.decode(base64Src), sKey), StandardCharsets.UTF_8);
	}
	
	public static void main(String[] args) {
		byte[] raw = java.util.Base64.getDecoder().decode("BdXp58SGwShbdBmnvD21dw==");
		for (byte b : raw) {
			System.out.print(b+", ");
		}
	}

}
