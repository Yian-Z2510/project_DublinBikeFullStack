package com.dublin.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "预测天气信息")
public class ForecastWeather {
    
    @ApiModelProperty(value = "记录ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "预测日期时间", example = "2026-01-02T12:00:00")
    private LocalDateTime futureDt;
    
    @ApiModelProperty(value = "天气主要状况", example = "Clear", notes = "如：Clear, Clouds, Rain等")
    private String main;
    
    @ApiModelProperty(value = "天气详细描述", example = "sky is clear")
    private String description;
    
    @ApiModelProperty(value = "12点温度（摄氏度）", example = "9.36")
    private Float temp12;
    
    @ApiModelProperty(value = "最低温度（摄氏度）", example = "1.29")
    private Float tempMin;
    
    @ApiModelProperty(value = "最高温度（摄氏度）", example = "9.8")
    private Float tempMax;
    
    @ApiModelProperty(value = "最大风速（米/秒）", example = "3.92")
    private Float windSpeedMax;
    
    @ApiModelProperty(value = "数据入库时间", example = "2026-01-01T12:00:00")
    private LocalDateTime snapshotTime;
}
