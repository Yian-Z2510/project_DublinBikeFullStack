package com.dublin.repository;

import com.dublin.entity.ForecastWeather;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ForecastWeatherRepository {
    
    void insert(ForecastWeather weather);
    
    void insertBatch(List<ForecastWeather> weathers);
    
    List<ForecastWeather> findLatestForecast();
    
    void deleteBySnapshotTimeBefore(@Param("dateTime") LocalDateTime dateTime);
}
