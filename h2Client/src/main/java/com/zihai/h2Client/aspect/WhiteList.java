package com.zihai.h2Client.aspect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WhiteList {
    public final static Map<String,String> whiteMap = new HashMap<>();
    public final static Set<String> noLogSet = new HashSet<>();
    static {
        whiteMap.put("/user/login", "登录");
        whiteMap.put("/file/upload","上传文件");
        whiteMap.put("/public","公共接口");

        noLogSet.add("/file/upload");
    }
}
