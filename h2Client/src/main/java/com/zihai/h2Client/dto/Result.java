package com.zihai.h2Client.dto;

public class Result<T> {
    /**
     * 返回码 0:失败 1 正常
     * */
    private Integer code;
    /**
     * 返回消息
     */
    private String message;

    private T data;

    public static Result failure(String message){
        Result result = new Result();
        result.setCode(0);
        result.setMessage(message);
        return result;
    }
    public static <T> Result<T> success (String message,T data){
        Result result = new Result();
        result.setCode(1);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    public static Result success(String message){
        Result result = new Result();
        result.setCode(1);
        result.setMessage(message);
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
