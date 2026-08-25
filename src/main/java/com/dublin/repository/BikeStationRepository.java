package com.dublin.repository;

import com.dublin.entity.BikeStation;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BikeStationRepository {
    
    void insert(BikeStation bikeStation);
    
    void insertBatch(List<BikeStation> bikeStations);
    
    BikeStation findById(Integer number);
    
    List<BikeStation> findAll();
}
