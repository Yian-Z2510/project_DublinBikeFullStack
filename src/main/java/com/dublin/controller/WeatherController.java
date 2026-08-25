package com.dublin.controller;

import com.dublin.entity.CurrentWeather;
import com.dublin.entity.ForecastWeather;
import com.dublin.service.WeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
@Api(tags = "天气信息接口", description = "提供都柏林实时天气和天气预报查询服务")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * 获取最新的实时天气
     * 供前端地图调用，返回数据库中最新的天气信息
     */
    @ApiOperation(value = "查询实时天气", notes = "获取都柏林最新的实时天气信息，数据每1小时更新一次")
    @GetMapping("/current")
    public ResponseEntity<CurrentWeather> getCurrentWeather() {
        log.info("查询最新实时天气");
        CurrentWeather weather = weatherService.getLatestCurrentWeather();
        if (weather == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(weather);
    }

    /**
     * 获取未来3天的天气预报
     * 供前端地图调用，返回数据库中最新的天气预报
     */
    @ApiOperation(value = "查询天气预报", notes = "获取都柏林未来3天的天气预报，数据每1小时更新一次")
    @GetMapping("/forecast")
    public ResponseEntity<List<ForecastWeather>> getForecastWeather() {
        log.info("查询未来3天天气预报");
        List<ForecastWeather> forecasts = weatherService.getLatestForecast();
        return ResponseEntity.ok(forecasts);
    }
}
