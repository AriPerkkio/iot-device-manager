package web.service;

import web.domain.entity.DeviceGroup;
import web.domain.response.ResponseWrapper;

public interface DeviceGroupService {

    /**
     * Get device groups matching given parameters
     *
     * @param id
     *      Device group ID used as filter
     * @param name
     *      Device group name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDeviceGroups(Integer id, String name);

    /**
     * Add device group to the database
     *
     * @param deviceGroup
     *      Device group to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addDeviceGroup(DeviceGroup deviceGroup);

    /**
     * Update device group matching given filter parameters. Item in database is replaced using given DeviceGroup object.
     * At least one filter is required.
     *
     * @param id
     *      Device group ID used as filter
     * @param name
     *      Device group name used as filter
     * @param deviceGroup
     *      Device group used to replace existing one in database
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper updateDeviceGroup(Integer id, String name, DeviceGroup deviceGroup);

    /**
     * Delete device group matching given parameters. At least one parameter is required.
     *
     * @param id
     *      Device group ID used as filter
     * @param name
     *      Device group name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDeviceGroup(Integer id, String name);
}
