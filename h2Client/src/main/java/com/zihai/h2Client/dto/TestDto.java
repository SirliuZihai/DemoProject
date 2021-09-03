package com.zihai.h2Client.dto;

import com.google.gson.JsonArray;

import java.math.BigDecimal;
import java.util.Date;

public class TestDto {
    private Integer age;
    private String name;
    private JsonArray json;
    private BigDecimal money;
    private Date time;

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

    public JsonArray getJson() {
        return json;
    }

    public void setJson(JsonArray json) {
        this.json = json;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
