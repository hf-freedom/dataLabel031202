package com.weather.weather;

import lombok.Data;

@Data
public class WeatherVO {
    private String city;
    private String province;
    private String county;
    private String temp;
    private String feelsLike;
    private String text;
    private String windDir;
    private String windScale;
    private String humidity;
    private String precip;
    private String pressure;
    private String vis;
    private String updateTime;
}
