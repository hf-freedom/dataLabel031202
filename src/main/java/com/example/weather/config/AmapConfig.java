package com.example.weather.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 高德地图配置类
 */
@Configuration
@ConfigurationProperties(prefix = "weather.amap")
public class AmapConfig {

    /**
     * API Key
     */
    private String key;

    /**
     * 天气查询API地址
     */
    private String weatherUrl;

    /**
     * 地理编码API地址
     */
    private String geocodeUrl;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWeatherUrl() {
        return weatherUrl;
    }

    public void setWeatherUrl(String weatherUrl) {
        this.weatherUrl = weatherUrl;
    }

    public String getGeocodeUrl() {
        return geocodeUrl;
    }

    public void setGeocodeUrl(String geocodeUrl) {
        this.geocodeUrl = geocodeUrl;
    }
}
