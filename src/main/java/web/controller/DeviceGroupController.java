package web.controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import web.domain.entity.Device;
import web.domain.entity.DeviceGroup;
import web.domain.response.ResponseWrapper;
import web.service.DeviceGroupService;

import javax.validation.Valid;

import static web.validators.FilterValidator.validateErrors;

@RestController
@RequestMapping(value = "/api", produces = "application/vnd.collection+json; charset=utf-8")
public class DeviceGroupController {

    private static final String URI = "/device-groups";
    private static final String ID_URI = URI + "/{id}";
    private static final String DEVICES_URI = ID_URI + "/devices";
    private final DeviceGroupService deviceGroupService;

    DeviceGroupController(DeviceGroupService deviceGroupService) {
        this.deviceGroupService = deviceGroupService;
    }

    /**
     * Get device groups matching given request parameters
     *
     * @param id
     *      Device group ID used as filter
     * @param name
     *      Device group name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.GET)
    public ResponseWrapper getDeviceGroups(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name) {
        return deviceGroupService.getDeviceGroups(id, name);
    }

    /**
     * Get device group by id
     *
     * @param id
     *      Device group ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.GET)
    public ResponseWrapper getDeviceGroupById(
            @PathVariable Integer id) {
        return deviceGroupService.getDeviceGroups(id, null);
    }

    /**
     * Add device group
     *
     * @param deviceGroup
     *      Device group to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.POST)
    public ResponseWrapper addDeviceGroup(
            @Valid @RequestBody DeviceGroup deviceGroup,
            Errors errors) {
        validateErrors(errors);

        return deviceGroupService.addDeviceGroup(deviceGroup);
    }

    /**
     * Update device group matching given request parameters
     *
     * @param id
     *      Device group ID used as filter
     * @param name
     *      Device group name used as filter
     * @param deviceGroup
     *      Device group used to replace existing one
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDeviceGroup(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name,
            @Valid @RequestBody DeviceGroup deviceGroup,
            Errors errors) {
        validateErrors(errors);

        return deviceGroupService.updateDeviceGroup(id, name, deviceGroup);
    }

    /**
     * Update device group matching given ID
     *
     * @param id
     *      Device group ID used as filter
     * @param deviceGroup
     *      Device group used to replace existing one
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.PUT)
    public ResponseWrapper updateDeviceGroupById(
            @PathVariable Integer id,
            @Valid @RequestBody DeviceGroup deviceGroup,
            Errors errors) {
        validateErrors(errors);

        return deviceGroupService.updateDeviceGroup(id, null, deviceGroup);
    }

    /**
     * Delete device group matching given parameters
     *
     * @param id
     *      Device group ID used as filter
     * @param name
     *      Device group name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDeviceGroup(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name) {
        return deviceGroupService.deleteDeviceGroup(id, name);
    }

    /**
     * Delete device group matching given ID
     *
     * @param id
     *      Device group ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDeviceGroupById(
            @PathVariable Integer id) {
        return deviceGroupService.deleteDeviceGroup(id, null);
    }

    /**
     * Get group's devices
     *
     * @param id
     *      Device group ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = DEVICES_URI, method = RequestMethod.GET)
    public ResponseWrapper getGroupsDevices(@PathVariable Integer id) {
        return deviceGroupService.getGroupsDevices(id);
    }

    /**
     * Add device to group
     *
     * @param id
     *      Device group ID used as filter
     * @param device
     *      Device to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = DEVICES_URI, method = RequestMethod.POST)
    public ResponseWrapper addDeviceToGroup(
        @PathVariable Integer id,
        @Valid @RequestBody Device device,
        Errors errors) {
        validateErrors(errors, "id");

        return deviceGroupService.addDeviceToGroup(id, device);
    }
}
