package web.service;

import javassist.NotFoundException;
import web.domain.entity.*;
import web.domain.response.ResponseWrapper;

import java.util.Date;

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

    /**
     * Add group for device
     *
     * @param id
     *      Device ID used as filter
     * @param deviceGroup
     *      Device group to add for given device
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addGroupForDevice(Integer id, DeviceGroup deviceGroup);

    /**
     * Update device's group
     *
     * @param id
     *      Device ID used as filter
     * @param deviceGroup
     *      Device group used to replace device's current group
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper updateDevicesGroup(Integer id, DeviceGroup deviceGroup);


    /**
     * Delete device's group
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDevicesGroup(Integer id);

    /**
     * Get device's type
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDevicesType(Integer id);

    /**
     * Add type for Device
     *
     * @param id
     *      Device ID used as filter
     * @param deviceType
     *      Device type to add for given device
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addTypeForDevice(Integer id, DeviceType deviceType);

    /**
     * Update device's type
     *
     * @param id
     *      Device ID used as filter
     * @param deviceType
     *      Device type used to replace device's current type
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper updateDevicesType(Integer id, DeviceType deviceType);

    /**
     * Delete device's type
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDevicesType(Integer id);

    /**
     * Get device's icon.
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDevicesIconInformation(Integer id);

    /**
     * Get device's icon.
     *
     * @param id
     *      Device ID used as filter
     * @param deviceIcon
     *      Device icon used to replace existing one in database
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper renameDevicesIcon(Integer id, DeviceIcon deviceIcon);

    /**
     * Delete device's icon.
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDevicesIcon(Integer id);

    /**
     * Get device's configuration
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDevicesConfiguration(Integer id);

    /**
     * Add configuration for device
     *
     * @param id
     *      Device ID used as filter
     *
     * @param configuration
     *      Configuration to add for given device
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addConfigurationForDevice(Integer id, Configuration configuration);

    /**
     * Update device's configuration
     *
     * @param id
     *      Device ID used as filter
     * @param configuration
     *      Configuration used to replace existing one
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper updateDevicesConfiguration(Integer id, Configuration configuration);

    /**
     * Delete device's configuration
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDevicesConfiguration(Integer id);

    /**
     * Get device's measurements
     *
     * @param id
     *      Device ID used as filter
     * @param exactTime
     *      Time used as filter. Search measurement updates which match timestamp
     * @param startTime
     *      Start time used as filter. Search measurement updates which occurred after this time.
     * @param endTime
     *      End time used as filter. Search measurement updates which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDevicesMeasurements(Integer id, Date exactTime, Date startTime, Date endTime);

    /**
     * Add measurement for device
     *
     * @param id
     *      Device ID used as filter
     * @param measurement
     *      Measurement to add for given device
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addMeasurementForDevice(Integer id, Measurement measurement);

    /**
     * Delete device's measurements
     *
     * @param id
     *      Device ID used as filter
     * @param exactTime
     *      Time used as filter. Search measurement updates which match timestamp
     * @param startTime
     *      Start time used as filter. Search measurement updates which occurred after this time.
     * @param endTime
     *      End time used as filter. Search measurement updates which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDevicesMeasurements(Integer id, Date exactTime, Date startTime, Date endTime);

    /**
     * Get device's location updates
     *
     * @param id
     *      Device ID used as filter
     * @param exactTime
     *      Time used as filter. Search location updates which match timestamp
     * @param startTime
     *      Start time used as filter. Search location updates which occurred after this time.
     * @param endTime
     *      End time used as filter. Search location updates which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getDevicesLocations(Integer id, Date exactTime, Date startTime, Date endTime);

    /**
     * Add location update for device
     *
     * @param id
     *      Device ID used as filter
     * @param location
     *      Location to add for given device
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addLocationForDevice(Integer id, Location location);

    /**
     * Delete device's locations updates
     *
     * @param id
     *      Device ID used as filter
     * @param exactTime
     *      Time used as filter. Search location updates which match timestamp
     * @param startTime
     *      Start time used as filter. Search location updates which occurred after this time.
     * @param endTime
     *      End time used as filter. Search location updates which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteDevicesLocations(Integer id, Date exactTime, Date startTime, Date endTime);

    /**
     * Validate a device matching given parameters exists
     *
     * @param id
     *      Device ID used as filter
     * @param name
     *      Device name used as filter
     * @param authenticationKey
     *      Device authentication key used as filter
     * @throws NotFoundException
     *      Exception thrown when device matching given parameters was not found
     */
    void validateDeviceExists(Integer id, String name, String authenticationKey) throws NotFoundException;
}
