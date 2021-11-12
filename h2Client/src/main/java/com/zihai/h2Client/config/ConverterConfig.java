package com.zihai.h2Client.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class ConverterConfig {
    private String pattern = "yyyy-MM-dd";

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class,new LocalDateDeserializer());
        module.addSerializer(LocalDate.class,new LocalDateSerializer());
        objectMapper.registerModule(module);
        //设置过滤掉null值得属性.
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setTimeZone(TimeZone.getDefault());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    public class LocalDateSerializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(pattern)));
        }
    }

    public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext deserializationContext)
                throws IOException {
            return LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(pattern));
        }
    }


}
