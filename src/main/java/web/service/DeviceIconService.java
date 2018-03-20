package web.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import web.domain.entity.DeviceIcon;
import web.domain.response.ResponseWrapper;

public interface DeviceIconService {

    /**
     * Get device icon as {@link Resource}. At least one filter is required.
     *
     * @param id
     *      Device icon ID used as filter
     * @param name
     *      Device icon name used as filter
     * @return
     *      Device icon resource when icon found. Null when no match found.
     */
    Resource getDeviceIcon(Integer id, String name);

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
     * Get device icons matching given parameters
     *
     * @param id
     *      Device icon ID used as filter
     * @param name
     *      Device icon name used as filter
     * @return
     *      ResponseWrapper containing payloar or errors
     */
    ResponseWrapper getDeviceIcons(Integer id, String name);

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
     *      True when delete is successful, false when delete failed
     */
    Boolean deleteDeviceIcon(Integer id, String name);
}
