package com.zihai.h2Client.util;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;

public class TokenUtil {
	// 私钥
	private static final String AES_KEY = "1111222233334444";
	// 偏移量
	public static final String VIPARA = "1234567876543210";
	// 填充类型
	public static final String AES_TYPE = "AES/ECB/PKCS5Padding";
	// 编码方式
	public static final String CODE_TYPE = "UTF-8";


	public static String encrypt(String cleartext) {
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
			SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance(AES_TYPE); // PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解密
			cipher.init(Cipher.ENCRYPT_MODE, key); // CBC类型的可以在第三个参数传递偏移量zeroIv,ECB没有偏移量
			byte[] encryptedData = cipher.doFinal(cleartext.getBytes(CODE_TYPE));
			return new BASE64Encoder().encode(encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String completionCodeFor16Bytes(String str) throws UnsupportedEncodingException {
		int num = str.getBytes("UTF-8").length;
		int index = num % 16;
		// 进行加密内容补全操作, 加密内容应该为 16字节的倍数, 当不足16*n字节是进行补全, 差一位时 补全16+1位
		// 补全字符 以 $ 开始,$后一位代表$后补全字符位数,之后全部以0进行补全;
		if (index != 0) {
			StringBuffer sbBuffer = new StringBuffer(str);
			if (16 - index == 1) {
				sbBuffer.append("$" + consult[16 - 1] + addStr(16 - 1 - 1));
			} else {
				sbBuffer.append("$" + consult[16 - index - 1] + addStr(16 - index - 1 - 1));
			}
			str = sbBuffer.toString();
		}
		return str;
	}

	public static String getNow() {
		LocalDate localDate = LocalDate.now();
		LocalTime localTime = LocalTime.now().withNano(0);
		String sfm = localTime.toString();
		if (sfm.length() < 6) {
			sfm += ":00";
		}
		return localDate.toString() + " " + sfm;
	}

	// 追加字符
	public static String addStr(int num) {
		StringBuffer sbBuffer = new StringBuffer("");
		for (int i = 0; i < num; i++) {
			sbBuffer.append("0");
		}
		return sbBuffer.toString();
	}

	// 字符补全
	private static final String[] consult = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B",
			"C", "D", "E", "F", "G" };
}
