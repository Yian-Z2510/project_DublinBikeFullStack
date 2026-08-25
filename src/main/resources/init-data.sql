USE dublin_service;

-- 创建数据清理存储过程
DROP PROCEDURE IF EXISTS clean_old_data;

DELIMITER $$

CREATE PROCEDURE clean_old_data()
BEGIN
    DECLARE seven_days_ago DATETIME;
    SET seven_days_ago = DATE_SUB(NOW(), INTERVAL 7 DAY);
    
    -- 清理7天前的自行车站点状态数据
    DELETE FROM bike_station_status WHERE last_update < seven_days_ago;
    
    -- 清理7天前的实时天气数据
    DELETE FROM current_weather WHERE snapshot_time < seven_days_ago;
    
    -- 清理7天前的预测天气数据
    DELETE FROM forecast_weather WHERE snapshot_time < seven_days_ago;
    
    -- 输出清理结果
    SELECT 
        CONCAT('已清理 ', ROW_COUNT(), ' 条7天前的数据') AS result,
        NOW() AS clean_time;
END$$

DELIMITER ;

-- 可以手动调用清理过程
-- CALL clean_old_data();
