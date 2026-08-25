package com.dublin.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BikeStationStatus {
    private Long id;
    private Integer number;
    private LocalDateTime lastUpdate;
    private Integer availableBikes;
    private Integer availableBikeStands;
    private String status;
}
