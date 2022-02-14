package com.zihai.h2Client.util;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtil {
    private final static String BAIDU_URL = "https://www.baidu.com/";
    private final static String WHAT_URL = "https://whatismyipaddress.com/";
    private final static String MBBROWSER_URL = "https://admin.mbbrowser.com/";

    public static void main(String[] args) {
        System.out.println(getIpFromMbbrowser());
        System.out.println(getIpFromWhat());
        System.out.println(getIpFromBaidu());

    }
    public static String getIpFromMbbrowser() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(CLIENT)
                .baseUrl(MBBROWSER_URL)
                .build();
        IpRequest api = retrofit.create(IpRequest.class);
        String resp = null;
        try {
            RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "lang=en&inweb=1");
            Response<ResponseBody> execute = api.getIpFromMbbrowser(body).execute();
            resp = responseIp(execute, "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return resp;
    }
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder().
            connectTimeout(10, TimeUnit.SECONDS).
            readTimeout(10, TimeUnit.SECONDS).
            writeTimeout(10, TimeUnit.SECONDS).build();

    public static String getIpFromBaidu() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(CLIENT)
                .baseUrl(BAIDU_URL)
                .build();
        IpRequest api = retrofit.create(IpRequest.class);
        String resp = null;
        try {
            Response<ResponseBody> execute = api.getIpFromBaidu().execute();
            resp = responseIp(execute, "本机IP:&nbsp;(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})");
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return resp;
    }

    public static String getIpFromWhat() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(CLIENT)
                .baseUrl(WHAT_URL)
                .build();
        IpRequest api = retrofit.create(IpRequest.class);
        String resp = null;
        try {
            Response<ResponseBody> execute = api.getIpFromWhat().execute();
            resp = responseIp(execute, "id=\"ipv4\"[^\\d]+(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})");
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return resp;
    }
    private static String responseIp(Response<ResponseBody> execute, String regex) throws Exception {
        byte[] respBytes = null;
        try {
            if ("gzip".equals(execute.headers().get("Content-Encoding"))) {
                respBytes = GzipUtil.uncompress(execute.body().bytes()).toByteArray();
            }
        } catch (IOException e) {
//            System.out.println("gzipException:" + e);
        }
        if (null == respBytes) {
            respBytes = execute.body().bytes();
        }
        Matcher m = Pattern.compile(regex)
                .matcher(new String(respBytes, "utf-8"));
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    interface IpRequest {
        @Headers({
                "accept: application/json, text/plain, */*",
                "accept-encoding: gzip, deflate, br",
                "accept-language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6",
                "cache-control: no-cache",
                "content-length: 15",
                "content-type: application/x-www-form-urlencoded",
                "origin: https://www.yalala.com",
                "pragma: no-cache",
                "referer: https://www.yalala.com/",
                "sec-ch-ua: \"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Microsoft Edge\";v=\"92\"",
                "sec-ch-ua-mobile: ?0",
                "sec-fetch-dest: empty",
                "sec-fetch-mode: cors",
                "sec-fetch-site: cross-site",
                "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78",
        })
        @POST("api/mbbrowser/clientinfo")
        Call<ResponseBody> getIpFromMbbrowser(@Body RequestBody body);
        @Headers({
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                "Accept-Encoding: gzip, deflate, br",
                "Accept-Language: zh-CN,zh;q=0.9",
                "Cache-Control: no-cache",
                "Connection: keep-alive",
                "Host: www.baidu.com",
                "Pragma: no-cache",
                "PreferAnonymous: 1",
                "sec-ch-ua: \"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Microsoft Edge\";v=\"92\"",
                "sec-ch-ua-mobile: ?0",
                "Sec-Fetch-Dest: document",
                "Sec-Fetch-Mode: navigate",
                "Sec-Fetch-Site: none",
                "Sec-Fetch-User: ?1",
                "Upgrade-Insecure-Requests: 1",
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78",
        })
        @GET("/s?wd=ip")
        Call<ResponseBody> getIpFromBaidu();

        @Headers({
                "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                "accept-encoding: gzip, deflate, br",
                "accept-language: zh-CN,zh;q=0.9",
                "cache-control: no-cache",
                "pragma: no-cache",
                "sec-ch-ua: \"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Microsoft Edge\";v=\"92\"",
                "sec-ch-ua-mobile: ?0",
                "sec-fetch-dest: document",
                "sec-fetch-mode: navigate",
                "sec-fetch-site: none",
                "sec-fetch-user: ?1",
                "upgrade-insecure-requests: 1",
                "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 Edg/92.0.902.78",
        })
        @GET("/")
        Call<ResponseBody> getIpFromWhat();
    }
}
