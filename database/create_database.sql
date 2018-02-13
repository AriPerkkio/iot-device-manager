CREATE DATABASE IF NOT EXISTS iotdevicemanager;
USE iotdevicemanager;

/***** TABLES *****/

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
    name VARCHAR(25) UNIQUE NOT NULL
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
    device_type_id INT,
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

/***** TRIGGERS *****/

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

    SET NEW.authentication_key = MD5(@id + NOW());
END$$
DELIMITER ;

/* TODO environment credentials */
DROP USER IF EXISTS 'iot-device-manager-client'@'%';
CREATE USER 'iot-device-manager-client'@'%' identified by 'client';

GRANT SELECT ON mysql.proc TO 'iot-device-manager-client'@'%';
