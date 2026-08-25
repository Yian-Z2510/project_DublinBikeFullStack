package com.dublin.entity;

import lombok.Data;

@Data
public class BikeStation {
    private Long id;
    private Integer number;
    private String address;
    private Integer banking;
    private Integer bikeStands;
    private String name;
    private Float positionLat;
    private Float positionLng;
}
