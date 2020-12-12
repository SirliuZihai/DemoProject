package com.zihai.h2Client.dto;

public class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
}
