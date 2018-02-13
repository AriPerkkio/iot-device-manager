*** Settings ***
Library     DatabaseLibrary
Library     Collections
Library     OperatingSystem

*** Variables ***
${Port}            3306
${Username}        root
${DatabaseHost}    127.0.0.1
${Password}        passu
@{QueryResults}
${PROCEDURE CHECK QUERY}    SELECT routine_name FROM information_schema.routines WHERE routine_type = "PROCEDURE" and routine_schema="iotdevicemanager"

*** Keywords ***
Setup Database
    # pymsql is unable to run SQL files which contain DELIMITER keyword
    # Work-around is to use OperatingSystem's Run
    Run    mysql -u ${Username} -p${Password} < ../create_database.sql
    Run    mysql -u ${Username} -p${Password} < ../procedures_configuration.sql
    Run    mysql -u ${Username} -p${Password} < ../procedures_device_group.sql
    Run    mysql -u ${Username} -p${Password} < ../procedures_device_icon.sql
    Run    mysql -u ${Username} -p${Password} < ../procedures_device.sql
    Run    mysql -u ${Username} -p${Password} < ../procedures_device_type.sql
    Run    mysql -u ${Username} -p${Password} < ../procedures_location.sql
    Run    mysql -u ${Username} -p${Password} < ../mockdata.sql

Teardown Database
    Setup Connection
    Execute Sql String    DROP DATABASE IF EXISTS iotdevicemanager;
    Terminate Connection

Setup Connection
    Log    Setting up connection
    Connect to Database    pymysql    iotdevicemanager    ${Username}    ${Password}    ${DatabaseHost}    ${Port}

Terminate Connection
    Log    Terminating connection
    Disconnect From Database

# ref: https://groups.google.com/forum/#!topic/robotframework-users/BeF_kJA2ELo
Get Values of Column
    [Arguments]    ${column}=0    @{records}    # Extract column values from from records and return as a list
    Log Many    @{records}
    @{column_value_list}=    Create List
    : FOR    ${value}    IN    @{records}
    \    Append To List    ${column_value_list}    ${value[${column}]}
    [Return]    @{column_value_list}

### DEVICE  ###

Map Result To Device
    [Arguments]    ${result}
    ${mapped device} =  Create Dictionary
    ...  id=${result[0]}
    ...  name=${result[1]}
    ...  device_type_id=${result[2]}
    ...  device_group_id=${result[3]}
    ...  configuration_id=${result[4]}
    ...  authentication_key=${result[5]}

    [Return]    ${mapped device}

Add Device
    [Arguments]    ${p_name}  ${p_device_type_id}  ${p_device_group_id}  ${p_configuration_id}
    @{QueryResults} =    Query    CALL add_device(${p_name}, ${p_device_type_id}, ${p_device_group_id}, ${p_configuration_id})
    ${added device} =    Map Result To Device    ${QueryResults[0]}
    [Return]    ${added device}

Get Device
    [Arguments]  ${f_id}  ${f_name}  ${f_device_type_id}  ${f_device_group_id}  ${f_configuration_id}  ${f_authentication_key}
    @{QueryResults} =    Query    CALL get_devices(${f_id}, ${f_name}, ${f_device_type_id}, ${f_device_group_id}, ${f_configuration_id}, ${f_authentication_key})
    ${length} =    Get Length  ${QueryResults}

    # Map results when resultset contains items
    ${fetched device} =  Run Keyword If  ${length} != 0
    ...  Map Result To Device    ${QueryResults[0]}

    [Return]    ${fetched device}

Update Device
    [Arguments]  ${f_id}  ${f_name}  ${f_authentication_key}  ${p_name}  ${p_device_type_id}  ${p_device_group_id}  ${p_configuration_id}
    @{QueryResults} =    Query    CALL update_device(${f_id}, ${f_name}, ${f_authentication_key}, ${p_name}, ${p_device_type_id}, ${p_device_group_id}, ${p_configuration_id})
    ${updated device} =    Map Result To Device    ${QueryResults[0]}
    [Return]    ${updated device}

