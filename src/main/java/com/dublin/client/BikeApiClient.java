package com.dublin.client;

import com.dublin.dto.BikeStationResponse;
import com.dublin.entity.BikeStation;
import com.dublin.entity.BikeStationStatus;
import com.dublin.feign.BikeApiFeign;
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
public class BikeApiClient {

    @Autowired
    private BikeApiFeign bikeApiFeign;

    @Value("${api.bike.contract}")
    private String contract;

    @Value("${api.bike.api-key}")
    private String apiKey;

    /**
     * 获取所有站点信息（静态数据）
     */
    public List<BikeStation> fetchAllStations() {
        log.info("使用Feign调用Bike API获取站点信息");
        
        try {
            List<BikeStationResponse> responses = bikeApiFeign.getStations(contract, apiKey);
            
            List<BikeStation> stations = responses.stream().map(response -> {
                BikeStation station = new BikeStation();
                station.setNumber(response.getNumber());
                station.setName(response.getName());
                station.setAddress(response.getAddress());
                station.setBanking(response.getBanking() ? 1 : 0);
                station.setBikeStands(response.getBikeStands());
                station.setPositionLat(response.getPosition().getLat().floatValue());
                station.setPositionLng(response.getPosition().getLng().floatValue());
                return station;
            }).collect(Collectors.toList());
            
            log.info("成功获取{}个站点信息", stations.size());
            return stations;
        } catch (Exception e) {
            log.error("调用Bike API失败; causeType={}", e.getClass().getSimpleName());
            throw new RuntimeException("获取站点信息失败", e);
        }
    }

    /**
     * 获取所有站点的动态状态
     */
    public List<BikeStationStatus> fetchAllStationStatus() {
        log.info("使用Feign调用Bike API获取站点状态");
        
        try {
            List<BikeStationResponse> responses = bikeApiFeign.getStations(contract, apiKey);
            
            List<BikeStationStatus> statuses = responses.stream().map(response -> {
                BikeStationStatus status = new BikeStationStatus();
                status.setNumber(response.getNumber());
                status.setAvailableBikes(response.getAvailableBikes());
                status.setAvailableBikeStands(response.getAvailableBikeStands());
                status.setStatus(response.getStatus());
                
                // 转换时间戳
                LocalDateTime lastUpdate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(response.getLastUpdate()), 
                    ZoneId.systemDefault()
                );
                status.setLastUpdate(lastUpdate);
                
                return status;
            }).collect(Collectors.toList());
            
            log.info("成功获取{}个站点状态", statuses.size());
            return statuses;
        } catch (Exception e) {
            log.error("调用Bike API获取状态失败; causeType={}", e.getClass().getSimpleName());
            throw new RuntimeException("获取站点状态失败", e);
        }
    }
}
