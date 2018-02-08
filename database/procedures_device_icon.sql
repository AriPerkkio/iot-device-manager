/***** DEVICE ICON *****/

USE iotdevicemanager;

DROP PROCEDURE IF EXISTS get_device_icons;
DELIMITER $$
CREATE PROCEDURE get_device_icons (
    IN f_id INT,
    IN f_name VARCHAR(25))
BEGIN
    SET @query = "SELECT id, name FROM device_icon";
    SET @where_clause = " WHERE 1=1";

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id="', f_id, '"');
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name="', f_name, '"');
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS add_device_icon;
DELIMITER $$
CREATE PROCEDURE add_device_icon (
    IN p_name VARCHAR(25))
BEGIN
    INSERT INTO device_icon(name) VALUES (p_name);
    COMMIT;
    CALL get_device_icons(NULL, p_name);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS delete_device_icon;
DELIMITER $$
CREATE PROCEDURE delete_device_icon (
    IN f_id INT,
    IN f_name VARCHAR(25))
BEGIN
    SET @query = "DELETE FROM device_icon";
    SET @where_clause = " WHERE 1=1";
    SET @params_ok = NULL;

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id = "', f_id, '"');
        SET @params_ok = 1;
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name = "', f_name, '"');
        SET @params_ok = 1;
    END IF;

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

DROP PROCEDURE IF EXISTS update_device_icon;
DELIMITER $$
CREATE PROCEDURE update_device_icon (
    IN f_id INT,
    IN f_name VARCHAR(25),
    IN p_name VARCHAR(25))
BEGIN
    SET @query = "UPDATE device_icon SET name = ? ";
    SET @where_clause = " WHERE 1=1";
    SET @params_ok = NULL;
    SET @name = p_name;

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id = "', f_id, '"');
        SET @params_ok = 1;
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name = "', f_name, '"');
        SET @params_ok = 1;
    END IF;

    IF @params_ok IS NOT NULL THEN
        PREPARE stmt FROM @query;
        EXECUTE stmt USING @name;
        DEALLOCATE PREPARE stmt;
        COMMIT;
        CALL get_device_icons(NULL, @name);
    END IF;
END
$$
DELIMITER ;

GRANT EXECUTE ON PROCEDURE get_device_icons TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE add_device_icon TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE delete_device_icon TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE update_device_icon TO 'iot-device-manager-client'@'%';
