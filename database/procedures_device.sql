/***** DEVICE *****/

USE iotdevicemanager;

DROP PROCEDURE IF EXISTS get_devices;
DELIMITER $$
CREATE PROCEDURE get_devices (
    IN f_id INT,
    IN f_name VARCHAR(50),
    IN f_device_type_id INT,
    IN f_device_group_id INT,
    IN f_configuration_id INT,
    IN f_authentication_key VARCHAR(32))
BEGIN
    SET @query = "SELECT id, name, device_type_id, device_group_id, 
        configuration_id, authentication_key FROM device";
    SET @where_clause = " WHERE 1=1";

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id="', f_id, '"');
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name="', f_name, '"');
    END IF;

    IF f_device_type_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND device_type_id="', f_device_type_id, '"');
    END IF;

    IF f_device_group_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND device_group_id="', f_device_group_id, '"');
    END IF;

    IF f_configuration_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND configuration_id="', f_configuration_id, '"');
    END IF;

    IF f_authentication_key IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND authentication_key="', f_authentication_key, '"');
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS add_device;
DELIMITER $$
CREATE PROCEDURE add_device (
    IN p_name VARCHAR(50),
    IN p_device_type_id INT,
    IN p_device_group_id INT,
    IN p_configuration_id INT)
BEGIN
    INSERT INTO device (
        name,
        device_type_id,
        device_group_id,
        configuration_id
    )
    VALUES (
        p_name,
        p_device_type_id,
        p_device_group_id,
        p_configuration_id
    );

    COMMIT;
    CALL get_devices(NULL, p_name, p_device_type_id, p_device_group_id, p_configuration_id, NULL);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS delete_device;
DELIMITER $$
CREATE PROCEDURE delete_device (
    IN f_id INT,
    IN f_name VARCHAR(50),
    IN f_authentication_key VARCHAR(32))
BEGIN
    SET @query = "DELETE FROM device ";
    SET @where_clause = " WHERE 1=1";
    SET @params_ok = NULL;

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id ="', f_id, '"');
        SET @params_ok = 1;
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name ="', f_name, '"');
        SET @params_ok = 1;
    END IF;
    
    IF f_authentication_key IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND authentication_key ="', f_authentication_key, '"');
        SET @params_ok = 1;
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    IF @params_ok IS NOT NULL THEN
        PREPARE stmt FROM @query;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
    
    COMMIT;
    SELECT (@params_ok IS NOT NULL);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS update_device;
DELIMITER $$
CREATE PROCEDURE update_device (
    IN f_id INT,
    IN f_name VARCHAR(50),
    IN f_authentication_key VARCHAR(32),
    IN p_name VARCHAR(50),
    IN p_device_type_id INT,
    IN p_device_group_id INT,
    IN p_configuration_id INT)
BEGIN
    SET @query = "UPDATE device SET
        name = ?,
        device_type_id = ?,
        device_group_id = ?,
        configuration_id = ?";
    SET @where_clause = " WHERE 1=1";
    SET @params_ok = NULL;

    SET @name = p_name;
    SET @device_type_id = p_device_type_id;
    SET @device_group_id = p_device_group_id;
    SET @configuration_id = p_configuration_id;

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id="', f_id, '"');
        SET @params_ok = 1;
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name="', f_name, '"');
        SET @params_ok = 1;
    END IF;

    IF f_authentication_key IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND authentication_key="', f_authentication_key, '"');
        SET @params_ok = 1;
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    IF @params_ok IS NOT NULL THEN
        PREPARE stmt FROM @query;
        EXECUTE stmt USING @name, @device_type_id, @device_group_id, @configuration_id;
        DEALLOCATE PREPARE stmt;
        COMMIT;
        CALL get_devices(NULL, p_name, p_device_type_id, p_device_group_id, p_configuration_id, NULL);
    END IF;
END
$$
DELIMITER ;

GRANT EXECUTE ON PROCEDURE get_devices TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE add_device TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE delete_device TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE update_device TO 'iot-device-manager-client'@'%';
