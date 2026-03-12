package com.weather.weather;

import lombok.Data;

@Data
public class WeatherResult {
    private Integer code;
    private String msg;
    private WeatherData data;

    public static WeatherResult success(WeatherData data) {
        WeatherResult result = new WeatherResult();
        result.setCode(200);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static WeatherResult fail(String msg) {
        WeatherResult result = new WeatherResult();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }

    @Data
    public static class WeatherData {
        private String city;
        private String temperature;
        private String weather;
        private String humidity;
        private String windSpeed;
        private String updateTime;
    }
}
