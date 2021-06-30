package com.zihai.h2Client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface BusService {
    //@AliasFor(annotation = BusService.class)
    String value() default "";
}
