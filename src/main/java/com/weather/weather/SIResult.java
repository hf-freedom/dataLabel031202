package com.weather.weather;

import lombok.Data;

@Data
public class SIResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> SIResult<T> success(T data) {
        SIResult<T> result = new SIResult<>();
        result.setCode(200);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static <T> SIResult<T> fail(String msg) {
        SIResult<T> result = new SIResult<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}
