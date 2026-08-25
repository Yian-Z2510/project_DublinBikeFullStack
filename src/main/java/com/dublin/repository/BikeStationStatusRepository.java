package com.dublin.repository;

import com.dublin.entity.BikeStationStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BikeStationStatusRepository {
    
    void insert(BikeStationStatus status);
    
    void insertBatch(List<BikeStationStatus> statuses);
    
    List<BikeStationStatus> findByNumber(@Param("number") Integer number);
    
    BikeStationStatus findLatestByNumber(@Param("number") Integer number);

    List<BikeStationStatus> findRecentByNumber(
            @Param("number") Integer number,
            @Param("limit") Integer limit);
    
    void deleteByLastUpdateBefore(@Param("dateTime") LocalDateTime dateTime);
}
