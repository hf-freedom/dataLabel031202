package com.example.weather.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.config.AmapConfig;
import com.example.weather.dto.WeatherResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 天气API客户端工具类
 */
@Component
public class SIWeatherApiClient {

    private static final Logger logger = LoggerFactory.getLogger(SIWeatherApiClient.class);

    @Autowired
    private SIHttpClient httpClient;

    @Autowired
    private AmapConfig amapConfig;

    @Autowired
    private SIJsonUtil jsonUtil;

    /**
     * 检查配置
     */
    @PostConstruct
    public void checkConfig() {
        if ("your-amap-key-here".equals(amapConfig.getKey())) {
            logger.warn("高德地图API Key未配置，请前往 https://console.amap.com 申请免费Key");
        }
    }

    /**
     * 根据城市编码查询天气
     *
     * @param cityCode 城市编码
     * @return 天气结果
     */
    public WeatherResult queryWeatherByCityCode(String cityCode) {
        Map<String, String> params = new HashMap<>();
        params.put("key", amapConfig.getKey());
        params.put("city", cityCode);
        params.put("extensions", "base");

        String response = httpClient.doGet(amapConfig.getWeatherUrl(), params);
        if (response == null) {
            return WeatherResult.fail("请求天气服务失败");
        }

        return parseWeatherResponse(response);
    }

    /**
     * 根据城市名称查询天气
     *
     * @param cityName 城市名称
     * @return 天气结果
     */
    public WeatherResult queryWeatherByCityName(String cityName) {
        // 先通过地理编码获取城市编码
        String cityCode = getCityCodeByName(cityName);
        if (cityCode == null) {
            // 如果无法获取编码，直接使用城市名称查询
            Map<String, String> params = new HashMap<>();
            params.put("key", amapConfig.getKey());
            params.put("city", cityName);
            params.put("extensions", "base");

            String response = httpClient.doGet(amapConfig.getWeatherUrl(), params);
            if (response == null) {
                return WeatherResult.fail("请求天气服务失败");
            }
            return parseWeatherResponse(response);
        }
        return queryWeatherByCityCode(cityCode);
    }

    /**
     * 根据经纬度查询天气
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 天气结果
     */
    public WeatherResult queryWeatherByLocation(Double longitude, Double latitude) {
        // 先通过逆地理编码获取城市信息
        String cityName = getCityNameByLocation(longitude, latitude);
        if (cityName == null) {
            return WeatherResult.fail("无法识别该位置的城市信息");
        }
        return queryWeatherByCityName(cityName);
    }

    /**
     * 根据经纬度获取城市名称
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 城市名称
     */
    public String getCityNameByLocation(Double longitude, Double latitude) {
        Map<String, String> params = new HashMap<>();
        params.put("key", amapConfig.getKey());
        params.put("location", longitude + "," + latitude);

        String response = httpClient.doGet(amapConfig.getGeocodeUrl(), params);
        if (response == null) {
            return null;
        }

        JSONObject jsonObject = jsonUtil.parseObject(response);
        if (jsonObject == null || !"1".equals(jsonObject.getString("status"))) {
            return null;
        }

        JSONObject regeocode = jsonObject.getJSONObject("regeocode");
        if (regeocode == null) {
            return null;
        }

        JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
        if (addressComponent == null) {
            return null;
        }

        // 优先返回城市，如果没有则返回省份
        String city = addressComponent.getString("city");
        if (city == null || city.isEmpty() || "[]".equals(city)) {
            city = addressComponent.getString("province");
        }
        return city;
    }

    /**
     * 根据城市名称获取城市编码
     *
     * @param cityName 城市名称
     * @return 城市编码
     */
    private String getCityCodeByName(String cityName) {
        // 这里可以扩展调用高德地图的地理编码API
        // 简化处理，直接返回null，让上层使用名称查询
        return null;
    }

    /**
     * 解析天气响应
     *
     * @param response 响应字符串
     * @return 天气结果
     */
    private WeatherResult parseWeatherResponse(String response) {
        JSONObject jsonObject = jsonUtil.parseObject(response);
        if (jsonObject == null) {
            return WeatherResult.fail("解析天气数据失败");
        }

        String status = jsonObject.getString("status");
        if (!"1".equals(status)) {
            String info = jsonObject.getString("info");
            return WeatherResult.fail("查询失败: " + (info != null ? info : "未知错误"));
        }

        JSONArray lives = jsonObject.getJSONArray("lives");
        if (lives == null || lives.isEmpty()) {
            return WeatherResult.fail("暂无天气数据");
        }

        JSONObject weatherData = lives.getJSONObject(0);
        return WeatherResult.success(
                weatherData.getString("city"),
                weatherData.getString("weather"),
                weatherData.getString("temperature") + "°C",
                weatherData.getString("humidity") + "%",
                weatherData.getString("winddirection"),
                weatherData.getString("windpower"),
                weatherData.getString("reporttime")
        );
    }
}
