# During development:
DROP DATABASE iotdevicemanager;

CREATE DATABASE IF NOT EXISTS iotdevicemanager;
USE iotdevicemanager;

/** TABLES **/

CREATE TABLE IF NOT EXISTS configuration (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(100),
    json_configuration MEDIUMBLOB
);

CREATE TABLE IF NOT EXISTS device_group (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS device_icon (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    path VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS device_type (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    device_icon_id INT,
    FOREIGN KEY (device_icon_id) REFERENCES device_icon(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS device (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    device_type_id INT NOT NULL,
    device_group_id INT,
    configuration_id INT,
    authentication_key VARCHAR(32) UNIQUE NOT NULL,
    FOREIGN KEY (device_type_id) REFERENCES device_type(id),
    FOREIGN KEY (device_group_id) REFERENCES device_group(id) ON DELETE SET NULL,
    FOREIGN KEY (configuration_id) REFERENCES configuration(id) ON DELETE SET NULL,
    INDEX(authentication_key)
);

CREATE TABLE IF NOT EXISTS location (
    device_id INT NOT NULL,
    coordinates VARCHAR(20) NOT NULL,
    time DATETIME NOT NULL,
    FOREIGN KEY (device_id) REFERENCES device(id) ON DELETE CASCADE
);

/** TRIGGERS **/

/* Add authentication key when inserting new device, Key is md5 of id. */
DROP TRIGGER IF EXISTS device_insert;
DELIMITER $$
CREATE TRIGGER device_insert
    BEFORE INSERT ON device
    FOR EACH ROW
BEGIN
    /* Selectin max id is safer than LAST_INSERT_ID() */
    SET @id = (SELECT MAX(id) FROM device) + 1;

    IF @id IS NULL THEN
        SET @id = 0;
    END IF;

    SET NEW.authentication_key = MD5(@id);
END$$
DELIMITER ;

/** PROCEDURES **/

/** DEVICE **/

/* Get devices using passed parameters as "AND clause"-filter. NULL parameters are ignored. */
DROP PROCEDURE IF EXISTS get_devices;
DELIMITER $$
CREATE PROCEDURE get_devices (
    f_name VARCHAR(50),
    f_device_type_id INT,
    f_device_group_id INT,
    f_configuration_id INT,
    f_authentication_key VARCHAR(32))
BEGIN
    SET @query = "SELECT id, name, device_type_id, device_group_id, configuration_id, authentication_key FROM device";
    SET @where_clause = " WHERE 1=1";

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

/* Add device with given attributes. Returns inserted device including id and generated authentication key */
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

    SELECT id, name, device_type_id, device_group_id, configuration_id, authentication_key 
        FROM device WHERE id=LAST_INSERT_ID();
END
$$
DELIMITER ;

/* Delete device matching given parameters. Returns 1 for succes and NULL for failure.  */
DROP PROCEDURE IF EXISTS delete_device;
DELIMITER $$
CREATE PROCEDURE delete_device (
    IN p_name VARCHAR(50),
    IN p_authenticationKey VARCHAR(32))
BEGIN
    SET @query = "DELETE FROM device WHERE 1=1 ";

    /* Indicates whether required parameters were found */
    SET @params_ok = NULL;

    IF p_name IS NOT NULL THEN
        SET @query = CONCAT(@query, 'AND name ="', p_name, '"');
        SET @params_ok = 1;
    END IF;
    
    IF p_authenticationKey IS NOT NULL THEN
        SET @query = CONCAT(@query, 'AND authenticationKey ="', p_authenticationKey, '"');
        SET @params_ok = 1;
    END IF;
    
    IF @params_ok IS NOT NULL THEN
        PREPARE stmt FROM @query;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;

    SELECT @params_ok;
END
$$
DELIMITER ;

/* Update device using passed p_* prefixed parameters. f_* prefixed parameters are used to filter device selection. */
/* NOTE: NULL attribute sets column value NULL. */
DROP PROCEDURE IF EXISTS update_device;
DELIMITER $$
CREATE PROCEDURE update_device (
    f_name VARCHAR(50),
    f_authentication_key VARCHAR(32),
    p_name VARCHAR(50),
    p_device_type_id INT,
    p_device_group_id INT,
    p_configuration_id INT)
BEGIN
    SET @query = "UPDATE device SET
        name = ?,
        device_type_id = ?,
        device_group_id = ?,
        configuration_id = ?";

    SET @where_clause = " WHERE 1=1";

    /* Indicates whether required parameters were found */
    SET @params_ok = NULL;

    SET @name = p_name;
    SET @device_type_id = p_device_type_id;
    SET @device_group_id = p_device_group_id;
    SET @configuration_id = p_configuration_id;

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
        CALL get_devices(p_name, p_device_type_id, p_device_group_id, p_configuration_id, NULL);
    END IF;
END
$$
DELIMITER ;

/* DEVICE ICON */

/* Get device icons using passed path parameter as filter. NULL parameter is ignored. */
DROP PROCEDURE IF EXISTS get_device_icons;
DELIMITER $$
CREATE PROCEDURE get_device_icons (
    f_path VARCHAR(100))
BEGIN
    SET @query = "SELECT id, path FROM device_icon";
    SET @where_clause = " WHERE 1=1";

    IF f_path IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND path="', f_path, '"');
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END
$$
DELIMITER ;


/* Add device icon with given attributes. Returns inserted device icon */
DROP PROCEDURE IF EXISTS add_device_icon;
DELIMITER $$
CREATE PROCEDURE add_device_icon (
    IN p_path VARCHAR(100))
BEGIN
    INSERT INTO device_icon(path) VALUES (p_path);

    SELECT id, path FROM device_icon WHERE id=LAST_INSERT_ID();
END
$$
DELIMITER ;


/* TODO environment credentials */
DROP USER IF EXISTS 'iot-device-manager-client'@'%';
CREATE USER 'iot-device-manager-client'@'%' identified by 'client';

GRANT SELECT ON mysql.proc TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE get_devices TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE add_device TO 'iot-device-manager-client'@'%';
