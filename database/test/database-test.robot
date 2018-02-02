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
${device_name}         robot-device
${device_type_id}      1
${device_group_id}     1
${configuration_id}    1

*** Keywords ***
Setup Connection
    Log    Setting up connection
    Connect to Database    pymysql    iotdevicemanager    ${Username}    ${Password}    ${DatabaseHost}    ${Port}

Terminate Connection
    Log    Terminating connection
    Disconnect From Database

Initialize Tables
    Query    CALL delete_device(NULL, "${device_name}", NULL)

# ref: https://groups.google.com/forum/#!topic/robotframework-users/BeF_kJA2ELo
Get Values of Column
    [Arguments]    ${column}=0    @{records}    # Extract column values from from records and return as a list
    Log Many    @{records}
    @{column_value_list}=    Create List
    : FOR    ${value}    IN    @{records}
    \    Append To List    ${column_value_list}    ${value[${column}]}
    [Return]    @{column_value_list}

*** Test Cases ***
Verify Tables Exist
    Setup Connection
    Table Must Exist    device
    Table Must Exist    device_group
    Table Must Exist    device_type
    Table Must Exist    device_icon
    Table Must Exist    configuration
    Table Must Exist    location
    Terminate Connection

Verify Procedures Exist
    Setup Connection
    @{QueryResults}    Query    ${PROCEDURE CHECK QUERY}
    @{results}=    Get Values of Column    0    @{QueryResults}
    Should Contain X Times    ${results}    add_device              1
    Should Contain X Times    ${results}    add_configuration       1
    Should Contain X Times    ${results}    add_device              1
    Should Contain X Times    ${results}    add_device_group        1
    Should Contain X Times    ${results}    add_device_icon         1
    Should Contain X Times    ${results}    add_device_type         1
    Should Contain X Times    ${results}    add_location            1
    Should Contain X Times    ${results}    delete_configuration    1
    Should Contain X Times    ${results}    delete_device           1
    Should Contain X Times    ${results}    delete_device_group     1
    Should Contain X Times    ${results}    delete_device_icon      1
    Should Contain X Times    ${results}    delete_device_type      1
    Should Contain X Times    ${results}    delete_locations        1
    Should Contain X Times    ${results}    get_configurations      1
    Should Contain X Times    ${results}    get_devices             1
    Should Contain X Times    ${results}    get_device_groups       1
    Should Contain X Times    ${results}    get_device_icons        1
    Should Contain X Times    ${results}    get_device_types        1
    Should Contain X Times    ${results}    get_locations           1
    Should Contain X Times    ${results}    update_configuration    1
    Should Contain X Times    ${results}    update_device           1
    Should Contain X Times    ${results}    update_device_group     1
    Should Contain X Times    ${results}    update_device_icon      1
    Should Contain X Times    ${results}    update_device_type      1
    Terminate Connection

Verify Device Stored Procedures
    Setup Connection
    Initialize Tables
    
    Log    Verify add_device returns newly inserted device
    @{QueryResults} =    Query    CALL add_device("${device_name}", "${device_type_id}", "${device_group_id}", "${configuration_id}")
    @{results}=    Get Values of Column    1    @{QueryResults}
    Should Contain X Times    ${results}    ${device_name}    1

    Log    Verify get_devices finds inserted device
    @{QueryResults} =    Query    CALL get_devices(NULL, "${device_name}", NULL, NULL, NULL, NULL)
    @{results}=    Get Values of Column    1    @{QueryResults}
    Should Contain X Times    ${results}    ${device_name}    1

    Terminate Connection
