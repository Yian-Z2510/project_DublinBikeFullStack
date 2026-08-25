package com.dublin.dto;

import lombok.Data;
import java.util.List;

@Data
public class CurrentWeatherResponse {
    private Long dt;
    private List<Weather> weather;
    private Main main;
    private Wind wind;
    
    @Data
    public static class Weather {
        private String main;
        private String description;
    }
    
    @Data
    public static class Main {
        private Double temp;
    }
    
    @Data
    public static class Wind {
        private Double speed;
    }
}
