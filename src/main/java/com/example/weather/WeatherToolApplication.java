package com.example.weather;

import com.example.weather.config.AmapConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AmapConfig.class)
public class WeatherToolApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherToolApplication.class, args);
    }
}
