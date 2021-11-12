package com.zihai.h2Client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.JsonArray;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class TestDto {
    private Integer age;
    private String name;
    private JsonArray json;
    private BigDecimal money;
    private Date time;
    private Boolean notify;
    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDate localDate;
    private List<People> peoples;

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

    public Boolean getNotify() {
        return notify;
    }

    public void setIsNotify(Boolean notify) {
        notify = notify;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public List<People> getPeoples() {
        return peoples;
    }

    public void setPeoples(List<People> peoples) {
        this.peoples = peoples;
    }
}
