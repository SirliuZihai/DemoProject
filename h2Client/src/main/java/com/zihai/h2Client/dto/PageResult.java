package com.zihai.h2Client.dto;


public class PageResult<T> extends Result{
    /**
     * 总条数
     */
    private Integer totalNum;

    public static <T> PageResult<T> success(String message,T data,Integer totalNum){
        PageResult pageResult = new PageResult();
        pageResult.setTotalNum(totalNum);
        pageResult.setCode(1);
        pageResult.setMessage(message);
        return pageResult;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }
}
