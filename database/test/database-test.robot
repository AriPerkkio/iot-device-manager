*** Settings ***
Resource    resources.robot

*** Variables ***
# Device
${device_name}          robot-device
${device_name renamed}  robot-device-renamed
${device_type_id}       1
${device_group_id}      1
${configuration_id}     1

# Device Group
${device_group_name}           robot-group
${device_group_name renamed}   robot-group-renamed
${device_group_description}    Group created by tests

# Devie Type
${device_type_name}             robot-type
${device_type_name renamed}     robot-type-renamed

# Device Icon
${device_icon_path}           /icons/robot-icon.png
${device_icon_path renamed}   /icons/robot-renamed-icon.png

# Configuration
${configuration_name}           robot-configuration
${configuration_name renamed}   robot-configuration-renamed
${configuration_description}    Configuration created by tests
${json_configuration}           {IS_TEST: TRUE}

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

    # Make sure test devices are not in database already
    Delete Device  NULL  "${device_name}"  NULL
    Delete Device  NULL  "${device_name renamed}"  NULL

    Log    Verify add_device returns inserted device
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

    Log    Verify get_devices finds device
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

Verify Configuration Stored Procedures Work As Expected
    Setup Connection

    # Make sure test configurations are not in database already
    Delete Configuration  NULL  "${configuration_name}"
    Delete Configuration  NULL  "${configuration_name renamed}"

    Log    Verify add_configuration returns inserted configuration
    ${add_configuration result} =    Add Configuration  "${configuration_name}"  "${configuration_description}"  "${json_configuration}"
    Dictionary Should Contain Item  ${add_configuration result}  name                ${configuration_name}
    Dictionary Should Contain Item  ${add_configuration result}  description         ${configuration_description}
    Dictionary Should Contain Item  ${add_configuration result}  json_configuration  ${json_configuration}

    Log    Verify update_configuration returns updated configuration
    ${updated_configuration result} =    Update Configuration  NULL  "${configuration_name}"  "${configuration_name renamed}"  "${configuration_description}"  "${json_configuration}"
    Dictionary Should Contain Item  ${updated_configuration result}  name                ${configuration_name renamed}
    Dictionary Should Contain Item  ${updated_configuration result}  description         ${configuration_description}
    Dictionary Should Contain Item  ${updated_configuration result}  json_configuration  ${json_configuration}

    Log    Verify get_configurations finds configuration
    ${get_configuration result} =    Get Configuration  NULL  "${configuration_name renamed}"
    Dictionary Should Contain Item  ${get_configuration result}  name                ${configuration_name renamed}
    Dictionary Should Contain Item  ${get_configuration result}  description         ${configuration_description}
    Dictionary Should Contain Item  ${get_configuration result}  json_configuration  ${json_configuration}

    Log    Verify configuration is not found after delete_configuration
    Delete Configuration  NULL  "${configuration_name renamed}"
    ${get_configuration result} =    Get Configuration  NULL  "${configuration_name renamed}"
    Should Be Equal    ${get_configuration result}    ${None}

Verify Device Group Stored Procedures Work As Expected
    Setup Connection

    # Make sure test groups are not in database already
    Delete Device Group  NULL  "${device_group_name}"
    Delete Device Group  NULL  "${device_group_name renamed}"

    Log    Verify add_device_group returns inserted device group
    ${add_device_group result} =    Add Device Group  "${device_group_name}"  "${device_group_description}"
    Dictionary Should Contain Item  ${add_device_group result}  name         ${device_group_name}
    Dictionary Should Contain Item  ${add_device_group result}  description  ${device_group_description}

    Log    Verify update_configuration returns updated configuration
    ${updated_device_group result} =  Update Device Group  NULL  "${device_group_name}"  "${device_group_name renamed}"  "${device_group_description}"
    Dictionary Should Contain Item  ${updated_device_group result}  name         ${device_group_name renamed}
    Dictionary Should Contain Item  ${updated_device_group result}  description  ${device_group_description}

    Log    Verify get_device_groups finds device group
    ${get_device_groups results} =    Get Device Group  NULL  "${device_group_name renamed}"
    Dictionary Should Contain Item  ${updated_device_group result}  name         ${device_group_name renamed}
    Dictionary Should Contain Item  ${updated_device_group result}  description  ${device_group_description}

    Log   Verify device group is not found after delete_device_group
    Delete Device Group  NULL  "${device_group_name renamed}"
    ${get_device_groups results} =    Get Device Group  NULL  "${device_group_name renamed}"
    Should Be Equal    ${get_device_groups results}    ${None}

Verify Device Type Stored Procedures Work As Expected
    Setup Connection

    # Make sure test types are not in database already
    Delete Device Type  NULL  "${device_type_name}"  NULL
    Delete Device Type  NULL  "${device_type_name renamed}"  NULL

    Log    Verify add_device_type returns inserted device type
    ${add_device_type result} =    Add Device Type  "${device_type_name}"  NULL
    Dictionary Should Contain Item  ${add_device_type result}  name            ${device_type_name}
    Dictionary Should Contain Key   ${add_device_type result}  device_icon_id

    Log    Verify update_device_type returns updated type
    ${updated_device_type result} =  Update Device Type  NULL  "${device_type_name}"  "${device_type_name renamed}"  NULL
    Dictionary Should Contain Item  ${updated_device_type result}  name            ${device_type_name renamed}
    Dictionary Should Contain Key   ${updated_device_type result}  device_icon_id

    Log    Verify get_device_types finds device type
    ${get_device_types results} =    Get Device Type  NULL  "${device_type_name renamed}"  NULL
    Dictionary Should Contain Item  ${updated_device_type result}  name            ${device_type_name renamed}
    Dictionary Should Contain Key   ${updated_device_type result}  device_icon_id

    Log   Verify device type is not found after delete_device_type
    Delete Device Type  NULL  "${device_type_name renamed}"  NULL
    ${get_device_types results} =    Get Device type  NULL  "${device_type_name renamed}"  NULL
    Should Be Equal    ${get_device_types results}    ${None}

Verify Device Icon Stored Procedures Work As Expected
    Setup Connection

    # Make sure test icons are not in database already
    Delete Device Icon  NULL  "${device_icon_path}"
    Delete Device Icon  NULL  "${device_icon_path renamed}"

    Log    Verify add_device_icon returns inserted device icon
    ${add_device_icon result} =    Add Device Icon  "${device_icon_path}"
    Dictionary Should Contain Item  ${add_device_icon result}  path  ${device_icon_path}

    Log    Verify update_device_icon returns updated icon
    ${updated_device_icon result} =  Update Device Icon  NULL  "${device_icon_path}"  "${device_icon_path renamed}"
    Dictionary Should Contain Item  ${updated_device_icon result}  path  ${device_icon_path renamed}

    Log    Verify get_device_icons finds device icon
    ${get_device_icons results} =    Get Device Icon  NULL  "${device_icon_path renamed}"
    Dictionary Should Contain Item  ${updated_device_icon result}  path  ${device_icon_path renamed}

    Log   Verify device icon is not found after delete_device_icon
    Delete Device Icon  NULL  "${device_icon_path renamed}"
    ${get_device_icons results} =    Get Device icon  NULL  "${device_icon_path renamed}"
    Should Be Equal    ${get_device_icons results}    ${None}

