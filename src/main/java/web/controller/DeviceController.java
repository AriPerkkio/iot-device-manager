package web.controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import web.domain.entity.Device;
import web.domain.response.ResponseWrapper;
import web.service.DeviceService;

import javax.validation.Valid;

import static web.validators.FilterValidator.validateErrors;

@RestController
@RequestMapping("/api")
public class DeviceController {

    private static final String URI = "/devices";
    private static final String ID_URI = URI + "/{id}";
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
    @RequestMapping(value = URI, method = RequestMethod.GET, produces = "application/vnd.collection+json; charset=utf-8")
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
    @RequestMapping(value = ID_URI, method = RequestMethod.GET, produces = "application/vnd.collection+json; charset=utf-8")
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
    @RequestMapping(value = URI, method = RequestMethod.POST, produces = "application/vnd.collection+json; charset=utf-8")
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
    @RequestMapping(value = URI, method = RequestMethod.PUT, produces = "application/vnd.collection+json; charset=utf-8")
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
    @RequestMapping(value = ID_URI, method = RequestMethod.PUT, produces = "application/vnd.collection+json; charset=utf-8")
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
    @RequestMapping(value = URI, method = RequestMethod.DELETE, produces = "application/vnd.collection+json; charset=utf-8")
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
    @RequestMapping(value = ID_URI, method = RequestMethod.DELETE, produces = "application/vnd.collection+json; charset=utf-8")
    public ResponseWrapper deleteDeviceById(@PathVariable Integer id) {
        return deviceService.deleteDevice(id, null, null);
    }
}
