package com.dublin.service;

import com.dublin.dto.BikeStationDTO;
import com.dublin.entity.BikeStation;
import com.dublin.entity.BikeStationStatus;
import com.dublin.repository.BikeStationRepository;
import com.dublin.repository.BikeStationStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BikeService {

    @Autowired
    private BikeStationRepository bikeStationRepository;

    @Autowired
    private BikeStationStatusRepository bikeStationStatusRepository;

    @Transactional
    public void initBikeStations(List<BikeStation> stations) {
        log.info("??????????????{}???", stations.size());
        bikeStationRepository.insertBatch(stations);
        log.info("????????????");
    }

    @Transactional
    public void saveBikeStationStatus(List<BikeStationStatus> statuses) {
        log.info("?????????????{}?", statuses.size());
        bikeStationStatusRepository.insertBatch(statuses);
    }

    public List<BikeStationDTO> getAllStationsWithStatus() {
        List<BikeStation> stations = bikeStationRepository.findAll();
        
        return stations.stream().map(station -> {
            BikeStationDTO dto = new BikeStationDTO();
            dto.setNumber(station.getNumber());
            dto.setName(station.getName());
            dto.setAddress(station.getAddress());
            dto.setBanking(station.getBanking());
            dto.setBikeStands(station.getBikeStands());
            dto.setPositionLat(station.getPositionLat());
            dto.setPositionLng(station.getPositionLng());
            
            BikeStationStatus status = bikeStationStatusRepository.findLatestByNumber(station.getNumber());
            if (status != null) {
                dto.setAvailableBikes(status.getAvailableBikes());
                dto.setAvailableBikeStands(status.getAvailableBikeStands());
                dto.setStatus(status.getStatus());
                dto.setLastUpdate(status.getLastUpdate());
            }
            
            return dto;
        }).collect(Collectors.toList());
    }

    public BikeStationDTO getStationByNumber(Integer number) {
        BikeStation station = bikeStationRepository.findById(number);
        if (station == null) {
            return null;
        }
        
        BikeStationDTO dto = new BikeStationDTO();
        dto.setNumber(station.getNumber());
        dto.setName(station.getName());
        dto.setAddress(station.getAddress());
        dto.setBanking(station.getBanking());
        dto.setBikeStands(station.getBikeStands());
        dto.setPositionLat(station.getPositionLat());
        dto.setPositionLng(station.getPositionLng());
        
        BikeStationStatus status = bikeStationStatusRepository.findLatestByNumber(number);
        if (status != null) {
            dto.setAvailableBikes(status.getAvailableBikes());
            dto.setAvailableBikeStands(status.getAvailableBikeStands());
            dto.setStatus(status.getStatus());
            dto.setLastUpdate(status.getLastUpdate());
        }
        
        return dto;
    }

    public List<BikeStationStatus> getStationHistory(Integer number, Integer requestedLimit) {
        int limit = requestedLimit == null ? 24 : Math.max(1, Math.min(requestedLimit, 288));
        List<BikeStationStatus> history = bikeStationStatusRepository.findRecentByNumber(number, limit);
        Collections.reverse(history);
        return history;
    }
}
