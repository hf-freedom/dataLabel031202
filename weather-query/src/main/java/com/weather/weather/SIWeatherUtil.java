package com.weather.weather;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class SIWeatherUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(SIWeatherUtil.class);
    
    @Value("${weather.qweather.api-key}")
    private String apiKey;
    
    @Value("${weather.qweather.base-url}")
    private String baseUrl;
    
    @Value("${weather.qweather.geo-url}")
    private String geoUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public WeatherVO getWeatherByLocation(String locationId, String cityName, String province, String county) {
        try {
            String url = baseUrl + "/weather/now?location=" + locationId + "&key=" + apiKey;
            logger.info("请求天气API: {}", url);
            String response = restTemplate.getForObject(url, String.class);
            logger.info("天气API响应: {}", response);
            
            JSONObject json = JSON.parseObject(response);
            String code = json.getString("code");
            
            if (!"200".equals(code)) {
                logger.error("天气API返回错误码: {}, 响应: {}", code, response);
                return null;
            }
            
            JSONObject now = json.getJSONObject("now");
            WeatherVO vo = new WeatherVO();
            vo.setCity(cityName);
            vo.setProvince(province);
            vo.setCounty(county);
            vo.setTemp(now.getString("temp"));
            vo.setFeelsLike(now.getString("feelsLike"));
            vo.setText(now.getString("text"));
            vo.setWindDir(now.getString("windDir"));
            vo.setWindScale(now.getString("windScale"));
            vo.setHumidity(now.getString("humidity"));
            vo.setPrecip(now.getString("precip"));
            vo.setPressure(now.getString("pressure"));
            vo.setVis(now.getString("vis"));
            vo.setUpdateTime(json.getString("updateTime"));
            
            return vo;
        } catch (Exception e) {
            logger.error("获取天气信息异常", e);
            return null;
        }
    }
    
    public String[] getLocationInfoByCityName(String cityName, String provinceName, String cityNameFull) {
        try {
            String searchCity = cityNameFull != null && !cityNameFull.isEmpty() ? cityNameFull : cityName;
            String encodedCity = URLEncoder.encode(searchCity, "UTF-8");
            String url = geoUrl + "/city/lookup?location=" + encodedCity + "&key=" + apiKey;
            logger.info("请求城市查询API: {}", url);
            String response = restTemplate.getForObject(url, String.class);
            logger.info("城市查询API响应: {}", response);
            
            JSONObject json = JSON.parseObject(response);
            String code = json.getString("code");
            
            if (!"200".equals(code)) {
                logger.error("城市查询API返回错误码: {}", code);
                return null;
            }
            
            JSONArray locations = json.getJSONArray("location");
            if (locations != null && !locations.isEmpty()) {
                JSONObject location = locations.getJSONObject(0);
                String locationId = location.getString("id");
                String name = location.getString("name");
                String adm1 = location.getString("adm1");
                String adm2 = location.getString("adm2");
                return new String[]{locationId, name, adm1, adm2};
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("URL编码异常", e);
        } catch (Exception e) {
            logger.error("查询城市信息异常", e);
        }
        return null;
    }
    
    public String[] getLocationIdByGeo(double longitude, double latitude) {
        try {
            String url = geoUrl + "/geo/lookup?location=" + longitude + "," + latitude + "&key=" + apiKey;
            logger.info("请求经纬度查询API: {}", url);
            String response = restTemplate.getForObject(url, String.class);
            logger.info("经纬度查询API响应: {}", response);
            
            JSONObject json = JSON.parseObject(response);
            String code = json.getString("code");
            
            if (!"200".equals(code)) {
                logger.error("经纬度查询API返回错误码: {}", code);
                return null;
            }
            
            JSONArray locations = json.getJSONArray("location");
            if (locations != null && !locations.isEmpty()) {
                JSONObject location = locations.getJSONObject(0);
                String locationId = location.getString("id");
                String name = location.getString("name");
                String adm1 = location.getString("adm1");
                String adm2 = location.getString("adm2");
                return new String[]{locationId, name, adm1, adm2};
            }
        } catch (Exception e) {
            logger.error("根据经纬度查询城市异常", e);
        }
        return null;
    }
    
    public WeatherVO getWeatherByGeo(double longitude, double latitude) {
        String[] locationInfo = getLocationIdByGeo(longitude, latitude);
        if (locationInfo == null) {
            return null;
        }
        
        String locationId = locationInfo[0];
        String cityName = locationInfo[1];
        String province = locationInfo[2];
        String county = locationInfo[3];
        
        return getWeatherByLocation(locationId, cityName, province, county);
    }
}
