/***** LOCATION *****/

USE iotdevicemanager;

DROP PROCEDURE IF EXISTS get_locations;
DELIMITER $$
CREATE PROCEDURE get_locations (
    IN f_device_id INT,
    IN f_exact_time DATETIME,
    IN f_start_time DATETIME,
    IN f_end_time DATETIME)
BEGIN
    SET @query = "SELECT device_id, coordinates, time FROM location";
    SET @where_clause = " WHERE 1=1";

    IF f_device_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND device_id="', f_device_id, '"');
    END IF;

    IF f_exact_time IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND time = "', f_exact_time, '"');
    ELSE
        IF f_start_time IS NOT NULL THEN
            SET @where_clause = CONCAT(@where_clause, ' AND time >= "', f_start_time, '"');
        END IF;

        IF f_end_time IS NOT NULL THEN
            SET @where_clause = CONCAT(@where_clause, ' AND time <= "', f_end_time, '"');
        END IF;
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS add_location;
DELIMITER $$
CREATE PROCEDURE add_location (
    IN p_device_id INT,
    IN p_coordinates VARCHAR(20),
    IN p_time DATETIME)
BEGIN
    SET @time = p_time;

    IF @time IS NULL THEN
        SET @time = NOW();
    END IF;

    INSERT INTO location(
        device_id,
        coordinates,
        time
    )
    VALUES(
        p_device_id,
        p_coordinates,
        @time
    );

    COMMIT;
    CALL get_locations(p_device_id, @time, NULL, NULL);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS delete_locations;
DELIMITER $$
CREATE PROCEDURE delete_locations (
    IN f_device_id INT,
    IN f_exact_time DATETIME,
    IN f_start_time DATETIME,
    IN f_end_time DATETIME)
BEGIN
    SET @query = "DELETE FROM location";
    SET @where_clause = " WHERE 1=1";
    SET @params_ok = NULL;

    IF f_device_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND device_id="', f_device_id, '"');
        SET @params_ok = 1;
    END IF;

    IF f_exact_time IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND time = "', f_exact_time, '"');
        SET @params_ok = 1;
    ELSE
        IF f_start_time IS NOT NULL THEN
            SET @where_clause = CONCAT(@where_clause, ' AND time >= "', f_start_time, '"');
            SET @params_ok = 1;
        END IF;

        IF f_end_time IS NOT NULL THEN
            SET @where_clause = CONCAT(@where_clause, ' AND time <= "', f_end_time, '"');
            SET @params_ok = 1;
        END IF;
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    IF @params_ok IS NOT NULL THEN
        PREPARE stmt FROM @query;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        COMMIT;
    END IF;

    SELECT (@params_ok IS NOT NULL);
END
$$
DELIMITER ;

GRANT EXECUTE ON PROCEDURE get_locations TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE add_location TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE delete_locations TO 'iot-device-manager-client'@'%';
