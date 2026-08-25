package com.dublin.feign;

import com.dublin.dto.BikeStationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
    name = "bike-api",
    url = "${api.bike.url}",
    configuration = FeignConfig.class
)
public interface BikeApiFeign {
    
    /**
     * 获取所有站点信息
     */
    @GetMapping
    List<BikeStationResponse> getStations(
        @RequestParam("contract") String contract,
        @RequestParam("apiKey") String apiKey
    );
}
