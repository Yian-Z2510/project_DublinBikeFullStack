package com.dublin.service;

import com.dublin.entity.CurrentWeather;
import com.dublin.entity.ForecastWeather;
import com.dublin.repository.CurrentWeatherRepository;
import com.dublin.repository.ForecastWeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class WeatherService {

    @Autowired
    private CurrentWeatherRepository currentWeatherRepository;

    @Autowired
    private ForecastWeatherRepository forecastWeatherRepository;

    /**
     * 保存实时天气数据
     */
    @Transactional
    public void saveCurrentWeather(CurrentWeather weather) {
        log.info("保存实时天气数据");
        currentWeatherRepository.insert(weather);
    }

    /**
     * 保存预测天气数据
     */
    @Transactional
    public void saveForecastWeather(List<ForecastWeather> forecasts) {
        log.info("保存预测天气数据，共{}条", forecasts.size());
        forecastWeatherRepository.insertBatch(forecasts);
    }

    /**
     * 获取最新的实时天气
     */
    public CurrentWeather getLatestCurrentWeather() {
        return currentWeatherRepository.findLatest();
    }

    /**
     * 获取最新的天气预报（未来3天）
     */
    public List<ForecastWeather> getLatestForecast() {
        return forecastWeatherRepository.findLatestForecast();
    }
}
