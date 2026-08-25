package com.dublin.task;

import com.dublin.client.BikeApiClient;
import com.dublin.client.WeatherApiClient;
import com.dublin.entity.BikeStation;
import com.dublin.entity.BikeStationStatus;
import com.dublin.entity.CurrentWeather;
import com.dublin.entity.ForecastWeather;
import com.dublin.repository.BikeStationRepository;
import com.dublin.repository.BikeStationStatusRepository;
import com.dublin.repository.CurrentWeatherRepository;
import com.dublin.repository.ForecastWeatherRepository;
import com.dublin.service.BikeService;
import com.dublin.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ScheduledTasks implements CommandLineRunner {

    @Autowired
    private BikeApiClient bikeApiClient;

    @Autowired
    private WeatherApiClient weatherApiClient;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private BikeStationRepository bikeStationRepository;

    @Autowired
    private BikeStationStatusRepository bikeStationStatusRepository;

    @Autowired
    private CurrentWeatherRepository currentWeatherRepository;

    @Autowired
    private ForecastWeatherRepository forecastWeatherRepository;

    /**
     * 应用启动时初始化站点数据
     */
    @Override
    public void run(String... args) {
        log.info("应用启动，开始初始化数据...");

        try {
            // 检查是否已有站点数据
            List<BikeStation> existingStations = bikeStationRepository.findAll();
            if (existingStations == null || existingStations.isEmpty()) {
                log.info("首次启动，初始化自行车站点静态数据");
                List<BikeStation> stations = bikeApiClient.fetchAllStations();
                bikeService.initBikeStations(stations);
            } else {
                log.info("站点数据已存在，跳过初始化");
            }
        } catch (Exception e) {
            log.error("初始化站点数据失败; causeType={}", e.getClass().getSimpleName());
        }

        // Each refresh handles its own failure so one provider cannot block the others.
        updateBikeStationStatus();
        updateCurrentWeather();
        updateForecastWeather();
    }

    /**
     * 每5分钟更新自行车站点状态
     */
    @Scheduled(cron = "${schedule.bike.cron}")
    public void updateBikeStationStatus() {
        log.info("开始更新自行车站点状态...");
        try {
            List<BikeStationStatus> statuses = bikeApiClient.fetchAllStationStatus();
            bikeService.saveBikeStationStatus(statuses);
            log.info("自行车站点状态更新完成");
        } catch (Exception e) {
            log.error("更新自行车站点状态失败; causeType={}", e.getClass().getSimpleName());
        }
    }

    /**
     * 每1小时更新实时天气
     */
    @Scheduled(cron = "${schedule.weather.cron}")
    public void updateCurrentWeather() {
        log.info("开始更新实时天气...");
        try {
            CurrentWeather weather = weatherApiClient.fetchCurrentWeather();
            weatherService.saveCurrentWeather(weather);
            log.info("实时天气更新完成");
        } catch (Exception e) {
            log.error("更新实时天气失败; causeType={}", e.getClass().getSimpleName());
        }
    }

    /**
     * 每1小时更新天气预报
     */
    @Scheduled(cron = "${schedule.weather.cron}")
    public void updateForecastWeather() {
        log.info("开始更新天气预报...");
        try {
            List<ForecastWeather> forecasts = weatherApiClient.fetchForecastWeather();
            weatherService.saveForecastWeather(forecasts);
            log.info("天气预报更新完成");
        } catch (Exception e) {
            log.error("更新天气预报失败; causeType={}", e.getClass().getSimpleName());
        }
    }

    /**
     * 每天凌晨2点清理7天前的数据
     */
    @Scheduled(cron = "${schedule.cleanup.cron}")
    public void cleanupOldData() {
        log.info("开始清理7天前的数据...");
        try {
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            
            bikeStationStatusRepository.deleteByLastUpdateBefore(sevenDaysAgo);
            log.info("清理自行车站点状态数据完成");
            
            currentWeatherRepository.deleteBySnapshotTimeBefore(sevenDaysAgo);
            log.info("清理实时天气数据完成");
            
            forecastWeatherRepository.deleteBySnapshotTimeBefore(sevenDaysAgo);
            log.info("清理天气预报数据完成");
            
            log.info("数据清理完成");
        } catch (Exception e) {
            log.error("清理数据失败; causeType={}", e.getClass().getSimpleName());
        }
    }
}
