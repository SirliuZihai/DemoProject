package com.zihai.work;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        OkHttpClient httpclient = new OkHttpClient();
        Request request =new Request.Builder()
                .url("http://localhost:8080/fass")
                .method("GET",null)
                .build();
        try {
            Response resonse = httpclient.newCall(request).execute();
            System.out.println(resonse.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
