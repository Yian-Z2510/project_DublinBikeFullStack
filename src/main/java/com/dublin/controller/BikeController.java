package com.dublin.controller;

import com.dublin.dto.BikeStationDTO;
import com.dublin.entity.BikeStationStatus;
import com.dublin.service.BikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bike")
@CrossOrigin(origins = "*")
@Api(tags = "自行车站点接口", description = "提供都柏林共享单车站点信息查询服务")
public class BikeController {

    @Autowired
    private BikeService bikeService;

    /**
     * 查询所有自行车站点信息（包含最新状态）
     * 供前端地图调用
     */
    @ApiOperation(value = "查询所有站点信息", notes = "获取所有自行车站点的静态信息和最新动态状态，供前端地图展示使用")
    @GetMapping("/stations")
    public ResponseEntity<List<BikeStationDTO>> getAllStations() {
        log.info("查询所有自行车站点信息");
        List<BikeStationDTO> stations = bikeService.getAllStationsWithStatus();
        return ResponseEntity.ok(stations);
    }

    /**
     * 根据站点编号查询站点信息（包含最新状态）
     * 供前端地图调用
     */
    @ApiOperation(value = "根据站点编号查询", notes = "根据站点编号查询单个站点的详细信息和最新状态")
    @GetMapping("/stations/{number}")
    public ResponseEntity<BikeStationDTO> getStationByNumber(
            @ApiParam(value = "站点编号", required = true, example = "42")
            @PathVariable Integer number) {
        log.info("查询站点信息: {}", number);
        BikeStationDTO station = bikeService.getStationByNumber(number);
        if (station == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(station);
    }

    @ApiOperation(value = "Query station history", notes = "Returns recent availability snapshots in chronological order")
    @GetMapping("/stations/{number}/history")
    public ResponseEntity<List<BikeStationStatus>> getStationHistory(
            @PathVariable Integer number,
            @RequestParam(defaultValue = "24") Integer limit) {
        if (bikeService.getStationByNumber(number) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bikeService.getStationHistory(number, limit));
    }
}
