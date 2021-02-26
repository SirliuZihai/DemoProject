package com.zihai.h2Client.dto;

import com.zihai.h2Client.util.JsonHelp;

import java.util.Map;

public class TestDto {
    private Integer age;
    private String name;
    private String json;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJson() {
        return json;
    }

    public void setJson(Object json) {
        System.out.println(json.getClass().getSimpleName());
        this.json = JsonHelp.gson.toJson(json);
    }
}
