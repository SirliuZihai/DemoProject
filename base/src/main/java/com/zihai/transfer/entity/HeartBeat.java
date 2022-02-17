package com.zihai.transfer.entity;

public class HeartBeat {
    private byte code;

    public HeartBeat(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }
}
