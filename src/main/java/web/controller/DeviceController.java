package web.controller;

import org.springframework.web.bind.annotation.*;
import web.domain.entity.Device;
import web.domain.response.ErrorCode;
import web.domain.response.ErrorWrapper;
import web.domain.response.ResponseWrapper;
import web.service.DeviceService;

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
     *      TODO
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
     * Add device from request body to the database
     *
     * @param device
     *      Device to add
     * @return
     *      TODO
     */
    @RequestMapping(value = URI, method = RequestMethod.POST)
    public ResponseWrapper addDevice(@RequestBody Device device) {
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
     *      TODO
     */
    @RequestMapping(value = URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDevice(
            @RequestParam(name="id", required = false) Integer id,
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="authenticationKey", required = false) String authenticationKey,
            @RequestBody Device device) {
        return deviceService.updateDevice(id, name, authenticationKey, device);
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
     *      TODO
     */
    @RequestMapping(value = URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDevice(
            @RequestParam(name="id", required = false) Integer id,
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="authenticationKey", required = false) String authenticationKey) {
        return deviceService.deleteDevice(id, name, authenticationKey);
    }

    @RequestMapping(value = ID_URI, method = RequestMethod.GET)
    public ResponseWrapper getDevicesIcon() {
        return new ResponseWrapper(new ErrorWrapper(ErrorCode.INTERNAL_ERROR, "TODO endpoints"));
    }
}
