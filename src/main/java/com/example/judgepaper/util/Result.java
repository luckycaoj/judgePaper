package com.example.judgepaper.util;

import lombok.ToString;

@ToString
public class Result<T> {
    public int code;
    public String msg;
    public T data;
    public Result(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
