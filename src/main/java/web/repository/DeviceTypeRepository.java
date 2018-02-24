package web.repository;

import web.domain.entity.DeviceType;

import java.util.Collection;

public interface DeviceTypeRepository {

    /**
     * Get device types matching given parameters. Parameters are optional and ignored when null value passed.
     *
     * @param id
     *          Device type ID used as filter
     * @param name
     *          Device type name used as filter
     * @param deviceIconId
     *          Device icon ID used as filter
     * @return
     *          Device types matching given parameters
     */
    Collection<DeviceType> getDeviceTypes(Integer id, String name, Integer deviceIconId);

    /**
     * Add given device type to the database
     *
     * @param deviceType
     *          Device type to add
     * @return
     *          Inserted device type including generated ID
     */
    DeviceType addDeviceType(DeviceType deviceType);


    /**
     * Update device type matching given filter parameters. Item in database is replaced using given Device Type object.
     * At least one filter is required. Update operation is ignored when no filters passed.
     *
     * @param id
     *          Device type ID used as filter
     * @param name
     *          Device type name used as filter
     * @param deviceType
     *          Device type used to replace existing one. All the attributes are updated - even NULL ones.
     * @return
     *          Device type with updated attributes.
     */
    DeviceType updateDeviceType(Integer id, String name, DeviceType deviceType);

    /**
     * Delete device type matching given parameters
     *
     * @param id
     *          Device type ID used as filter
     * @param name
     *          Device type name used as filter
     * @return
     *          True when operation is successful. False when operation failed.
     */
    Boolean deleteDeviceType(Integer id, String name);
}
