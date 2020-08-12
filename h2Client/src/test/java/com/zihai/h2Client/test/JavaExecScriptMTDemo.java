package com.zihai.h2Client.test;


import com.google.gson.JsonObject;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.springframework.core.io.ClassPathResource;

import javax.script.CompiledScript;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.*;

public class JavaExecScriptMTDemo {

    public static void main(String[] args) throws Exception {
        testMain();
    }
    static void testMain() throws IOException, ScriptException, NoSuchMethodException {
        ScriptEngineManager sem = new ScriptEngineManager();
        NashornScriptEngine engine = (NashornScriptEngine) sem.getEngineByName("javascript");
        InputStream in = new ClassPathResource("js/test.js").getInputStream();
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        String str = new String(bytes, StandardCharsets.UTF_8);
        CompiledScript compiledScript = engine.compile(str);
        compiledScript.eval();
        JsonObject gson = new JsonObject();
        gson.addProperty("phone","1581613");

        Object mirror = engine.invokeFunction("checkPhone",gson.toString());
        System.out.println(mirror);
        gson.addProperty("phone","13386126649");
        Object mirror2 = engine.invokeFunction("checkPhone",gson.toString());
        System.out.println(mirror2);
    }
    static void testThread() throws ScriptException, InterruptedException, ExecutionException {
        ScriptEngineManager sem = new ScriptEngineManager();
        NashornScriptEngine engine = (NashornScriptEngine) sem.getEngineByName("javascript");
        String script = "function transform(arr){" +
                " var arr2=[]; for(var i=0;i<arr.size();i++){arr2[i]=arr[i]+1;}  return arr2.reverse(); " + "}";
        engine.eval(script);
        Callable<Collection> addition = new Callable<Collection>() {
            @Override
            public Collection call() {
                try {
                    ScriptObjectMirror mirror = (ScriptObjectMirror) engine.invokeFunction("transform", Arrays.asList(1, 2, 3));

                    return mirror.values();
                } catch (ScriptException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        ExecutorService executor = Executors.newCachedThreadPool();
        Collection collect = executor.submit(addition).get();
        System.out.println(collect.toString());
        executor.awaitTermination(1, TimeUnit.SECONDS);
        executor.shutdownNow();

    }

}