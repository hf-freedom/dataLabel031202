package com.example.weather.service;

import com.example.weather.dto.WeatherResult;
import com.example.weather.entity.City;
import com.example.weather.entity.District;
import com.example.weather.entity.Province;
import com.example.weather.mapper.CityMapper;
import com.example.weather.util.SIWeatherApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 天气服务类
 */
@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private SIWeatherApiClient weatherApiClient;

    /**
     * 获取所有省份
     *
     * @return 省份列表
     */
    public List<Province> getAllProvinces() {
        return cityMapper.selectAllProvinces();
    }

    /**
     * 根据省份编码获取城市列表
     *
     * @param provinceCode 省份编码
     * @return 城市列表
     */
    public List<City> getCitiesByProvince(String provinceCode) {
        return cityMapper.selectCitiesByProvinceCode(provinceCode);
    }

    /**
     * 根据城市编码获取区县列表
     *
     * @param cityCode 城市编码
     * @return 区县列表
     */
    public List<District> getDistrictsByCity(String cityCode) {
        return cityMapper.selectDistrictsByCityCode(cityCode);
    }

    /**
     * 根据城市编码查询天气
     *
     * @param cityCode 城市编码
     * @return 天气结果
     */
    public WeatherResult getWeatherByCityCode(String cityCode) {
        logger.info("根据城市编码查询天气: {}", cityCode);
        return weatherApiClient.queryWeatherByCityCode(cityCode);
    }

    /**
     * 根据区县编码查询天气
     *
     * @param districtCode 区县编码
     * @return 天气结果
     */
    public WeatherResult getWeatherByDistrictCode(String districtCode) {
        logger.info("根据区县编码查询天气: {}", districtCode);
        // 区县编码前4位是城市编码
        String cityCode = districtCode.substring(0, 4) + "00";
        return weatherApiClient.queryWeatherByCityCode(cityCode);
    }

    /**
     * 根据经纬度查询天气
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 天气结果
     */
    public WeatherResult getWeatherByLocation(Double longitude, Double latitude) {
        logger.info("根据经纬度查询天气: 经度={}, 纬度={}", longitude, latitude);
        return weatherApiClient.queryWeatherByLocation(longitude, latitude);
    }
}
