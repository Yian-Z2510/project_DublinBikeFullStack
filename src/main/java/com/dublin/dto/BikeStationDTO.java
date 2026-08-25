package com.dublin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "自行车站点信息")
public class BikeStationDTO {
    
    @ApiModelProperty(value = "站点编号", example = "42")
    private Integer number;
    
    @ApiModelProperty(value = "站点名称", example = "SMITHFIELD NORTH")
    private String name;
    
    @ApiModelProperty(value = "站点地址", example = "Smithfield North")
    private String address;
    
    @ApiModelProperty(value = "是否支持银行卡支付", example = "0", notes = "0-不支持，1-支持")
    private Integer banking;
    
    @ApiModelProperty(value = "纬度", example = "53.349562")
    private Float positionLat;
    
    @ApiModelProperty(value = "经度", example = "-6.278198")
    private Float positionLng;
    
    @ApiModelProperty(value = "车位总数", example = "30")
    private Integer bikeStands;
    
    @ApiModelProperty(value = "可用自行车数量", example = "24")
    private Integer availableBikes;
    
    @ApiModelProperty(value = "可用车位数量", example = "6")
    private Integer availableBikeStands;
    
    @ApiModelProperty(value = "站点状态", example = "OPEN", notes = "OPEN-开放，CLOSED-关闭")
    private String status;
    
    @ApiModelProperty(value = "最后更新时间", example = "2026-01-01T12:00:00")
    private LocalDateTime lastUpdate;
}
