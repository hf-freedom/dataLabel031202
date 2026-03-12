package com.weather.controller;

import com.weather.mapper.CityMapper;
import com.weather.weather.CityVO;
import com.weather.weather.SIWeatherResult;
import com.weather.weather.SIWeatherUtil;
import com.weather.weather.WeatherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    
    @Autowired
    private CityMapper cityMapper;
    
    @Autowired
    private SIWeatherUtil weatherUtil;
    
    @GetMapping("/provinces")
    public SIWeatherResult<List<CityVO>> getProvinces() {
        List<CityVO> provinces = cityMapper.findAllProvinces();
        return SIWeatherResult.success(provinces);
    }
    
    @GetMapping("/cities/{provinceCode}")
    public SIWeatherResult<List<CityVO>> getCities(@PathVariable String provinceCode) {
        List<CityVO> cities = cityMapper.findCitiesByProvinceCode(provinceCode);
        return SIWeatherResult.success(cities);
    }
    
    @GetMapping("/counties/{cityCode}")
    public SIWeatherResult<List<CityVO>> getCounties(@PathVariable String cityCode) {
        List<CityVO> counties = cityMapper.findCountiesByCityCode(cityCode);
        return SIWeatherResult.success(counties);
    }
    
    @GetMapping("/query")
    public SIWeatherResult<WeatherVO> queryWeather(@RequestParam String countyCode) {
        CityVO county = cityMapper.findByCode(countyCode);
        if (county == null) {
            return SIWeatherResult.error("城市不存在");
        }
        
        String fullName = cityMapper.findCityFullName(countyCode);
        String[] parts = fullName != null ? fullName.split("-") : new String[]{"", "", county.getName()};
        String provinceName = parts.length > 0 ? parts[0] : "";
        String cityName = parts.length > 1 ? parts[1] : "";
        String countyName = parts.length > 2 ? parts[2] : county.getName();
        
        String[] locationInfo = weatherUtil.getLocationInfoByCityName(countyName, provinceName, cityName + countyName);
        if (locationInfo == null) {
            return SIWeatherResult.error("获取城市位置信息失败");
        }
        
        String locationId = locationInfo[0];
        String apiCityName = locationInfo[1];
        String apiProvince = locationInfo[2];
        String apiCounty = locationInfo[3];
        
        WeatherVO weather = weatherUtil.getWeatherByLocation(locationId, apiCityName, apiProvince, apiCounty);
        
        if (weather == null) {
            return SIWeatherResult.error("获取天气信息失败");
        }
        
        return SIWeatherResult.success(weather);
    }
    
    @GetMapping("/query/geo")
    public SIWeatherResult<WeatherVO> queryWeatherByGeo(
            @RequestParam Double longitude,
            @RequestParam Double latitude) {
        
        WeatherVO weather = weatherUtil.getWeatherByGeo(longitude, latitude);
        
        if (weather == null) {
            return SIWeatherResult.error("根据经纬度获取天气信息失败");
        }
        
        return SIWeatherResult.success(weather);
    }
}
