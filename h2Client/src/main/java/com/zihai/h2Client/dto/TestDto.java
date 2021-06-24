package com.zihai.h2Client.dto;

import com.zihai.h2Client.util.JsonHelp;

import java.math.BigDecimal;

public class TestDto {
    private Integer age;
    private String name;
    private String json;
    private BigDecimal money;

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

    public void setJson(String json) {
        this.json = json;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
