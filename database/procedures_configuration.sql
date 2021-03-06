/***** CONFIGURATION *****/

DROP PROCEDURE IF EXISTS get_configurations;
DELIMITER $$
CREATE PROCEDURE get_configurations (
    IN f_id INT,
    IN f_name VARCHAR(50))
BEGIN
    SET @query = "SELECT id, name, description, content
        FROM configuration";
    SET @where_clause = " WHERE 1=1";

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id = "', f_id, '"');
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name = "', f_name, '"');
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS add_configuration;
DELIMITER $$
CREATE PROCEDURE add_configuration (
    IN p_name VARCHAR(50),
    IN p_description VARCHAR(100),
    IN p_content JSON)
BEGIN
    INSERT INTO configuration(
        name,
        description,
        content
    )
    VALUES (
        p_name,
        p_description,
        p_content
    );

    CALL get_configurations(NULL, p_name);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS delete_configuration;
DELIMITER $$
CREATE PROCEDURE delete_configuration (
    IN f_id INT,
    IN f_name VARCHAR(50))
BEGIN
    SET @query = "DELETE FROM configuration";
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

DROP PROCEDURE IF EXISTS update_configuration;
DELIMITER $$
CREATE PROCEDURE update_configuration (
    IN f_id INT,
    IN f_name VARCHAR(50),
    IN p_name VARCHAR(50),
    IN p_description VARCHAR(100),
    IN p_content JSON)
BEGIN
    SET @query = "UPDATE configuration SET
        name = ?,
        description = ?,
        content = ?";
    SET @where_clause = " WHERE 1=1";
    SET @params_ok = NULL;

    SET @name = p_name;
    SET @description = p_description;
    SET @content = p_content;

    IF f_id IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND id = "', f_id, '"');
        SET @params_ok = 1;
    END IF;

    IF f_name IS NOT NULL THEN
        SET @where_clause = CONCAT(@where_clause, ' AND name = "', f_name, '"');
        SET @params_ok = 1;
    END IF;

    SET @query = CONCAT(@query, @where_clause);

    IF @params_ok IS NOT NULL THEN
        PREPARE stmt FROM @query;
        EXECUTE stmt USING @name, @description, @content;
        DEALLOCATE PREPARE stmt;
        CALL get_configurations(NULL, @name);
    END IF;
END
$$
DELIMITER ;

GRANT EXECUTE ON PROCEDURE get_configurations TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE update_configuration TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE delete_configuration TO 'iot-device-manager-client'@'%';
GRANT EXECUTE ON PROCEDURE add_configuration TO 'iot-device-manager-client'@'%';
