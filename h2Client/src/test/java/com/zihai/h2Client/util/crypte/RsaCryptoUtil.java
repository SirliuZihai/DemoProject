package com.zihai.h2Client.util.crypte;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

/**
 * RSA 加密解密工具类
 * <p>
 * RSA算法对加密数据的长度有限制: 密钥512 bit情况下不能超过53 byte， 1024情况下不能超过117字节,
 * 规则为：加密的明文长度不能超过RSA密钥的长度减去11byte RSA的秘药长度（512-65536），jdk默认1024 bit
 * <p/>
 * 加密后密文的长度为密钥的长度，如密钥长度为1024bit(128Byte)，最后生成的密文固定为 1024bit(128Byte),
 * 密钥越长强度越大越安全 ，但性能越差
 * <p/>
 * 加密后密文长度等于私钥长度。
 * <p/>
 * java JCE RSA 密钥格式默认是pkcs8, 如果是用其他工具产生密钥的话需要注意格式问题， 如： 使用openssl产生的密钥是pcks1,
 * 需要转换成pkcs8
 * <p/>
 * Created by zhangli on 14-7-15.
 */
@SuppressWarnings("restriction")
public class RsaCryptoUtil {

	private static final Logger log = LoggerFactory.getLogger(RsaCryptoUtil.class);

	private static final String RSA_ECB = "RSA/ECB/PKCS1Padding";

	/**
	 * RSA私钥解密
	 */
	public static byte[] decryptByPrivateKey(byte[] sSrc, String privateKeyByBase64) {
		if (sSrc == null || sSrc.length == 0 || StringUtils.isBlank(privateKeyByBase64)) {
			throw new IllegalArgumentException("cipherSrc or privateKey cann't empty!");
		}

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyByBase64));
		byte[] textBytes = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(spec);
			Cipher cipher = Cipher.getInstance(RSA_ECB);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			textBytes = decrypt(sSrc, privateKey.getEncoded().length, cipher);
		} catch (Throwable throwable) {
			log.info("RSA 私钥解密异常：{}", throwable.getMessage() + "" + throwable);
		}
		return textBytes;
	}

	/**
	 * RSA私钥解密(Base64)
	 */
	public static String decryptByPrivateKey(String sSrcByBase64, String privateKeyByBase64) {
		if (StringUtils.isBlank(sSrcByBase64) || StringUtils.isBlank(privateKeyByBase64)) {
			throw new IllegalArgumentException("cipherSrc or privateKey cann't empty!");
		}

		byte[] decryptBytes = decryptByPrivateKey(Base64.decode(sSrcByBase64), privateKeyByBase64);
		return new String(decryptBytes, StandardCharsets.UTF_8);
	}

	/**
	 * RSA公钥加密
	 */
	public static byte[] encryptByPublicKey(byte[] sSrc, String publicKeyByBase64) {
		if (sSrc == null || sSrc.length == 0 || StringUtils.isBlank(publicKeyByBase64)) {
			throw new IllegalArgumentException("textSrc or publicKey cann't empty!");
		}
		X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(Base64.decode(publicKeyByBase64));
		byte[] cipherBytes = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(x509Spec);
			Cipher cipher = Cipher.getInstance(RSA_ECB);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			cipherBytes = encrypt(sSrc, publicKey.getEncoded().length, cipher);
		} catch (Throwable throwable) {
			log.info("RSA 公钥加密异常：{}", throwable.getMessage() + "" + throwable);
		}
		return cipherBytes;
	}

	/**
	 * RSA公钥加密(base64)
	 */
	public static String encryptByPublicKey(String textSrc, String publicKeyByBase64) {
		if (StringUtils.isBlank(textSrc) || StringUtils.isBlank(publicKeyByBase64)) {
			throw new IllegalArgumentException("textSrc or publicKey cann't empty!");
		}

		byte[] encryptBytes = encryptByPublicKey(textSrc.getBytes(StandardCharsets.UTF_8), publicKeyByBase64);
		return new String(Base64.encode(encryptBytes), StandardCharsets.UTF_8);
	}

	private static byte[] encrypt(byte[] inputBytes, int keySize, Cipher cipher) throws Exception {
		int maxTextSize = keySize / 8 - 11;
		// 分片加密
		if (inputBytes.length > maxTextSize) {
			int index = 0;
			try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
				while (true) {
					if (index + maxTextSize < inputBytes.length) {
						bout.write(cipher.doFinal(inputBytes, index, maxTextSize));
						index += maxTextSize;
					} else {
						bout.write(cipher.doFinal(inputBytes, index, inputBytes.length - index));
						break;
					}
				}
				return bout.toByteArray();
			}
		} else {
			return cipher.doFinal(inputBytes);
		}
	}

	private static byte[] decrypt(byte[] inputBytes, int keySize, Cipher cipher) throws Exception {
		int maxDecryptSize = keySize / 8;
		if (inputBytes.length > maxDecryptSize) {
			int index = 0;
			try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
				while (true) {
					if (index + maxDecryptSize < inputBytes.length) {
						bout.write(cipher.doFinal(inputBytes, index, maxDecryptSize));
						index += maxDecryptSize;
					} else {
						bout.write(cipher.doFinal(inputBytes, index, inputBytes.length - index));
						break;
					}
				}
				return bout.toByteArray();
			}
		} else {
			return cipher.doFinal(inputBytes);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		prop.load(new FileInputStream("D:\\document\\华盛-1\\java-hs-openapi-master_20210923\\src\\test\\resources\\client.properties"));
		String secretKey = prop.getProperty("encrypt.rsa.privateKey");
		
		String src = "gZi2Nkxmki9PajIF3q8xr5YPBLdoXtZpfj99WDsNtugpqp5dsHw0BiVIxU1Kao7/JF3mdhUQcfS9JQZsHDLDlL34VUuuIoXNtM9tdDQjFMKdXp3okahR1LCqciajvlnQc3FPNu0wDpNUwkXImkFw4yV8UCCY20QyLEzCzVV/140=";
		
		byte[] srcBytes = decryptByPrivateKey(Base64.decode(src), secretKey);
		
		System.out.println(new String(srcBytes));
	}

}
