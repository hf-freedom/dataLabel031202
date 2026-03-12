package com.weather.weather;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SICityCacheUtil {
    
    private final Map<String, CityVO> cityCache = new ConcurrentHashMap<>();
    private List<CityVO> provinces = new ArrayList<>();
    
    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("city.json");
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            
            JSONArray jsonArray = JSON.parseArray(sb.toString());
            provinces = parseCityList(jsonArray, null);
            
            for (CityVO province : provinces) {
                cityCache.put(province.getCode(), province);
                if (province.getChildren() != null) {
                    for (CityVO city : province.getChildren()) {
                        cityCache.put(city.getCode(), city);
                        if (city.getChildren() != null) {
                            for (CityVO county : city.getChildren()) {
                                cityCache.put(county.getCode(), county);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private List<CityVO> parseCityList(JSONArray jsonArray, CityVO parent) {
        List<CityVO> list = new ArrayList<>();
        if (jsonArray == null) {
            return list;
        }
        
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            CityVO city = new CityVO();
            city.setCode(obj.getString("code"));
            city.setName(obj.getString("name"));
            
            JSONArray children = obj.getJSONArray("children");
            if (children != null && !children.isEmpty()) {
                city.setChildren(parseCityList(children, city));
            }
            
            list.add(city);
        }
        return list;
    }
    
    public List<CityVO> getProvinces() {
        return provinces;
    }
    
    public List<CityVO> getCities(String provinceCode) {
        CityVO province = cityCache.get(provinceCode);
        if (province != null) {
            return province.getChildren();
        }
        return new ArrayList<>();
    }
    
    public List<CityVO> getCounties(String cityCode) {
        CityVO city = cityCache.get(cityCode);
        if (city != null) {
            return city.getChildren();
        }
        return new ArrayList<>();
    }
    
    public CityVO getCity(String code) {
        return cityCache.get(code);
    }
    
    public String getCityFullName(String code) {
        CityVO county = cityCache.get(code);
        if (county == null) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        for (CityVO province : provinces) {
            if (province.getChildren() != null) {
                for (CityVO city : province.getChildren()) {
                    if (city.getChildren() != null) {
                        for (CityVO c : city.getChildren()) {
                            if (c.getCode().equals(code)) {
                                sb.append(province.getName()).append("-");
                                sb.append(city.getName()).append("-");
                                sb.append(county.getName());
                                return sb.toString();
                            }
                        }
                    }
                }
            }
        }
        return county.getName();
    }
}
