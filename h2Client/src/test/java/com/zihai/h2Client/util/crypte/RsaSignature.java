package com.zihai.h2Client.util.crypte;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA签名/验签
 * 
 * @author jianjun.deng
 *
 */
public class RsaSignature {

	private static final Logger log = LoggerFactory.getLogger(RsaSignature.class);

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * RSA私钥签名
	 */
	public static byte[] sign(byte[] content, String privateKeyByBase64) {
		if (content == null || content.length == 0 || StringUtils.isBlank(privateKeyByBase64)) {
			return null;
		}
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKeyByBase64));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content);
			return signature.sign();
		} catch (Exception e) {
			log.error("签名异常", e);
		}

		return null;
	}

	/**
	 * RSA公钥验签
	 */
	public static boolean verify(byte[] content, byte[] sign, String publicKeyByBase64) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKeyByBase64);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content);
			boolean result =  signature.verify(sign);
			if (!result) {
				logDebug("content", content);
				logDebug("sign", sign);
			}
			return result;
		} catch (Exception e) {
			log.error("验签异常", e);
		}

		return false;
	}
	
	public static void logDebug(String label, byte[] data) {
		StringBuffer sb = new StringBuffer();
		sb.append(label).append("(").append(data.length).append(")").append(": ");
		for (byte b : data) {
			sb.append(b).append(", ");
		}
		log.info(sb.toString());
	}
	
}
