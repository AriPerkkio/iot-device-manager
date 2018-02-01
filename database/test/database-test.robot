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
Convert Column Values of Records to List
    [Arguments]    ${column}=0    @{records}    # Extract column values from from records and return as a list
    Log Many    @{records}
    @{column_value_list}=    Create List
    : FOR    ${value}    IN    @{records}
    \    Log    ${value}    level=DEBUG
    \    Append To List    ${column_value_list}    ${value[${column}]}
    Log Many    ${column_value_list}
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
    @{column_value_list}=    Convert Column Values of Records to List    0    @{QueryResults}
    Should Contain X Times    ${column_value_list}    add_device              1
    Should Contain X Times    ${column_value_list}    add_configuration       1
    Should Contain X Times    ${column_value_list}    add_device              1
    Should Contain X Times    ${column_value_list}    add_device_group        1
    Should Contain X Times    ${column_value_list}    add_device_icon         1
    Should Contain X Times    ${column_value_list}    add_device_type         1
    Should Contain X Times    ${column_value_list}    add_location            1
    Should Contain X Times    ${column_value_list}    delete_configuration    1
    Should Contain X Times    ${column_value_list}    delete_device           1
    Should Contain X Times    ${column_value_list}    delete_device_group     1
    Should Contain X Times    ${column_value_list}    delete_device_icon      1
    Should Contain X Times    ${column_value_list}    delete_device_type      1
    Should Contain X Times    ${column_value_list}    delete_locations        1
    Should Contain X Times    ${column_value_list}    get_configurations      1
    Should Contain X Times    ${column_value_list}    get_devices             1
    Should Contain X Times    ${column_value_list}    get_device_groups       1
    Should Contain X Times    ${column_value_list}    get_device_icons        1
    Should Contain X Times    ${column_value_list}    get_device_types        1
    Should Contain X Times    ${column_value_list}    get_locations           1
    Should Contain X Times    ${column_value_list}    update_configuration    1
    Should Contain X Times    ${column_value_list}    update_device           1
    Should Contain X Times    ${column_value_list}    update_device_group     1
    Should Contain X Times    ${column_value_list}    update_device_icon      1
    Should Contain X Times    ${column_value_list}    update_device_type      1
    Terminate Connection
