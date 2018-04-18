package web.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import web.domain.entity.*;
import web.domain.response.ResponseWrapper;
import web.service.DeviceService;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Date;

import static web.validators.FilterValidator.validateErrors;

@Validated
@RestController
@RequestMapping(value = "/api", produces = "application/vnd.collection+json; charset=utf-8")
public class DeviceController {

    private static final String URI = "/devices";
    private static final String ID_URI = URI + "/{id}";
    private static final String GROUP_URI = ID_URI + "/group";
    private static final String TYPE_URI = ID_URI + "/type";
    private static final String ICON_URI = ID_URI + "/icon";
    private static final String CONFIGURATION_URI = ID_URI + "/configuration";
    private static final String MEASUREMENTS_URI = ID_URI + "/measurements";
    private static final String LOCATIONS_URI = ID_URI + "/locations";
    private final DeviceService deviceService;

    DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Get devices matching given request parameters
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
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = URI, method = RequestMethod.GET)
    public ResponseWrapper getDevices(
            @RequestParam(name="id", required = false) Integer id,
            @Valid @Pattern(regexp = "[A-Za-z0-9_ .,-]{1,50}") @RequestParam(name="name", required = false) String name,
            @RequestParam(name="deviceTypeId", required = false) Integer deviceTypeId,
            @RequestParam(name="deviceGroupId", required = false) Integer deviceGroupId,
            @RequestParam(name="configurationId", required = false) Integer configurationId,
            @Valid @Pattern(regexp = "[A-Za-z0-9]{1,32}") @RequestParam(name="authenticationKey", required = false) String authenticationKey) {
        return deviceService.getDevices(id, name, deviceTypeId, deviceGroupId, configurationId, authenticationKey);
    }

    /**
     * Get device matching given ID
     *
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.GET)
    public ResponseWrapper getDevice(@PathVariable Integer id) {
        return deviceService.getDevices(id, null, null, null, null, null);
    }

    /**
     * Add device from request body to the database
     *
     * @param device
     *      Device to add
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = URI, method = RequestMethod.POST)
    public ResponseWrapper addDevice(@Valid @RequestBody Device device, Errors errors) {
        validateErrors(errors);

        return deviceService.addDevice(device);
    }

    /**
     * Update device matching given request parameters. Existing item in database is replaced using given request body
     * At least one parameter is required.
     *
     * @param id
     *      Device ID used as filter
     * @param name
     *      Device name used as filter
     * @param authenticationKey
     *      Authentication key used as filter
     * @param device
     *      Device used to replace existing one
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDevice(
            @RequestParam(name="id", required = false) Integer id,
            @Valid @Pattern(regexp = "[A-Za-z0-9_ .,-]{1,50}") @RequestParam(name="name", required = false) String name,
            @Valid @Pattern(regexp = "[A-Za-z0-9]{1,32}") @RequestParam(name="authenticationKey", required = false) String authenticationKey,
            @Valid @RequestBody Device device,
            Errors errors) {
        validateErrors(errors);

        return deviceService.updateDevice(id, name, authenticationKey, device);
    }

    /**
     * Update device matching given ID. Existing item in database is replaced using given request body
     *
     * @param device
     *      Device used to replace existing one
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDeviceById(
            @PathVariable Integer id,
            @Valid @RequestBody Device device,
            Errors errors) {
        validateErrors(errors);

        return deviceService.updateDevice(id, null, null, device);
    }

    /**
     * Delete device matching given request parameters. At least one parameter is required.
     *
     * @param id
     *      Device ID used as filter
     * @param name
     *      Device name used as filter
     * @param authenticationKey
     *      Authentication key used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDevice(
            @RequestParam(name="id", required = false) Integer id,
            @Valid @Pattern(regexp = "[A-Za-z0-9_ .,-]{1,50}") @RequestParam(name="name", required = false) String name,
            @Valid @Pattern(regexp = "[A-Za-z0-9]{1,32}") @RequestParam(name="authenticationKey", required = false) String authenticationKey) {
        return deviceService.deleteDevice(id, name, authenticationKey);
    }

    /**
     * Delete device matching given ID.
     *
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDeviceById(@PathVariable Integer id) {
        return deviceService.deleteDevice(id, null, null);
    }

    /**
     * Get device's group
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = GROUP_URI, method = RequestMethod.GET)
    public ResponseWrapper getDevicesGroup(@PathVariable Integer id) {
        return deviceService.getDevicesGroup(id);
    }

    /**
     * Add group for device
     *
     * @param id
     *      Device ID used as filter
     * @param deviceGroup
     *      Device group to add for given device
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = GROUP_URI, method = RequestMethod.POST)
    public ResponseWrapper addGroupForDevice(
        @PathVariable Integer id,
        @Valid @RequestBody DeviceGroup deviceGroup,
        Errors errors) {
        validateErrors(errors);

        return deviceService.addGroupForDevice(id, deviceGroup);
    }

    /**
     * Update device's group
     *
     * @param id
     *      Device ID used as filter
     * @param deviceGroup
     *      Device group used to replace device's current group
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = GROUP_URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDevicesGroup(
        @PathVariable Integer id,
        @Valid @RequestBody DeviceGroup deviceGroup,
        Errors errors) {
        validateErrors(errors);

        return deviceService.updateDevicesGroup(id, deviceGroup);
    }

    /**
     * Delete device's group
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = GROUP_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDevicesGroup(@PathVariable Integer id) {
        return deviceService.deleteDevicesGroup(id);
    }

    /**
     * Get device's type
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = TYPE_URI, method = RequestMethod.GET)
    public ResponseWrapper getDevicesType(@PathVariable Integer id) {
        return deviceService.getDevicesType(id);
    }

    /**
     * Add type for given device
     *
     * @param id
     *      Device ID used as filter
     * @param deviceType
     *      Device type to add for given device
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = TYPE_URI, method = RequestMethod.POST)
    public ResponseWrapper addTypeForDevice(
        @PathVariable Integer id,
        @Valid @RequestBody DeviceType deviceType,
        Errors errors) {
        validateErrors(errors);

        return deviceService.addTypeForDevice(id, deviceType);
    }

    /**
     * Update device's type
     *
     * @param id
     *      Device ID used as filter
     * @param deviceType
     *      Device type used to replace device's current type
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = TYPE_URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDevicesType(
        @PathVariable Integer id,
        @Valid @RequestBody DeviceType deviceType,
        Errors errors) {
        validateErrors(errors);

        return deviceService.updateDevicesType(id, deviceType);
    }

    /**
     * Delete device's type
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = TYPE_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDevicesType(@PathVariable Integer id) {
        return deviceService.deleteDevicesType(id);
    }

    /**
     * Get device's icon's information
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = ICON_URI, method = RequestMethod.GET)
    public ResponseWrapper getDevicesIconInformation(@PathVariable Integer id) {
        return deviceService.getDevicesIconInformation(id);
    }

    /**
     * Rename device's icon
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = ICON_URI, method = RequestMethod.PUT)
    public ResponseWrapper renameDevicesIcon(
        @PathVariable Integer id,
        @Valid @RequestBody DeviceIcon deviceIcon,
        Errors errors) {
        validateErrors(errors);

        return deviceService.renameDevicesIcon(id, deviceIcon);
    }

    /**
     * Delete device's icon
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = ICON_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDevicesIcon(@PathVariable Integer id) {
        return deviceService.deleteDevicesIcon(id);
    }

    /**
     * Get device's configuration
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = CONFIGURATION_URI, method = RequestMethod.GET)
    public ResponseWrapper getDevicesConfiguration(@PathVariable Integer id) {
        return deviceService.getDevicesConfiguration(id);
    }

    /**
     * Add configuration for given device
     *
     * @param id
     *      Device ID used as filter
     * @param configuration
     *      Configuration to add for given device
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = CONFIGURATION_URI, method = RequestMethod.POST)
    public ResponseWrapper addConfigurationForDevice(
        @PathVariable Integer id,
        @Valid @RequestBody Configuration configuration,
        Errors errors) {
        validateErrors(errors);

        return deviceService.addConfigurationForDevice(id, configuration);
    }

    /**
     * Update device's configuration
     *
     * @param id
     *      Device ID used as filter
     * @param configuration
     *      Device configuration used to replace device's current configuration
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = CONFIGURATION_URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDevicesConfiguration(
        @PathVariable Integer id,
        @Valid @RequestBody Configuration configuration,
        Errors errors) {
        validateErrors(errors);

        return deviceService.updateDevicesConfiguration(id, configuration);
    }

    /**
     * Delete device's configuration
     *
     * @param id
     *      Device ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = CONFIGURATION_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDevicesConfiguration(@PathVariable Integer id) {
        return deviceService.deleteDevicesConfiguration(id);
    }

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
    @RequestMapping(value = MEASUREMENTS_URI, method = RequestMethod.GET)
    public ResponseWrapper getDevicesMeasurements(
        @PathVariable Integer id,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "exactTime", required = false) Date exactTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "startTime", required = false) Date startTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "endTime", required = false) Date endTime) {
        return deviceService.getDevicesMeasurements(id, exactTime, startTime, endTime);
    }

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
    @RequestMapping(value = MEASUREMENTS_URI, method = RequestMethod.POST)
    public ResponseWrapper addMeasurementsForDevice(
        @PathVariable Integer id,
        @Valid @RequestBody Measurement measurement,
        Errors errors) {
        validateErrors(errors, "deviceId");

        return deviceService.addMeasurementForDevice(id, measurement);
    }

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
    @RequestMapping(value = MEASUREMENTS_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDevicesMeasurements(
        @PathVariable Integer id,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "exactTime", required = false) Date exactTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "startTime", required = false) Date startTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "endTime", required = false) Date endTime) {
        return deviceService.deleteDevicesMeasurements(id, exactTime, startTime, endTime);
    }

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
    @RequestMapping(value = LOCATIONS_URI, method = RequestMethod.GET)
    public ResponseWrapper getDevicesLocations(
        @PathVariable Integer id,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "exactTime", required = false) Date exactTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "startTime", required = false) Date startTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "endTime", required = false) Date endTime) {
        return deviceService.getDevicesLocations(id, exactTime, startTime, endTime);
    }

    /**
     * Add location for device
     *
     * @param id
     *      Device ID used as filter
     * @param location
     *      Location to add for given device
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = LOCATIONS_URI, method = RequestMethod.POST)
    public ResponseWrapper addLocationsForDevice(
        @PathVariable Integer id,
        @Valid @RequestBody Location location,
        Errors errors) {
        validateErrors(errors, "deviceId");

        return deviceService.addLocationForDevice(id, location);
    }

    /**
     * Delete device's location updates
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
    @RequestMapping(value = LOCATIONS_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDevicesLocations(
        @PathVariable Integer id,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "exactTime", required = false) Date exactTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "startTime", required = false) Date startTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "endTime", required = false) Date endTime) {
        return deviceService.deleteDevicesLocations(id, exactTime, startTime, endTime);
    }
}
