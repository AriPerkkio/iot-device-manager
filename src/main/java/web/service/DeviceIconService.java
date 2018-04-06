package web.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import web.domain.entity.DeviceIcon;
import web.domain.entity.DeviceType;
import web.domain.response.ResponseWrapper;

public interface DeviceIconService {

    /**
     * Get device icons matching given parameters
     *
     * @param id
     *      Device icon ID used as filter
     * @param name
     *      Device icon name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDeviceIcons(Integer id, String name);

    /**
     * Get device icon as {@link Resource}
     *
     * @param name
     *      Device icon name used as filter
     * @return
     *      Device icon resource when icon found. Runtime exception is thrown otherwise
     */
    Resource getDeviceIconFile(String name);

    /**
     * Add device icon to the database.
     *
     * @param icon
     *      Icon to add as {@link MultipartFile}
     * @param name
     *      Name for new icon
     * @return
     *      ResponseWrapper containing payload or error
     */
    ResponseWrapper addDeviceIcon(MultipartFile icon, String name);

    /**
     * Update device icon matching given filter parameters. At least one parameter is required.
     *
     * @param id
     *      Device icon ID used as filter
     * @param name
     *      Device icon name used as filter
     * @param deviceIcon
     *      Device icon used to replace existing one
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper updateDeviceIcon(Integer id, String name, DeviceIcon deviceIcon);

    /**
     * Delete device icon matching given parameters. At least one parameter is required.
     *
     * @param id
     *      Device icon ID used as filter
     * @param name
     *      Device icon name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDeviceIcon(Integer id, String name);

    /**
     * Get icon's types
     *
     * @param id
     *      Device icon ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getIconsTypes(Integer id);

    /**
     * Add type with icon
     *
     * @param id
     *      Device icon ID used as filter
     * @param deviceType
     *      Device type to add with given icon
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addTypeWithIcon(Integer id, DeviceType deviceType);

}
