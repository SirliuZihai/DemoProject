package com.zihai.h2Client.test;

import com.zihai.h2Client.dto.TestDto;
import com.zihai.h2Client.util.JsonUtils;
import org.junit.Test;

import java.math.BigDecimal;

public class JsonTest {
    @Test
    public void Test(){
        TestDto dto = new TestDto();
        dto.setMoney(new BigDecimal("1.00"));
        System.out.println(JsonUtils.toJsonString(dto));
    }
}