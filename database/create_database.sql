# During development:
DROP DATABASE iotdevicemanager;

CREATE DATABASE IF NOT EXISTS iotdevicemanager;
USE iotdevicemanager;

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
    path VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS device_type (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    device_icon_id INT,
    FOREIGN KEY (device_icon_id) REFERENCES device_icon(id)
);

CREATE TABLE IF NOT EXISTS device (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    device_type_id INT NOT NULL,
    device_group_id INT,
    configuration_id INT,
    authentication_key VARCHAR(32) NOT NULL,
    FOREIGN KEY (device_type_id) REFERENCES device_type(id),
    FOREIGN KEY (device_group_id) REFERENCES device_group(id),
    FOREIGN KEY (configuration_id) REFERENCES configuration(id)
);

CREATE TABLE IF NOT EXISTS location (
    device_id INT NOT NULL,
    coordinates VARCHAR(20) NOT NULL,
    time DATETIME NOT NULL,
    FOREIGN KEY (device_id) REFERENCES device(id)
);

DROP TRIGGER IF EXISTS device_insert;
DELIMITER $$
CREATE TRIGGER device_insert
    BEFORE INSERT ON device
    FOR EACH ROW
BEGIN
    SET NEW.authentication_key = MD5(LAST_INSERT_ID());
END$$
DELIMITER ;
