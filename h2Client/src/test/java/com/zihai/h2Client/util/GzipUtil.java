package com.zihai.h2Client.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GzipUtil {

    public static final String ENCODE_UTF_8 = "UTF-8";

    /**
     * 字节数组解压
     */
    public static ByteArrayOutputStream uncompress(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gzipInputStream.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw e;
        }
        return out;
    }
    /**
     * 字节数组解压至string
     */
    public static String uncompressToString(byte[] bytes) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = uncompress(bytes);
        return byteArrayOutputStream.toString(ENCODE_UTF_8);
    }



}
