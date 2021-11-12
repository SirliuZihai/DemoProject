package com.zihai.h2Client.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zihai.h2Client.dto.TestDto;
import com.zihai.h2Client.util.JsonUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import java.util.TimeZone;

public class JsonTest {
    @Test
    public void Test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //设置过滤掉null值得属性.
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setTimeZone(TimeZone.getDefault());
        TestDto dto = objectMapper.readValue("{ \"time\":\"2021-11-10T17:45:34\"}",TestDto.class);
        dto.setLocalDate(LocalDate.now());
        System.out.println(dto.getLocalDate().toString());;
        System.out.println(JsonUtils.toJsonString(dto));
    }
}