package com.dublin.repository;

import com.dublin.entity.CurrentWeather;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;

@Mapper
public interface CurrentWeatherRepository {
    
    void insert(CurrentWeather weather);
    
    CurrentWeather findLatest();
    
    void deleteBySnapshotTimeBefore(@Param("dateTime") LocalDateTime dateTime);
}
