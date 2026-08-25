package com.dublin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BikeStationResponse {
    private Integer number;
    private String name;
    private String address;
    private Boolean banking;
    
    @JsonProperty("bike_stands")
    private Integer bikeStands;
    
    @JsonProperty("available_bikes")
    private Integer availableBikes;
    
    @JsonProperty("available_bike_stands")
    private Integer availableBikeStands;
    
    private String status;
    
    @JsonProperty("last_update")
    private Long lastUpdate;
    
    private Position position;
    
    @Data
    public static class Position {
        private Double lat;
        private Double lng;
    }
}
