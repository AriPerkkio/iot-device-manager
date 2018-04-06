package web.controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import web.domain.entity.Device;
import web.domain.entity.DeviceType;
import web.domain.response.ResponseWrapper;
import web.service.DeviceTypeService;

import javax.validation.Valid;

import static web.validators.FilterValidator.validateErrors;

@RestController
@RequestMapping(value = "/api", produces = "application/vnd.collection+json; charset=utf-8")
public class DeviceTypeController {

    private static final String URI = "/device-types";
    private static final String ID_URI = URI + "/{id}";
    private static final String DEVICES_URI = ID_URI + "/devices";
    private final DeviceTypeService deviceTypeService;

    DeviceTypeController(DeviceTypeService deviceTypeService) {
        this.deviceTypeService = deviceTypeService;
    }

    /**
     * Get device types matching given request parameters
     *
     * @param id
     *      Device type ID used as filter
     * @param name
     *      Device type name used as filter
     * @param deviceIconId
     *      Device icon ID used as filter
     * @return
     *      ResponseWrapper containing payload or error
     */
    @RequestMapping(value = URI, method = RequestMethod.GET)
    public ResponseWrapper getDeviceTypes(
        @RequestParam(value="id", required = false) Integer id,
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value="deviceIconId", required = false) Integer deviceIconId) {
        return deviceTypeService.getDeviceTypes(id, name, deviceIconId);
    }

    /**
     * Get device types matching given ID
     *
     * @param id
     *      Device type ID used as filter
     * @return
     *      ResponseWrapper containing payload or error
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.GET)
    public ResponseWrapper getDeviceTypeById(
        @PathVariable Integer id) {
        return deviceTypeService.getDeviceTypes(id, null, null);
    }

    /**
     * Add device type
     *
     * @param deviceType
     *      Device type to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.POST)
    public ResponseWrapper addDeviceType(
        @Valid @RequestBody DeviceType deviceType,
        Errors errors) {
        validateErrors(errors);

        return deviceTypeService.addDeviceType(deviceType);
    }

    /**
     * Update device type matching given parameters
     *
     * @param id
     *      Device type ID used as filter
     * @param name
     *      Device type name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDeviceType(
        @RequestParam(value="id", required = false) Integer id,
        @RequestParam(value="name", required = false) String name,
        @Valid @RequestBody DeviceType deviceType,
        Errors errors) {
        validateErrors(errors);

        return deviceTypeService.updateDeviceType(id, name, deviceType);
    }

    /**
     * Update device type matching given ID
     *
     * @param id
     *      Device type ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDeviceTypeById(
        @PathVariable Integer id,
        @Valid @RequestBody DeviceType deviceType,
        Errors errors) {
        validateErrors(errors);

        return deviceTypeService.updateDeviceType(id, null, deviceType);
    }

    /**
     * Delete device type matching given parameters
     *
     * @param id
     *      Device type ID used as filter
     * @param name
     *      Device type name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDeviceType(
        @RequestParam(value="id", required = false) Integer id,
        @RequestParam(value="name", required = false) String name) {
        return deviceTypeService.deleteDeviceType(id, name);
    }

    /**
     * Delete device type matching given ID
     *
     * @param id
     *      Device type ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDeviceTypeById(@PathVariable Integer id) {
        return deviceTypeService.deleteDeviceType(id, null);
    }

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
    @RequestMapping(value = DEVICES_URI, method = RequestMethod.GET)
    public ResponseWrapper getTypesDevices(
        @PathVariable Integer id,
        @RequestParam(name="deviceGroupId", required = false) Integer deviceGroupId,
        @RequestParam(name="configurationId", required = false) Integer configurationId) {
        return deviceTypeService.getTypesDevices(id, deviceGroupId, configurationId);
    }

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
    @RequestMapping(value = DEVICES_URI, method = RequestMethod.POST)
    public ResponseWrapper addDeviceWithType(
        @PathVariable Integer id,
        @Valid @RequestBody Device device,
        Errors errors) {
        validateErrors(errors);
        return deviceTypeService.addDeviceWithType(id, device);
    }
}
