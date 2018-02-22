package web.repository;

import web.domain.entity.DeviceIcon;

import java.util.Collection;

public interface DeviceIconRepository {

    /**
     * Get device icons matching given parameters. Parameters are optional and ignored when null value passed.
     *
     * @param id
     *          Device icon ID used as filter
     * @param name
     *          Device icon name used as filter
     * @return
     *          Device icons matching given parameters
     */
    Collection<DeviceIcon> getDeviceIcons(Integer id, String name);

    /**
     * Add given device icon to the database
     *
     * @param deviceIcon
     *          Device icon to add
     * @return
     *          Inserted device icon including generated ID
     */
    DeviceIcon addDeviceIcon(DeviceIcon deviceIcon);

    /**
     * Update device icon matching given filter parameters.
     * At least one filter is required. Update operation is ignored when no filters passed.
     *
     * @param id
     *          Device Icon ID used as filter
     * @param name
     *          Device Icon name used as filter
     * @param deviceIcon
     *          Device icon used to replace existing one.
     * @return
     *          Device Icon with updated attributes
     */
    DeviceIcon updateDeviceIcon(Integer id, String name, DeviceIcon deviceIcon);

    /**
     * Delete device icon matching given parameters
     *
     * @param id
     *          Device Icon ID used as filter
     * @param name
     *          Device Icon name used as filter
     * @return
     *          True when operation is successful. False when operation failed.
     */
    Boolean deleteDeviceIcon(Integer id, String name);
}
