package com.zihai.h2Client.aspect;

import java.util.HashMap;
import java.util.Map;

public class WhiteList {
    public final static Map<String,String> whiteMap = new HashMap<>();
    static {
        whiteMap.put("/user/login", "登录");
        whiteMap.put("/file/upload","上传文件");
        whiteMap.put("/public","公共接口");
    }
}
