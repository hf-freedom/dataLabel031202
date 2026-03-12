package com.example.weather.entity;

import java.util.List;

/**
 * 城市实体类
 */
public class City {

    /**
     * 城市编码
     */
    private String code;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 区县列表
     */
    private List<District> districts;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
}
