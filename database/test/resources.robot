*** Settings ***
Library     DatabaseLibrary
Library     Collections

*** Variables ***
${Port}            3306
${Username}        root
${DatabaseHost}    127.0.0.1
${Password}        passu
@{QueryResults}
${PROCEDURE CHECK QUERY}    SELECT routine_name FROM information_schema.routines WHERE routine_type = "PROCEDURE" and routine_schema="iotdevicemanager"

*** Keywords ***
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

Map Result To Device
    [Arguments]    ${result}
    ${mapped device} =  Create Dictionary  id=${result[0]}  name=${result[1]}  device_type_id=${result[2]}  device_group_id=${result[3]}  configuration_id=${result[4]}  authentication_key=${result[5]}
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