package com.zihai.h2Client.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static void deleteFile(String path){
        File f = new File(path);
        if(f.exists()&&f.isFile())
            f.delete();
    }
    public static void downLoad(String fromUrl,String toLocal){
        OkHttpClient httpclient = new OkHttpClient();
        Request request =new Request.Builder()
                .url(fromUrl)
                .method("GET",null)
                .build();
        try {
            Response resonse = httpclient.newCall(request).execute();
            new FileOutputStream(toLocal).write(resonse.body().bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
