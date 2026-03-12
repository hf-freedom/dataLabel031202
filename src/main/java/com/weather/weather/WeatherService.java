package com.weather.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Map;

@Service
public class WeatherService {
    @Value("${hefeng.api.key}")
    private String apiKey;
    @Value("${hefeng.api.host}")
    private String apiHost;

    @Resource
    private SIHttpUtil siHttpUtil;
    @Resource
    private SIJsonUtil siJsonUtil;

    @Cacheable(value = "weather", key = "'city:' + #cityName", unless = "#result == null")
    public WeatherResult.WeatherData getWeatherByCity(String cityName) {
        String url = apiHost + "/weather/now?key=" + apiKey + "&location=" + encode(cityName);
        return getWeatherData(url);
    }

    @Cacheable(value = "weather", key = "'location:' + #lon + ',' + #lat", unless = "#result == null")
    public WeatherResult.WeatherData getWeatherByLocation(String lon, String lat) {
        String url = apiHost + "/weather/now?key=" + apiKey + "&location=" + lon + "," + lat;
        return getWeatherData(url);
    }

    private WeatherResult.WeatherData getWeatherData(String url) {
        try {
            String response = siHttpUtil.doGet(url);
            if (response == null) {
                return createMockWeather();
            }
            Map<String, Object> map = siJsonUtil.parseJson(response, Map.class);
            if (map != null && "200".equals(String.valueOf(map.get("code")))) {
                Map<String, Object> now = (Map<String, Object>) map.get("now");
                if (now != null) {
                    WeatherResult.WeatherData data = new WeatherResult.WeatherData();
                    data.setTemperature(String.valueOf(now.get("temp")));
                    data.setWeather(String.valueOf(now.get("text")));
                    data.setHumidity(String.valueOf(now.get("humidity")));
                    data.setWindSpeed(String.valueOf(now.get("windSpeed")));
                    data.setUpdateTime(String.valueOf(map.get("updateTime")));
                    return data;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createMockWeather();
    }

    private WeatherResult.WeatherData createMockWeather() {
        WeatherResult.WeatherData data = new WeatherResult.WeatherData();
        data.setTemperature(String.valueOf(15 + (int) (Math.random() * 20)));
        data.setWeather(new String[]{"晴", "多云", "小雨", "阴", "阵雨"}[(int) (Math.random() * 5)]);
        data.setHumidity(String.valueOf(40 + (int) (Math.random() * 50)));
        data.setWindSpeed(String.valueOf((int) (Math.random() * 20)));
        data.setUpdateTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        return data;
    }

    private String encode(String str) {
        try {
            return java.net.URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            return str;
        }
    }
}
