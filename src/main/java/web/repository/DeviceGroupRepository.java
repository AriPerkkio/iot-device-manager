package web.repository;

import web.domain.entity.DeviceGroup;

import java.util.Collection;

public interface DeviceGroupRepository {

    /**
     * Get device groups matching given parameters. Parameters are optional and ignored when null value passed.
     *
     * @param id
     *          Device Group ID used as filter
     * @param name
     *          Device Group name used as filter
     * @return
     *          Device Groups matching given parameters
     */
    Collection<DeviceGroup> getDeviceGroups(Integer id, String name);

    /**
     * Add given device group to the database
     *
     * @param deviceGroup
     *          Device Group to add
     * @return
     *          Inserted device group including generated ID
     */
    DeviceGroup addDeviceGroup(DeviceGroup deviceGroup);

    /**
     * Update device group matching given filter parameters. Item in database is replaced using given Device Group object.
     * At least one filter is required. Update operation is ignored when no filters passed.
     *
     * @param id
     *          Device Group ID used as filter
     * @param name
     *          Device Group name used as filter
     * @param deviceGroup
     *          Device Group used to replace existing one. All the attributes are updated - even NULL ones.
     * @return
     *          Device Group with updated attributes.
     */
    DeviceGroup updateDeviceGroup(Integer id, String name, DeviceGroup deviceGroup);

    /**
     * Delete device group matcing given parameters
     *
     * @param id
     *          Device Group ID used as filter
     * @param name
     *          Device Group name used as filter
     * @return
     *          True when operation is successful. False when operation failed.
     */
    Boolean deleteDeviceGroup(Integer id, String name);
}