Delete Device
    [Arguments]  ${f_id}  ${f_name}  ${f_authentication_key}
    @{QueryResults} =    Query    CALL delete_device(${f_id}, ${f_name}, ${f_authentication_key})
    [Return]    ${QueryResults[0][0]}

### CONFIGURATIONS ###

Map Result To Configuration
    [Arguments]    ${result}
    ${mapped configuration} =  Create Dictionary
    ...  id=${result[0]}
    ...  name=${result[1]}
    ...  description=${result[2]}
    ...  json_configuration=${result[3]}

    [Return]    ${mapped configuration}

Add Configuration
    [Arguments]  ${p_name}  ${p_description}  ${p_json_configuration}
    @{QueryResults} =    Query    CALL add_configuration(${p_name}, ${p_description}, ${p_json_configuration})
    ${added_configuration} =    Map Result To Configuration  ${QueryResults[0]}
    [Return]    ${added_configuration}

Get Configuration
    [Arguments]    ${f_id}  ${f_name}
    @{QueryResults} =    Query    CALL get_configurations(${f_id}, ${f_name})
    ${length} =    Get Length  ${QueryResults}

    # Map results when resultset contains items
    ${fetched_configuration} =  Run Keyword If  ${length} != 0
    ...  Map Result To Configuration    ${QueryResults[0]}

    [Return]    ${fetched_configuration}

Update Configuration
    [Arguments]    ${f_id}  ${f_name}  ${p_name}  ${p_description}  ${p_json_configuration}
    @{QueryResults} =    Query    CALL update_configuration(${f_id}, ${f_name}, ${p_name}, ${p_description}, ${p_json_configuration})
    ${updated_configuration} =    Map Result To Configuration  ${QueryResults[0]}
    [Return]    ${updated_configuration}

Delete Configuration
    [Arguments]    ${f_id}  ${f_name}
    @{QueryResults} =    Query    CALL delete_configuration(${f_id}, ${f_name})
    [Return]    ${QueryResults[0][0]}

### DEVICE GROUP ###

Map Result To Device Group
    [Arguments]    ${result}
    ${mapped device_group} =  Create Dictionary
    ...  id=${result[0]}
    ...  name=${result[1]}
    ...  description=${result[2]}
    [Return]    ${mapped device_group}

Add Device Group
    [Arguments]  ${p_name}  ${p_description}
    @{QueryResults} =    Query    CALL add_device_group(${p_name}, ${p_description})
    ${added_device_group} =    Map Result To Device Group  ${QueryResults[0]}
    [Return]    ${added_device_group}

Get Device Group
    [Arguments]    ${f_id}  ${f_name}
    @{QueryResults} =    Query    CALL get_device_groups(${f_id}, ${f_name})
    ${length} =    Get Length  ${QueryResults}

    # Map results when resultset contains items
    ${fetched_device_group} =  Run Keyword If  ${length} != 0
    ...  Map Result To Device Group    ${QueryResults[0]}

    [Return]    ${fetched_device_group}

Update Device Group
    [Arguments]    ${f_id}  ${f_name}  ${p_name}  ${p_description}
    @{QueryResults} =    Query    CALL update_device_group(${f_id}, ${f_name}, ${p_name}, ${p_description})
    ${updated_device_group} =    Map Result To Device Group  ${QueryResults[0]}
    [Return]    ${updated_device_group}

Delete Device Group
    [Arguments]    ${f_id}  ${f_name}
    @{QueryResults} =    Query    CALL delete_device_group(${f_id}, ${f_name})
    [Return]    ${QueryResults[0][0]}

### DEVICE TYPE ###

Map Result To Device Type
    [Arguments]    ${result}
    ${mapped device_type} =  Create Dictionary
    ...  id=${result[0]}
    ...  name=${result[1]}
    ...  device_icon_id=${result[2]}
    [Return]    ${mapped device_type}

Add Device Type
    [Arguments]  ${p_name}  ${p_device_icon_id}
    @{QueryResults} =    Query    CALL add_device_type(${p_name}, ${p_device_icon_id})
    ${added_device_type} =    Map Result To Device Type  ${QueryResults[0]}
    [Return]    ${added_device_type}

