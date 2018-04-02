package web.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import web.domain.entity.Location;
import web.domain.response.ResponseWrapper;
import web.service.LocationService;

import javax.validation.Valid;
import java.util.Date;

import static web.validators.FilterValidator.validateErrors;

@RestController
@RequestMapping("/api")
public class LocationController {

    private static final String URI = "/locations";
    private static final String CONTENT_TYPE = "application/vnd.collection+json; charset=utf-8";
    private final LocationService locationService;

    LocationController (LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Get location updates matching given parameters.
     *
     * @param deviceId
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
    @RequestMapping(value = URI, method = RequestMethod.GET, produces = CONTENT_TYPE)
    public ResponseWrapper getLocations(
        @RequestParam(value = "deviceId", required = false) Integer deviceId,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "exactTime", required = false) Date exactTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "startTime", required = false) Date startTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "endTime", required = false) Date endTime) {
        return locationService.getLocations(deviceId, exactTime, startTime, endTime);
    }

    /**
     * Add location update to the database
     *
     * @param location
     *      Location update to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.POST, produces = CONTENT_TYPE)
    public ResponseWrapper addLocation(
        @Valid @RequestBody Location location,
        Errors errors) {
        validateErrors(errors);

        return locationService.addLocation(location);
    }

    /**
     * Delete locations matching given parameters
     *
     * @param deviceId
     *      Device ID used as filter
     * @param exactTime
     *      Time used as filter. Delete location updates which match timestamp
     * @param startTime
     *      Start time used as filter. Delete location updates which occurred after this time.
     * @param endTime
     *      End time used as filter. Delete location updates which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.DELETE, produces = CONTENT_TYPE)
    public ResponseWrapper deleteLocations(
        @RequestParam(value = "deviceId", required = false) Integer deviceId,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "exactTime", required = false) Date exactTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "startTime", required = false) Date startTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "endTime", required = false) Date endTime) {
        return locationService.deleteLocations(deviceId, exactTime, startTime, endTime);
    }
}
