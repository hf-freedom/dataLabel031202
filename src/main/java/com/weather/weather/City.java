package com.weather.weather;

import lombok.Data;
import java.util.List;

@Data
public class City {
    private String code;
    private String name;
    private String parentCode;
    private Integer level;
    private String longitude;
    private String latitude;
}
