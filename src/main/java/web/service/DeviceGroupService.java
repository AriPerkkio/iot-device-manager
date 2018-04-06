package web.service;

import web.domain.entity.Device;
import web.domain.entity.DeviceGroup;
import web.domain.response.ResponseWrapper;

import java.util.Date;

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

    /**
     * Get group's devices
     *
     * @param id
     *      Device group ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getGroupsDevices(Integer id);

    /**
     * Add device to group
     *
     * @param id
     *      Device group ID used as filter
     * @param device
     *      Device to add into group
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addDeviceToGroup(Integer id, Device device);

    /**
     * Get group's measurements
     *
     * @param id
     *      Device group ID used as filter
     * @param exactTime
     *      Time used as filter. Search measurements which match timestamp
     * @param startTime
     *      Start time used as filter. Search measurements which occurred after this time.
     * @param endTime
     *      End time used as filter. Search measurements which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getGroupsMeasurements(Integer id, Date exactTime, Date startTime, Date endTime);

    /**
     * Delete group's measurements
     *
     * @param id
     *      Device group ID used as filter
     * @param exactTime
     *      Time used as filter. Search measurements which match timestamp
     * @param startTime
     *      Start time used as filter. Search measurements which occurred after this time.
     * @param endTime
     *      End time used as filter. Search measurements which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteGroupsMeasurements(Integer id, Date exactTime, Date startTime, Date endTime);

    /**
     * Get group's location updates
     *
     * @param id
     *      Device group ID used as filter
     * @param exactTime
     *      Time used as filter. Search location updates which match timestamp
     * @param startTime
     *      Start time used as filter. Search location updates which occurred after this time.
     * @param endTime
     *      End time used as filter. Search location updates which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getGroupsLocations(Integer id, Date exactTime, Date startTime, Date endTime);

    /**
     * Delete group's location updates
     *
     * @param id
     *      Device group ID used as filter
     * @param exactTime
     *      Time used as filter. Search location updates which match timestamp
     * @param startTime
     *      Start time used as filter. Search location updates which occurred after this time.
     * @param endTime
     *      End time used as filter. Search location updates which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteGroupsLocations(Integer id, Date exactTime, Date startTime, Date endTime);
}
