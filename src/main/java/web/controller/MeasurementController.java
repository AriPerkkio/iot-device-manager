package web.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import web.domain.entity.Measurement;
import web.domain.response.ResponseWrapper;
import web.service.MeasurementService;

import javax.validation.Valid;
import java.util.Date;

import static web.validators.FilterValidator.validateErrors;

@RestController
@RequestMapping("/api")
public class MeasurementController {
    private static final String URI = "/measurements";
    private static final String CONTENT_TYPE = "application/vnd.collection+json; charset=utf-8";
    private final MeasurementService measurementService;

    MeasurementController (MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    /**
     * Get measurements matching given parameters.
     *
     * @param deviceId
     *      Device ID used as filter
     * @param exactTime
     *      Time used as filter. Search measurements which match timestamp
     * @param startTime
     *      Start time used as filter. Search measurements which occurred after this time.
     * @param endTime
     *      End time used as filter. Search measurements which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.GET, produces = CONTENT_TYPE)
    public ResponseWrapper getMeasurements(
        @RequestParam(value = "deviceId", required = false) Integer deviceId,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "exactTime", required = false) Date exactTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "startTime", required = false) Date startTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "endTime", required = false) Date endTime) {
        return measurementService.getMeasurements(deviceId, exactTime, startTime, endTime);
    }

    /**
     * Add measurement to the database
     *
     * @param measurement
     *      Measurement to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.POST, produces = CONTENT_TYPE)
    public ResponseWrapper addMeasurement(
        @Valid @RequestBody Measurement measurement,
        Errors errors) {
        validateErrors(errors);

        return measurementService.addMeasurement(measurement);
    }

    /**
     * Delete measurements matching given parameters
     *
     * @param deviceId
     *      Device ID used as filter
     * @param exactTime
     *      Time used as filter. Delete measurements which match timestamp
     * @param startTime
     *      Start time used as filter. Deletes updates which occurred after this time.
     * @param endTime
     *      End time used as filter. Delete measurements which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    @RequestMapping(value = URI, method = RequestMethod.DELETE, produces = CONTENT_TYPE)
    public ResponseWrapper deleteMeasurements(
        @RequestParam(value = "deviceId", required = false) Integer deviceId,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "exactTime", required = false) Date exactTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "startTime", required = false) Date startTime,
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam(value = "endTime", required = false) Date endTime) {
        return measurementService.deleteMeasurements(deviceId, exactTime, startTime, endTime);
    }
}
