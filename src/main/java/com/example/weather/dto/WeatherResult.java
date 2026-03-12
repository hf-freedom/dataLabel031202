package com.example.weather.dto;

/**
 * 天气查询结果DTO
 */
public class WeatherResult {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 天气状况
     */
    private String weather;

    /**
     * 温度
     */
    private String temperature;

    /**
     * 湿度
     */
    private String humidity;

    /**
     * 风向
     */
    private String windDirection;

    /**
     * 风力
     */
    private String windPower;

    /**
     * 发布时间
     */
    private String reportTime;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindPower() {
        return windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public static WeatherResult success(String city, String weather, String temperature,
                                        String humidity, String windDirection, String windPower, String reportTime) {
        WeatherResult result = new WeatherResult();
        result.setSuccess(true);
        result.setMessage("查询成功");
        result.setCity(city);
        result.setWeather(weather);
        result.setTemperature(temperature);
        result.setHumidity(humidity);
        result.setWindDirection(windDirection);
        result.setWindPower(windPower);
        result.setReportTime(reportTime);
        return result;
    }

    public static WeatherResult fail(String message) {
        WeatherResult result = new WeatherResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}
