package com.weather.controller;

import com.weather.mapper.CityMapper;
import com.weather.weather.City;
import com.weather.weather.SIResult;
import com.weather.weather.WeatherResult;
import com.weather.weather.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Resource
    private WeatherService weatherService;
    @Resource
    private CityMapper cityMapper;

    @GetMapping("/provinces")
    public SIResult<List<City>> getProvinces() {
        List<City> provinces = cityMapper.getProvinces();
        return SIResult.success(provinces);
    }

    @GetMapping("/cities")
    public SIResult<List<City>> getCities(@RequestParam String parentCode) {
        List<City> cities = cityMapper.getChildren(parentCode);
        return SIResult.success(cities);
    }

    @GetMapping("/counties")
    public SIResult<List<City>> getCounties(@RequestParam String parentCode) {
        List<City> counties = cityMapper.getChildren(parentCode);
        return SIResult.success(counties);
    }

    @GetMapping("/byCity")
    public SIResult<WeatherResult.WeatherData> getWeatherByCity(@RequestParam String cityName) {
        try {
            WeatherResult.WeatherData data = weatherService.getWeatherByCity(cityName);
            data.setCity(cityName);
            return SIResult.success(data);
        } catch (Exception e) {
            return SIResult.fail("查询天气失败：" + e.getMessage());
        }
    }

    @GetMapping("/byLocation")
    public SIResult<WeatherResult.WeatherData> getWeatherByLocation(@RequestParam String lon, @RequestParam String lat) {
        try {
            City city = cityMapper.getCityByLocation(lon, lat);
            WeatherResult.WeatherData data = weatherService.getWeatherByLocation(lon, lat);
            if (city != null) {
                data.setCity(city.getName());
            }
            return SIResult.success(data);
        } catch (Exception e) {
            return SIResult.fail("查询天气失败：" + e.getMessage());
        }
    }

    @GetMapping("/cityInfo")
    public SIResult<City> getCityInfo(@RequestParam String cityCode) {
        City city = cityMapper.getCityByCode(cityCode);
        return SIResult.success(city);
    }
}
