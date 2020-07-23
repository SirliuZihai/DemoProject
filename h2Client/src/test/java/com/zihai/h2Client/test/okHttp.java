package com.zihai.h2Client.test;

import okhttp3.*;
import okio.BufferedSink;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class okHttp {
    public static void main(String[] args) throws IOException {
        /*File f = new File("/C:/Users/Administrator/Pictures/Camera Roll/gg-1/gg.jpg");
        byte[] bytes = FileUtils.readFileToByteArray(f);
        System.out.println(bytes.length);
        System.out.println(Base64.getEncoder().encode(bytes).length);*/
        postPicture("http://192.168.1.82:65214/registrate");
    }
    public static String postJson(String url,String json) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public static void postPicture(String url) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("endinaness", "") // android -> big  other->little
                .addFormDataPart("filters", "63") // off -> 0   on -> 63 for all.
                .addFormDataPart("seq", "123")
                .addFormDataPart("img","gg.jpg",
                        RequestBody.create(MediaType.parse("image/jpeg"),
                                new File("/C:/Users/Administrator/Pictures/Camera Roll/gg-1/gg.jpg")))
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.82:65214/registrate")
                .method("POST", body)
                .addHeader("Content-Type", "multipart/form-data")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.code());
        System.out.println(response.body().string());
    }
}
