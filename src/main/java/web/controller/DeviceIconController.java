package web.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.domain.entity.DeviceIcon;
import web.domain.entity.DeviceType;
import web.domain.response.ResponseWrapper;
import web.service.DeviceIconService;

import javax.validation.Valid;

import static web.validators.FilterValidator.validateErrors;

@RestController
@RequestMapping(value = "/api", produces = "application/vnd.collection+json; charset=utf-8")
public class DeviceIconController {

    private static final String URI = "/device-icons";
    private static final String ID_URI = URI + "/{id}";
    private static final String NAME_URI = URI + "/{name}.png";
    private static final String TYPES_URI = ID_URI + "/types";
    private final DeviceIconService deviceIconService;

    DeviceIconController(DeviceIconService deviceIconService){
        this.deviceIconService = deviceIconService;
    }

    /**
     * Get device icons' information matching given request parameters
     *
     * @param id
     *      Device icon ID used as filter
     * @param name
     *      Device icon name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.GET)
    public ResponseWrapper getDeviceIcons(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name) {
        return deviceIconService.getDeviceIcons(id, name);
    }

    /**
     * Get device icon's information matching given ID
     *
     * @param id
     *      Device icon ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.GET)
    public ResponseWrapper getDeviceIconInformationById(@PathVariable Integer id) {
        return deviceIconService.getDeviceIcons(id, null);
    }

    /**
     * Get device icon file by name
     *
     * @param name
     *      Device icon name used as filter
     * @return
     *      Device icon .png file as {@link Resource} wrapper in {@link ResponseEntity}
     */
    @RequestMapping(value = NAME_URI, method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getDeviceIconFile(@PathVariable String name) {

        // Endpoint matches .png files only - at this point it should be safe to add extension manually
        Resource icon = deviceIconService.getDeviceIconFile(name + ".png");

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + icon.getFilename() + "\"")
                .body(icon);
    }

    /**
     * Upload device icon with given name
     *
     * @param icon
     *      Device icon as .png to upload
     * @param name
     *      Device icon name to use for new icon
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.POST)
    public ResponseWrapper uploadDeviceIcon(
            @RequestParam("name") String name,
            @Valid @RequestBody MultipartFile icon) {

        if(icon == null) {
            throw new HttpMessageNotReadableException("Request body missing");
        }

        return deviceIconService.addDeviceIcon(icon, name);
    }

    /**
     * Rename device icon matching given request parameters
     *
     * @param id
     *      Device icon ID used as filter
     * @param name
     *      Device icon name used as filter
     * @param deviceIcon
     *      Device icon used to replace existing one
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.PUT)
    public ResponseWrapper renameDeviceIcon(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name,
            @Valid @RequestBody DeviceIcon deviceIcon,
            Errors errors) {
        validateErrors(errors);

        return deviceIconService.updateDeviceIcon(id, name, deviceIcon);
    }

    /**
     * Rename device icon matching given ID
     *
     * @param id
     *      Device icon ID used as filter
     * @param deviceIcon
     *      Device icon used to replace existing one
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.PUT)
    public ResponseWrapper renameDeviceIconById(
            @PathVariable Integer id,
            @Valid @RequestBody DeviceIcon deviceIcon,
            Errors errors) {
        validateErrors(errors);

        return deviceIconService.updateDeviceIcon(id, null, deviceIcon);
    }

    /**
     * Delete device icon matching given parameters
     *
     * @param id
     *      Device icon used as filter
     * @param name
     *      Device icon name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDeviceIcon(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name) {

        return deviceIconService.deleteDeviceIcon(id, name);
    }

    /**
     * Delete device icon matching given ID
     *
     * @param id
     *      Device icon used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.DELETE)
    public ResponseWrapper deleteDeviceIconById(
            @PathVariable Integer id) {
        return deviceIconService.deleteDeviceIcon(id, null);
    }

    /**
     * Get icon's types
     *
     * @param id
     *      Device icon ID used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = TYPES_URI, method = RequestMethod.GET)
    public ResponseWrapper getIconsTypes(
        @PathVariable Integer id) {
        return deviceIconService.getIconsTypes(id);
    }

    /**
     * Add type with icon
     *
     * @param id
     *      Device icon ID used as filter
     * @param deviceType
     *      Device type to add with given icon
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = TYPES_URI, method = RequestMethod.POST)
    public ResponseWrapper addTypeWithIcon(
        @PathVariable Integer id,
        @Valid @RequestBody DeviceType deviceType,
        Errors errors) {
        validateErrors(errors);

        return deviceIconService.addTypeWithIcon(id, deviceType);
    }
}
