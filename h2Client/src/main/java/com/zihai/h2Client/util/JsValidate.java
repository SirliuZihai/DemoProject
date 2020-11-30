package com.zihai.h2Client.util;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zihai.h2Client.dto.Result;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.springframework.core.io.ClassPathResource;

import javax.script.CompiledScript;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsValidate {
    public static NashornScriptEngine engine;

    static {
        ScriptEngineManager sem = new ScriptEngineManager();
        engine = (NashornScriptEngine) sem.getEngineByName("javascript");
        try {
            InputStream in = new ClassPathResource("validate/iim_validate.js").getInputStream();
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            String str = new String(bytes, StandardCharsets.UTF_8);
            CompiledScript compiledScript = engine.compile(str);
            compiledScript.eval();
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        JsonObject obj = new Gson().fromJson("{account:'adcs'}",JsonObject.class);
        String result = (String)engine.invokeFunction("login_validate",obj.toString());
        JsonObject obj2 = JsonHelp.gson.fromJson(result,JsonObject.class);
        Result result1 = JsonHelp.gson.fromJson(result,Result.class);
        System.out.println(result1.getCode());
    }
}