package com.dublin.dto;

import lombok.Data;
import java.util.List;

@Data
public class ForecastWeatherResponse {
    private List<DailyForecast> list;
    
    @Data
    public static class DailyForecast {
        private Long dt;
        private List<Weather> weather;
        private Temp temp;
        private Double speed;
        
        @Data
        public static class Weather {
            private String main;
            private String description;
        }
        
        @Data
        public static class Temp {
            private Double day;
            private Double min;
            private Double max;
        }
    }
}
