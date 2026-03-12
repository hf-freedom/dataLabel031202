package com.example.weather.mapper;

import com.example.weather.entity.City;
import com.example.weather.entity.District;
import com.example.weather.entity.Province;
import com.example.weather.util.SIWeatherDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市数据Mapper
 * 从本地缓存中查询城市数据
 */
@Repository
public class CityMapper {

    @Autowired
    private SIWeatherDataLoader dataLoader;

    /**
     * 查询所有省份
     *
     * @return 省份列表
     */
    public List<Province> selectAllProvinces() {
        return dataLoader.getAllProvinces();
    }

    /**
     * 根据省份编码查询城市列表
     *
     * @param provinceCode 省份编码
     * @return 城市列表
     */
    public List<City> selectCitiesByProvinceCode(String provinceCode) {
        List<Province> provinces = dataLoader.getAllProvinces();
        for (Province province : provinces) {
            if (province.getCode().equals(provinceCode)) {
                return province.getCities();
            }
        }
        return new ArrayList<>();
    }

    /**
     * 根据城市编码查询区县列表
     *
     * @param cityCode 城市编码
     * @return 区县列表
     */
    public List<District> selectDistrictsByCityCode(String cityCode) {
        List<Province> provinces = dataLoader.getAllProvinces();
        for (Province province : provinces) {
            for (City city : province.getCities()) {
                if (city.getCode().equals(cityCode)) {
                    return city.getDistricts();
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * 根据区县编码查询区县信息
     *
     * @param districtCode 区县编码
     * @return 区县信息
     */
    public District selectDistrictByCode(String districtCode) {
        List<Province> provinces = dataLoader.getAllProvinces();
        for (Province province : provinces) {
            for (City city : province.getCities()) {
                for (District district : city.getDistricts()) {
                    if (district.getCode().equals(districtCode)) {
                        return district;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根据编码查询名称
     *
     * @param code 编码
     * @return 名称
     */
    public String selectNameByCode(String code) {
        return dataLoader.getNameByCode(code);
    }

    /**
     * 根据名称查询编码
     *
     * @param name 名称
     * @return 编码
     */
    public String selectCodeByName(String name) {
        return dataLoader.getCodeByName(name);
    }

    /**
     * 根据城市编码查询城市名称
     *
     * @param cityCode 城市编码
     * @return 城市名称
     */
    public String selectCityNameByCode(String cityCode) {
        return dataLoader.getCityNameByCode(cityCode);
    }
}
