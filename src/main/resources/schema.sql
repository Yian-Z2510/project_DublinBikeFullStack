-- 创建数据库
CREATE DATABASE IF NOT EXISTS dublin_service DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dublin_service;

-- 1. 自行车站点表（静态数据）
DROP TABLE IF EXISTS bike_station;
CREATE TABLE bike_station (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    number INTEGER NOT NULL UNIQUE COMMENT '站点编号',
    address VARCHAR(128) COMMENT '站点地址',
    banking INTEGER COMMENT '是否支持银行卡支付',
    bike_stands INTEGER COMMENT '车位总数',
    name VARCHAR(128) COMMENT '站点名称',
    position_lat FLOAT COMMENT '纬度',
    position_lng FLOAT COMMENT '经度',
    INDEX idx_number (number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自行车站点静态信息表';

-- 2. 自行车站点状态表（动态数据，每5分钟更新）
DROP TABLE IF EXISTS bike_station_status;
CREATE TABLE bike_station_status (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    number INTEGER NOT NULL COMMENT '站点编号',
    last_update DATETIME NOT NULL COMMENT '最后更新时间',
    available_bikes INTEGER COMMENT '可用自行车数量',
    available_bike_stands INTEGER COMMENT '可用车位数量',
    status VARCHAR(128) COMMENT '站点状态',
    INDEX idx_number (number),
    INDEX idx_last_update (last_update),
    UNIQUE KEY uk_bike_station_status_number_last_update (number, last_update)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自行车站点动态状态表';

-- 3. 实时天气表（每1小时更新）
DROP TABLE IF EXISTS current_weather;
CREATE TABLE current_weather (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    dt DATETIME NOT NULL COMMENT '天气数据时间',
    main VARCHAR(256) COMMENT '天气主要状况',
    description VARCHAR(256) COMMENT '天气详细描述',
    temp FLOAT COMMENT '温度',
    wind_speed FLOAT COMMENT '风速',
    snapshot_time DATETIME NOT NULL COMMENT '数据入库时间',
    INDEX idx_snapshot_time (snapshot_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实时天气数据表';

-- 4. 预测天气表（每1小时更新，未来3天）
DROP TABLE IF EXISTS forecast_weather;
CREATE TABLE forecast_weather (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    future_dt DATETIME NOT NULL COMMENT '预测日期时间',
    main VARCHAR(256) COMMENT '天气主要状况',
    description VARCHAR(256) COMMENT '天气详细描述',
    temp_12 FLOAT COMMENT '12点温度',
    temp_min FLOAT COMMENT '最低温度',
    temp_max FLOAT COMMENT '最高温度',
    wind_speed_max FLOAT COMMENT '最大风速',
    snapshot_time DATETIME NOT NULL COMMENT '数据入库时间',
    INDEX idx_snapshot_time (snapshot_time),
    INDEX idx_future_dt (future_dt),
    INDEX idx_snapshot_future (snapshot_time, future_dt)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预测天气数据表';
