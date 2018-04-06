package web.service;

import org.springframework.web.multipart.MultipartFile;
import web.domain.entity.Device;
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
     * Add icon for type
     *
     * @param id
     *      Device type ID used as filter
     * @param icon
     *      Icon to add as {@link MultipartFile}
     * @param name
     *      Name for new icon
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addIconForType(Integer id, MultipartFile icon, String name);

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

    /**
     * Get device type's devices
     * @param id
     *      Device type ID used as filter
     * @param deviceGroupId
     *      Device group ID used as filter
     * @param configurationId
     *      Configuration ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getTypesDevices(Integer id, Integer deviceGroupId, Integer configurationId);

    /**
     * Add device with given type
     *
     * @param id
     *      Device type ID used as filter
     * @param device
     *      Device to add with given type
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addDeviceWithType(Integer id, Device device);
}
