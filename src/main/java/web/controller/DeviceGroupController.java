package web.controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import web.domain.entity.DeviceGroup;
import web.domain.response.ResponseWrapper;
import web.service.DeviceGroupService;

import javax.validation.Valid;

import static web.validators.FilterValidator.validateErrors;

@RestController
@RequestMapping("/api")
public class DeviceGroupController {

    private static final String URI = "/device-groups";
    private static final String ID_URI = URI + "/{id}";
    private static final String CONTENT_TYPE = "application/vnd.collection+json; charset=utf-8";
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
    @RequestMapping(value = URI, method = RequestMethod.GET, produces = CONTENT_TYPE)
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
    @RequestMapping(value = ID_URI, method = RequestMethod.GET, produces = CONTENT_TYPE)
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
    @RequestMapping(value = URI, method = RequestMethod.POST, produces = CONTENT_TYPE)
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
    @RequestMapping(value = URI, method = RequestMethod.PUT, produces = CONTENT_TYPE)
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
    @RequestMapping(value = ID_URI, method = RequestMethod.PUT, produces = CONTENT_TYPE)
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
    @RequestMapping(value = URI, method = RequestMethod.DELETE, produces = CONTENT_TYPE)
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
    @RequestMapping(value = ID_URI, method = RequestMethod.DELETE, produces = CONTENT_TYPE)
    public ResponseWrapper deleteDeviceGroupById(
            @PathVariable Integer id) {
        return deviceGroupService.deleteDeviceGroup(id, null);
    }
}
