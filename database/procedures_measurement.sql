/***** MEASUREMENT *****/

DROP PROCEDURE IF EXISTS get_measurements;
DELIMITER $$
CREATE PROCEDURE get_measurements (
    IN f_device_id INT,
    IN f_exact_time DATETIME,
    IN f_start_time DATETIME,
    IN f_end_time DATETIME)
BEGIN
    SET @query = "SELECT device_id, content, time FROM measurement";
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

DROP PROCEDURE IF EXISTS add_measurement;
DELIMITER $$
CREATE PROCEDURE add_measurement (
    IN p_device_id INT,
    IN p_content JSON,
    IN p_time DATETIME)
BEGIN
    SET @time = p_time;

    IF @time IS NULL THEN
        SET @time = NOW();
    END IF;

    INSERT INTO measurement(
        device_id,
        content,
        time
    )
    VALUES(
        p_device_id,
        p_content,
        @time
    );

    CALL get_measurements(p_device_id, @time, NULL, NULL);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS delete_measurements;
DELIMITER $$
CREATE PROCEDURE delete_measurements (
    IN f_device_id INT,
    IN f_exact_time DATETIME,
    IN f_start_time DATETIME,
    IN f_end_time DATETIME)
BEGIN
    SET @query = "DELETE FROM measurement";
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
    END IF;

    SELECT (@params_ok IS NOT NULL);
END
$$
DELIMITER ;

GRANT EXECUTE ON PROCEDURE get_measurements TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE add_measurement TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE delete_measurements TO 'iot-device-manager-client'@'%';
