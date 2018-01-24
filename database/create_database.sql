# During development:
DROP DATABASE iotdevicemanager;

CREATE DATABASE IF NOT EXISTS iotdevicemanager;
USE iotdevicemanager;

/** TABLES **/

CREATE TABLE IF NOT EXISTS configuration (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    json_configuration MEDIUMBLOB
);

CREATE TABLE IF NOT EXISTS device_group (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS device_icon (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    path VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS device_type (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    device_icon_id INT,
    FOREIGN KEY (device_icon_id) REFERENCES device_icon(id)
);

CREATE TABLE IF NOT EXISTS device (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    device_type_id INT NOT NULL,
    device_group_id INT,
    configuration_id INT,
    authentication_key VARCHAR(32) UNIQUE NOT NULL,
    FOREIGN KEY (device_type_id) REFERENCES device_type(id),
    FOREIGN KEY (device_group_id) REFERENCES device_group(id),
    FOREIGN KEY (configuration_id) REFERENCES configuration(id),
    INDEX(authentication_key)
);

CREATE TABLE IF NOT EXISTS location (
    device_id INT NOT NULL,
    coordinates VARCHAR(20) NOT NULL,
    time DATETIME NOT NULL,
    FOREIGN KEY (device_id) REFERENCES device(id)
);

/** TRIGGERS **/

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


DROP PROCEDURE IF EXISTS add_device;
DELIMITER $$
CREATE PROCEDURE add_device (
    IN p_name VARCHAR(50),
    IN p_device_type_id INT,
    IN p_device_group_id INT,
    IN p_configuration_id INT)
BEGIN

    IF p_device_group_id = 0 THEN
        SET p_device_group_id = NULL;
    END IF;

    IF p_configuration_id = 0 THEN
        SET p_configuration_id = NULL;
    END IF;

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