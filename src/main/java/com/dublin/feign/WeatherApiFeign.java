package com.dublin.feign;

import com.dublin.dto.CurrentWeatherResponse;
import com.dublin.dto.ForecastWeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "weather-api",
    url = "${api.weather.url}",
    configuration = FeignConfig.class
)
public interface WeatherApiFeign {
    
    /**
     * 获取实时天气
     */
    @GetMapping("/weather")
    CurrentWeatherResponse getCurrentWeather(
        @RequestParam("lat") String lat,
        @RequestParam("lon") String lon,
        @RequestParam("appid") String appid,
        @RequestParam("units") String units
    );
    
    /**
     * 获取天气预报
     */
    @GetMapping("/forecast/daily")
    ForecastWeatherResponse getForecastWeather(
        @RequestParam("lat") String lat,
        @RequestParam("lon") String lon,
        @RequestParam("appid") String appid,
        @RequestParam("units") String units,
        @RequestParam("cnt") Integer cnt
    );
}
