CREATE DATABASE IF NOT EXISTS iotdevicemanager;
USE iotdevicemanager;

CREATE TABLE IF NOT EXISTS device (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    device_type INT NOT NULL, # TODO Foreign keys
    group_id INT, # TODO Foreign keys
    configuration_id INT, # TODO Foreign keys
    authentication_key VARCHAR(32) NOT NULL
);

# Set authentication key for each device
DROP TRIGGER IF EXISTS device_insert;
DELIMITER $$
CREATE TRIGGER device_insert
    BEFORE INSERT ON device
    FOR EACH ROW
BEGIN
    SET NEW.authentication_key = MD5(LAST_INSERT_ID());
END$$
DELIMITER ;
