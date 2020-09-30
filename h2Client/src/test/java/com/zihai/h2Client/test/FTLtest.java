package com.zihai.h2Client.test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by lylhjh on 2020-09-15.
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("com.zihai")
@SpringBootTest
public class FTLtest {
    @Autowired
    Configuration cfg;
    @Test
    public void dowork() throws IOException, TemplateException {
        Template template = cfg.getTemplate("demo.ftl");
        JsonObject obj = new JsonObject();
        obj.addProperty("userId","1234");
        JsonObject person = new JsonObject();
        person.addProperty("age",12);
        obj.add("person",person);
        JsonArray arrays = new JsonArray();
        arrays.add("xia");
        arrays.add("zi");
        arrays.add("ge");
        obj.add("arrays",arrays);
        Map map = new Gson().fromJson(obj, Map.class);
        map.put("time",String.valueOf(new Date().getTime()/1000));
        System.out.println(map.toString());
        String result = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);
        System.out.println(result);
    }

}
