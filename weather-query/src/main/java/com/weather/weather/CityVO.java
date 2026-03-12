package com.weather.weather;

import lombok.Data;
import java.util.List;

@Data
public class CityVO {
    private String code;
    private String name;
    private List<CityVO> children;
}
