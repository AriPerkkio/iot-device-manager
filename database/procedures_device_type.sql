/***** DEVICE TYPE *****/

USE iotdevicemanager;

DROP PROCEDURE IF EXISTS get_device_types;
DELIMITER $$
CREATE PROCEDURE get_device_types (
    IN f_id INT,
    IN f_name VARCHAR(50),
    IN f_device_icon_id INT)
BEGIN
    SET @query = "SELECT id, name, device_icon_id FROM device_type";
    SET @where_clause = " WHERE 1=1";

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id="', f_id, '"');
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name="', f_name, '"');
    END IF;

    IF f_device_icon_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND device_icon_id="', f_device_icon_id, '"');
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS add_device_type;
DELIMITER $$
CREATE PROCEDURE add_device_type (
    IN p_name VARCHAR(50),
    IN p_device_icon_id INT)
BEGIN
    INSERT INTO device_type(
        name,
        device_icon_id
    )
    VALUES (
        p_name,
        p_device_icon_id
    );

    COMMIT;
    CALL get_device_types(NULL, p_name, p_device_icon_id);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS delete_device_type;
DELIMITER $$
CREATE PROCEDURE delete_device_type (
    IN f_id INT,
    IN f_name VARCHAR(50),
    IN f_device_icon_id INT)
BEGIN
    SET @query = "DELETE FROM device_type";
    SET @where_clause = " WHERE 1=1";
    SET @params_ok = NULL;

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id="', f_id, '"');
        SET @params_ok = 1;
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name="', f_name, '"');
        SET @params_ok = 1;
    END IF;

    IF f_device_icon_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND device_icon_id="', f_device_icon_id, '"');
        SET @params_ok = 1;
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

DROP PROCEDURE IF EXISTS update_device_type;
DELIMITER $$
CREATE PROCEDURE update_device_type (
    IN f_id INT,
    IN f_name VARCHAR(50),
    IN p_name VARCHAR(50),
    IN p_device_icon_id INT)
BEGIN
    SET @query = "UPDATE device_type SET
        name = ?,
        device_icon_id = ?";
    SET @where_clause = " WHERE 1=1";
    SET @params_ok = NULL;
    SET @name = p_name;
    SET @device_icon_id = p_device_icon_id;

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id="', f_id, '"');
        SET @params_ok = 1;
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name="', f_name, '"');
        SET @params_ok = 1;
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    IF @params_ok IS NOT NULL THEN
        PREPARE stmt FROM @query;
        EXECUTE stmt USING @name, @device_icon_id;
        DEALLOCATE PREPARE stmt;
        COMMIT;
        CALL get_device_types(NULL, @name, @device_icon_id);
    END IF;
END
$$
DELIMITER ;

GRANT EXECUTE ON PROCEDURE get_device_types TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE update_device_type TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE delete_device_type TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE add_device_type TO 'iot-device-manager-client'@'%';
