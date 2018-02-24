package web.repository;

import web.domain.entity.Device;

import java.util.Collection;

public interface DeviceRepository {

    /**
     * Get devices matching given parameters. Parameters are optional and ignored when null value passed.
     *
     * @param id
     *          Device ID used as filter
     * @param name
     *          Device name used as filter
     * @param deviceTypeId
     *          Device Type ID used as filter
     * @param deviceGroupId
     *          Device Group ID used as filter
     * @param configurationId
     *          Configuration ID used as filter
     * @param authenticationKey
     *          Authentication key used as filter
     * @return
     *          Devices matching given parameters
     */
    Collection<Device> getDevices(Integer id, String name, Integer deviceTypeId, Integer deviceGroupId,
                                  Integer configurationId, String authenticationKey);

    /**
     * Add given device to the database
     *
     * @param device
     *          Device to add
     * @return
     *          Inserted device including generated ID and authentication key
     */
    Device addDevice(Device device);

    /**
     * Update device matching given filter parameters. Item in database is replaced using given Device object.
     * At least one filter is required. Update operation is ignored when no filters passed.
     *
     * @param id
     *          Device ID used as filter
     * @param name
     *          Device name used as filter
     * @param authenticationKey
     *          Device authentication key used as filter.
     * @param device
     *          Device used to replace existing one. All the attributes are updated - even NULL ones.
     * @return
     *          Device with updated attributes.
     */
    Device updateDevice(Integer id, String name, String authenticationKey, Device device);

    /**
     * Delete device matching given parameters
     *
     * @param id
     *          Device ID used as filter
     * @param name
     *          Device name used as filter
     * @param authenticationKey
     *          Authentication key used as filter
     * @return
     *          True when operation is successful. False when operation failed.
     */
    Boolean deleteDevice(Integer id, String name, String authenticationKey);
}
