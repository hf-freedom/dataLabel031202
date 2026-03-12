package com.weather.weather;

import lombok.Data;

@Data
public class SIWeatherResult<T> {
    private Integer code;
    private String message;
    private T data;
    
    public static <T> SIWeatherResult<T> success(T data) {
        SIWeatherResult<T> result = new SIWeatherResult<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }
    
    public static <T> SIWeatherResult<T> error(String message) {
        SIWeatherResult<T> result = new SIWeatherResult<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
    
    public static <T> SIWeatherResult<T> error(Integer code, String message) {
        SIWeatherResult<T> result = new SIWeatherResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
