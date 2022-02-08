package com.zihai.h2Client.util.crypte;

import java.io.UnsupportedEncodingException;

/**
 * HMAC-SHA1 签名算法实现
 */
public class HmacSHA1Signature {

    /** 
     * 签名, 并以 Base64 编码后的结果返回
     */
    public static byte[] sign(byte[] content, String secretKey) throws Exception {
        
        return HmacSHA1.getHmacSHA1(content, secretKey);
    }

    /** 
     * 验证签名
     */
    public static boolean verify(byte[] content, byte[] sign, String secretKey) throws Exception {
        
        final byte[] original = sign;
        final byte[] expected = sign(content, secretKey);
        final String base64Expected = base64Str(expected);
        final String base64Original = base64Str(original);
        System.out.println(base64Str(content)+"  期望签名结果     "+base64Expected+"    请求结果     "+base64Original);
        if (base64Original.equals(base64Expected)) {
            return true;
        }
        return false;
    }
    
    private static String base64Str(byte[] content) {
    	try {
			return new String(Base64.encode(content), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
}
