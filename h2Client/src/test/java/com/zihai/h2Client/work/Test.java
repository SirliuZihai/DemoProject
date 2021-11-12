package com.zihai.h2Client.work;

import com.zihai.h2Client.dto.People;
import com.zihai.h2Client.dto.TestDto;
import com.zihai.h2Client.util.JsonUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
      /*  TestDto dto = new TestDto();
        List<People> arrayList = new ArrayList<>();
        dto.setPeoples(arrayList);
        People p = new People("liu",23);
        arrayList.add(p);
        p = new People("liu",24);
        System.out.println(JsonUtils.toJsonString(arrayList));*/
        LocalDate d = LocalDate.parse("2021-12-01", DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        System.out.println(d.toString());
    }
}
