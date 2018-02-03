*** Settings ***
Resource    resources.robot

*** Variables ***
${device_name}          robot-device
${device_name renamed}  robot-device-renamed
${device_type_id}       1
${device_group_id}      1
${configuration_id}     1

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

Verify Device Stored Procedures Work As Expected
    Setup Connection

    # Make sure test devices are not in database when starting
    Delete Device  NULL  "${device_name}"  NULL
    Delete Device  NULL  "${device_name renamed}"  NULL

    Log    Verify add_device returns newly inserted device
    ${add_device result} =    Add Device  "${device_name}"  ${device_type_id}  ${device_group_id}  ${configuration_id}
    Dictionary Should Contain Item  ${add_device result}  name              ${device_name}
    Dictionary Should Contain Item  ${add_device result}  device_type_id    ${device_type_id}
    Dictionary Should Contain Item  ${add_device result}  device_group_id   ${device_group_id}
    Dictionary Should Contain Item  ${add_device result}  configuration_id  ${configuration_id}

    Log    Verify update_device returns updated device
    ${update_device result} =    Update Device    NULL  "${device_name}"  NULL  "${device_name renamed}"  ${device_type_id}  ${device_group_id}  ${configuration_id}
    Dictionary Should Contain Item  ${update_device result}  name              ${device_name renamed}
    Dictionary Should Contain Item  ${update_device result}  device_type_id    ${device_type_id}
    Dictionary Should Contain Item  ${update_device result}  device_group_id   ${device_group_id}
    Dictionary Should Contain Item  ${update_device result}  configuration_id  ${configuration_id}

    Log    Verify get_devices finds inserted device
    ${get_devices result} =    Get Device  NULL  "${device_name renamed}"  ${device_type_id}  ${device_group_id}  ${configuration_id}  NULL
    Dictionary Should Contain Item  ${get_devices result}  name              ${device_name renamed}
    Dictionary Should Contain Item  ${get_devices result}  device_type_id    ${device_type_id}
    Dictionary Should Contain Item  ${get_devices result}  device_group_id   ${device_group_id}
    Dictionary Should Contain Item  ${get_devices result}  configuration_id  ${configuration_id}

    Log    Verify device is not found after delete_device
    Delete Device  NULL  "${device_name renamed}"  NULL
    ${get_devices result} =    Get Device  NULL  "${device_name renamed}"  ${device_type_id}  ${device_group_id}  ${configuration_id}  NULL
    Should Be Equal  ${get_devices result}  ${None}

    Terminate Connection
