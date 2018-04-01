package web.service;

import web.domain.entity.Device;
import web.domain.response.ResponseWrapper;

public interface DeviceService {

    /**
     * Get devices matching given parameters
     *
     * @param id
     *      Device ID used as filter
     * @param name
     *      Device name used as filter
     * @param deviceTypeId
     *      Device type ID used as filter
     * @param deviceGroupId
     *      Device group type ID used as filter
     * @param configurationId
     *      Configuration ID used as filter
     * @param authenticationKey
     *      Authentication key used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDevices(Integer id, String name, Integer deviceTypeId, Integer deviceGroupId,
                               Integer configurationId, String authenticationKey);

    /**
     * Add device to the database
     *
     * @param device
     *      Device to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addDevice(Device device);

    /**
     * Update device matching given filter parameters. Item in database is replaced using given Device object.
     * At least one filter is required.
     *
     * @param id
     *      Device ID used as filter
     * @param name
     *      Device name used as filter
     * @param authenticationKey
     *      Device authentication key used as filter
     * @param device
     *      Device used to replace existing one in database
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper updateDevice(Integer id, String name, String authenticationKey, Device device);

    /**
     * Delete device matching given parameters. At least one parameter is required.
     *
     * @param id
     *      Device ID used as filter
     * @param name
     *      Device name used as filter
     * @param authenticationKey
     *      Device authentication key used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDevice(Integer id, String name, String authenticationKey);

    /**
     * Get device's group
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDevicesGroup(Integer id);
}
