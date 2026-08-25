package com.dublin.client;

import com.dublin.dto.CurrentWeatherResponse;
import com.dublin.dto.ForecastWeatherResponse;
import com.dublin.entity.CurrentWeather;
import com.dublin.entity.ForecastWeather;
import com.dublin.feign.WeatherApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WeatherApiClient {

    @Autowired
    private WeatherApiFeign weatherApiFeign;

    @Value("${api.weather.lat}")
    private String lat;

    @Value("${api.weather.lon}")
    private String lon;

    @Value("${api.weather.app-id}")
    private String appId;

    @Value("${api.weather.units}")
    private String units;

    /**
     * 获取实时天气
     */
    public CurrentWeather fetchCurrentWeather() {
        log.info("使用Feign调用Weather API获取实时天气");
        
        try {
            CurrentWeatherResponse response = weatherApiFeign.getCurrentWeather(lat, lon, appId, units);
            
            CurrentWeather weather = new CurrentWeather();
            
            // 天气时间
            weather.setDt(LocalDateTime.ofInstant(
                Instant.ofEpochSecond(response.getDt()), 
                ZoneId.systemDefault()
            ));
            
            // 天气信息
            if (response.getWeather() != null && !response.getWeather().isEmpty()) {
                CurrentWeatherResponse.Weather weatherInfo = response.getWeather().get(0);
                weather.setMain(weatherInfo.getMain());
                weather.setDescription(weatherInfo.getDescription());
            }
            
            // 温度和风速
            weather.setTemp(response.getMain().getTemp().floatValue());
            weather.setWindSpeed(response.getWind().getSpeed().floatValue());
            
            // 入库时间
            weather.setSnapshotTime(LocalDateTime.now());
            
            log.info("成功获取实时天气: {}", weather.getMain());
            return weather;
        } catch (Exception e) {
            log.error("调用Weather API失败; causeType={}", e.getClass().getSimpleName());
            throw new RuntimeException("获取实时天气失败", e);
        }
    }

    /**
     * 获取未来天气预报
     */
    public List<ForecastWeather> fetchForecastWeather() {
        log.info("使用Feign调用Weather Forecast API获取天气预报");
        
        try {
            ForecastWeatherResponse response = weatherApiFeign.getForecastWeather(lat, lon, appId, units, 3);
            
            LocalDateTime snapshotTime = LocalDateTime.now();
            
            List<ForecastWeather> forecasts = response.getList().stream().map(daily -> {
                ForecastWeather forecast = new ForecastWeather();
                
                // 预测日期
                forecast.setFutureDt(LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(daily.getDt()), 
                    ZoneId.systemDefault()
                ));
                
                // 天气信息
                if (daily.getWeather() != null && !daily.getWeather().isEmpty()) {
                    ForecastWeatherResponse.DailyForecast.Weather weatherInfo = daily.getWeather().get(0);
                    forecast.setMain(weatherInfo.getMain());
                    forecast.setDescription(weatherInfo.getDescription());
                }
                
                // 温度信息
                ForecastWeatherResponse.DailyForecast.Temp temp = daily.getTemp();
                forecast.setTemp12(temp.getDay().floatValue());
                forecast.setTempMin(temp.getMin().floatValue());
                forecast.setTempMax(temp.getMax().floatValue());
                
                // 风速
                forecast.setWindSpeedMax(daily.getSpeed().floatValue());
                
                // 入库时间
                forecast.setSnapshotTime(snapshotTime);
                
                return forecast;
            }).collect(Collectors.toList());
            
            log.info("成功获取{}天天气预报", forecasts.size());
            return forecasts;
        } catch (Exception e) {
            log.error("调用Weather Forecast API失败; causeType={}", e.getClass().getSimpleName());
            throw new RuntimeException("获取天气预报失败", e);
        }
    }
}
