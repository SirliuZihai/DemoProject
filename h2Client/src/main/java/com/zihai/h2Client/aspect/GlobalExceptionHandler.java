package com.zihai.h2Client.aspect;

import com.zihai.h2Client.dto.BusinessException;
import com.zihai.h2Client.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result exceptionHandler(Exception e){
        if(e instanceof BusinessException || e instanceof IllegalArgumentException){
            return Result.failure(e.getMessage());
        }else{
            log.error(e.getMessage(), e);;
            return Result.failure(e.getMessage());
        }
    }
}