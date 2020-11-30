package com.zihai.h2Client.dto;

public class Page {
    /**
     * 页码 默认 1
     * @required
     * */
    private Integer pageNum = 1;

    /**
     * 每页条数 默认 20
     * @required
     */
    private Integer pageSize = 20;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
