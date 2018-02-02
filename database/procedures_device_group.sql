/***** DEVICE GROUP *****/

USE iotdevicemanager;

DROP PROCEDURE IF EXISTS get_device_groups;
DELIMITER $$
CREATE PROCEDURE get_device_groups (
    IN f_id INT,
    IN f_name VARCHAR(50))
BEGIN
    SET @query = "SELECT id, name, description
        FROM device_group";

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

DROP PROCEDURE IF EXISTS add_device_group;
DELIMITER $$
CREATE PROCEDURE add_device_group (
    IN p_name VARCHAR(50),
    IN p_description VARCHAR(100))
BEGIN
    INSERT INTO device_group(
        name,
        description
    )
    VALUES (
        p_name,
        p_description
    );

    COMMIT;
    CALL get_device_groups(NULL, p_name);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS delete_device_group;
DELIMITER $$
CREATE PROCEDURE delete_device_group (
    IN f_id INT,
    IN f_name VARCHAR(50))
BEGIN
    SET @query = "DELETE FROM device_group";
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

DROP PROCEDURE IF EXISTS update_device_group;
DELIMITER $$
CREATE PROCEDURE update_device_group (
    IN f_id INT,
    IN f_name VARCHAR(50),
    IN p_name VARCHAR(50),
    IN p_description VARCHAR(100))
BEGIN
    SET @query = "UPDATE device_group SET
        name = ?,
        description = ?";
    SET @where_clause = " WHERE 1=1";
    SET @params_ok = NULL;
    SET @name = p_name;
    SET @description = p_description;

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
        EXECUTE stmt USING @name, @description;
        DEALLOCATE PREPARE stmt;
        COMMIT;
        CALL get_device_groups(NULL, p_name);
    END IF;
END
$$
DELIMITER ;

GRANT EXECUTE ON PROCEDURE get_device_groups TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE update_device_group TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE delete_device_group TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE add_device_group TO 'iot-device-manager-client'@'%';
