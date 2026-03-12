package com.example.weather.controller;

import com.example.weather.dto.ApiResponse;
import com.example.weather.dto.WeatherResult;
import com.example.weather.entity.City;
import com.example.weather.entity.District;
import com.example.weather.entity.Province;
import com.example.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 天气查询控制器
 */
@RestController
@RequestMapping("/api/weather")
@Validated
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService weatherService;

    /**
     * 获取所有省份列表
     *
     * @return 省份列表
     */
    @GetMapping("/provinces")
    public ApiResponse<List<Province>> getProvinces() {
        logger.info("获取省份列表");
        List<Province> provinces = weatherService.getAllProvinces();
        return ApiResponse.success(provinces);
    }

    /**
     * 根据省份编码获取城市列表
     *
     * @param provinceCode 省份编码
     * @return 城市列表
     */
    @GetMapping("/cities/{provinceCode}")
    public ApiResponse<List<City>> getCities(@PathVariable @NotBlank String provinceCode) {
        logger.info("获取城市列表, 省份编码: {}", provinceCode);
        List<City> cities = weatherService.getCitiesByProvince(provinceCode);
        return ApiResponse.success(cities);
    }

    /**
     * 根据城市编码获取区县列表
     *
     * @param cityCode 城市编码
     * @return 区县列表
     */
    @GetMapping("/districts/{cityCode}")
    public ApiResponse<List<District>> getDistricts(@PathVariable @NotBlank String cityCode) {
        logger.info("获取区县列表, 城市编码: {}", cityCode);
        List<District> districts = weatherService.getDistrictsByCity(cityCode);
        return ApiResponse.success(districts);
    }

    /**
     * 根据城市编码查询天气
     *
     * @param cityCode 城市编码
     * @return 天气信息
     */
    @GetMapping("/queryByCity")
    public ApiResponse<WeatherResult> queryByCity(@RequestParam @NotBlank String cityCode) {
        logger.info("查询天气, 城市编码: {}", cityCode);
        WeatherResult result = weatherService.getWeatherByCityCode(cityCode);
        if (result.getSuccess()) {
            return ApiResponse.success(result);
        } else {
            return ApiResponse.error(result.getMessage());
        }
    }

    /**
     * 根据区县编码查询天气
     *
     * @param districtCode 区县编码
     * @return 天气信息
     */
    @GetMapping("/queryByDistrict")
    public ApiResponse<WeatherResult> queryByDistrict(@RequestParam @NotBlank String districtCode) {
        logger.info("查询天气, 区县编码: {}", districtCode);
        WeatherResult result = weatherService.getWeatherByDistrictCode(districtCode);
        if (result.getSuccess()) {
            return ApiResponse.success(result);
        } else {
            return ApiResponse.error(result.getMessage());
        }
    }

    /**
     * 根据经纬度查询天气
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 天气信息
     */
    @GetMapping("/queryByLocation")
    public ApiResponse<WeatherResult> queryByLocation(
            @RequestParam @NotNull Double longitude,
            @RequestParam @NotNull Double latitude) {
        logger.info("查询天气, 经度: {}, 纬度: {}", longitude, latitude);
        WeatherResult result = weatherService.getWeatherByLocation(longitude, latitude);
        if (result.getSuccess()) {
            return ApiResponse.success(result);
        } else {
            return ApiResponse.error(result.getMessage());
        }
    }
}
