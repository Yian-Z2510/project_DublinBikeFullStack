package com.dublin.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "实时天气信息")
public class CurrentWeather {
    
    @ApiModelProperty(value = "记录ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "天气数据时间", example = "2026-01-01T12:00:00")
    private LocalDateTime dt;
    
    @ApiModelProperty(value = "天气主要状况", example = "Clouds", notes = "如：Clear, Clouds, Rain等")
    private String main;
    
    @ApiModelProperty(value = "天气详细描述", example = "broken clouds")
    private String description;
    
    @ApiModelProperty(value = "温度（摄氏度）", example = "7.71")
    private Float temp;
    
    @ApiModelProperty(value = "风速（米/秒）", example = "2.57")
    private Float windSpeed;
    
    @ApiModelProperty(value = "数据入库时间", example = "2026-01-01T12:05:00")
    private LocalDateTime snapshotTime;
}