Get Device Type
    [Arguments]    ${f_id}  ${f_name}  ${f_device_icon_id}
    @{QueryResults} =    Query    CALL get_device_types(${f_id}, ${f_name}, ${f_device_icon_id})
    ${length} =    Get Length  ${QueryResults}

    # Map results when resultset contains items
    ${fetched_device_type} =  Run Keyword If  ${length} != 0
    ...  Map Result To Device Type    ${QueryResults[0]}

    [Return]    ${fetched_device_type}

Update Device Type
    [Arguments]    ${f_id}  ${f_name}  ${p_name}  ${p_device_icon_id}
    @{QueryResults} =    Query    CALL update_device_type(${f_id}, ${f_name}, ${p_name}, ${p_device_icon_id})
    ${updated_device_type} =    Map Result To Device Type  ${QueryResults[0]}
    [Return]    ${updated_device_type}

Delete Device Type
    [Arguments]    ${f_id}  ${f_name}  ${f_device_icon_id}
    @{QueryResults} =    Query    CALL delete_device_type(${f_id}, ${f_name}, ${f_device_icon_id})
    [Return]    ${QueryResults[0][0]}

### DEVICE ICON ###

Map Result To Device Icon
    [Arguments]    ${result}
    ${mapped device_icon} =  Create Dictionary
    ...  id=${result[0]}
    ...  name=${result[1]}
    [Return]    ${mapped device_icon}

Add Device Icon
    [Arguments]  ${p_name}
    @{QueryResults} =    Query    CALL add_device_icon(${p_name})
    ${added_device_icon} =    Map Result To Device Icon  ${QueryResults[0]}
    [Return]    ${added_device_icon}

Get Device Icon
    [Arguments]    ${f_id}  ${f_name}
    @{QueryResults} =    Query    CALL get_device_icons(${f_id}, ${f_name})
    ${length} =    Get Length  ${QueryResults}

    # Map results when resultset contains items
    ${fetched_device_icon} =  Run Keyword If  ${length} != 0
    ...  Map Result To Device Icon    ${QueryResults[0]}

    [Return]    ${fetched_device_icon}

Update Device Icon
    [Arguments]    ${f_id}  ${f_name}  ${p_name}
    @{QueryResults} =    Query    CALL update_device_icon(${f_id}, ${f_name}, ${p_name})
    ${updated_device_icon} =    Map Result To Device Icon  ${QueryResults[0]}
    [Return]    ${updated_device_icon}

Delete Device Icon
    [Arguments]    ${f_id}  ${f_name}
    @{QueryResults} =    Query    CALL delete_device_icon(${f_id}, ${f_name})
    [Return]    ${QueryResults[0][0]}

### LOCATION ###

Map Result To Location
    [Arguments]    ${result}
    ${mapped location} =  Create Dictionary
    ...  id=${result[0]}
    ...  coordinates=${result[1]}
    ...  time=${result[2]}
    [Return]    ${mapped location}

Add Location
    [Arguments]    ${p_device_id}  ${p_coordinates}  ${p_time}
    @{QueryResults} =    Query    CALL add_location(${p_device_id}, ${p_coordinates}, ${p_time})
    ${added_location} =    Map Result To Location  ${QueryResults[0]}
    [Return]    ${added_location}

Get Location
    [Arguments]    ${f_device_id}  ${f_exact_time}  ${f_start_time}  ${f_end_time}
    @{QueryResults} =    Query    CALL get_locations(${f_device_id}, ${f_exact_time}, ${f_start_time}, ${f_end_time})
    ${length} =    Get Length  ${QueryResults}

    # Map results when resultset contains items
    ${fetched_location} =  Run Keyword If  ${length} != 0
    ...  Map Result To Location    ${QueryResults[0]}

    [Return]    ${fetched_location}

Delete Location
    [Arguments]    ${f_device_id}  ${f_exact_time}  ${f_start_time}  ${f_end_time}
    @{QueryResults} =    Query    CALL delete_locations(${f_device_id}, ${f_exact_time}, ${f_start_time}, ${f_end_time})
    [Return]    ${QueryResults[0][0]}
