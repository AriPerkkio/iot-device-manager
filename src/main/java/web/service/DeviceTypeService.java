package web.service;

import web.domain.entity.DeviceIcon;
import web.domain.entity.DeviceType;
import web.domain.response.ResponseWrapper;

public interface DeviceTypeService {
    /**
     * Get device types matching given parameters
     *
     * @param id
     *      Device type ID used as filter
     * @param name
     *      Device type name used as filter
     * @param deviceIconId
     *      Device icon ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDeviceTypes(Integer id, String name, Integer deviceIconId);

    /**
     * Add device type to the database
     *
     * @param deviceType
     *      Device type to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addDeviceType(DeviceType deviceType);

    /**
     * Update device type matching given filter parameters. Item in database is replaced using given DeviceType object.
     * At least one filter is required.
     *
     * @param id
     *      Device type ID used as filter
     * @param name
     *      Device type name used as filter
     * @param deviceType
     *      Device type used to replace existing one in database
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper updateDeviceType(Integer id, String name, DeviceType deviceType);

    /**
     * Delete device type matching given parameters. At least one parameter is required.
     *
     * @param id
     *      Device type ID used as filter
     * @param name
     *      Device type name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDeviceType(Integer id, String name);

    /**
     * Get device type's icon
     *
     * @param id
     *      Device type ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getTypesIcon(Integer id);

    /**
     * Rename device type's icon
     *
     * @param id
     *      Device type ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper renameTypesIcon(Integer id, DeviceIcon deviceIcon);

    /**
     * Delete device type's icon
     *
     * @param id
     *      Device type ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteTypesIcon(Integer id);
}
