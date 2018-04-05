package web.controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import web.domain.entity.Device;
import web.domain.entity.DeviceGroup;
import web.domain.entity.DeviceIcon;
import web.domain.entity.DeviceType;
import web.domain.response.ResponseWrapper;
import web.service.DeviceService;

import javax.validation.Valid;

import static web.validators.FilterValidator.validateErrors;

@RestController
@RequestMapping(value = "/api", produces = "application/vnd.collection+json; charset=utf-8")
public class DeviceController {

    private static final String URI = "/devices";
    private static final String ID_URI = URI + "/{id}";
    private static final String GROUP_URI = ID_URI + "/group";
    private static final String TYPE_URI = ID_URI + "/type";
    private static final String ICON_URI = ID_URI + "/icon";
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
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="deviceTypeId", required = false) Integer deviceTypeId,
            @RequestParam(name="deviceGroupId", required = false) Integer deviceGroupId,
            @RequestParam(name="configurationId", required = false) Integer configurationId,
            @RequestParam(name="authenticationKey", required = false) String authenticationKey) {
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
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="authenticationKey", required = false) String authenticationKey,
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
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="authenticationKey", required = false) String authenticationKey) {
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

}
