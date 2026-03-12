package com.weather.mapper;

import com.weather.weather.CityVO;
import com.weather.weather.SICityCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityMapper {
    
    @Autowired
    private SICityCacheUtil cityCacheUtil;
    
    public List<CityVO> findAllProvinces() {
        return cityCacheUtil.getProvinces();
    }
    
    public List<CityVO> findCitiesByProvinceCode(String provinceCode) {
        return cityCacheUtil.getCities(provinceCode);
    }
    
    public List<CityVO> findCountiesByCityCode(String cityCode) {
        return cityCacheUtil.getCounties(cityCode);
    }
    
    public CityVO findByCode(String code) {
        return cityCacheUtil.getCity(code);
    }
    
    public String findCityFullName(String code) {
        return cityCacheUtil.getCityFullName(code);
    }
}
