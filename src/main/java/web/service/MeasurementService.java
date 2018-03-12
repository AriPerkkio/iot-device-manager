package web.service;

import web.domain.entity.Measurement;
import web.domain.response.ResponseWrapper;

import java.util.Date;

public interface MeasurementService {
    /**
     * Get measurements matching given parameters.
     *
     * @param deviceId
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
    ResponseWrapper getMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime);

    /**
     * Add measurement to the database
     *
     * @param measurement
     *      Measurement to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addMeasurement(Measurement measurement);

    /**
     * Delete measurements matching given parameters
     *
     * @param deviceId
     *      Device ID used as filter
     * @param exactTime
     *      Time used as filter. Delete measurement updates which match timestamp
     * @param startTime
     *      Start time used as filter. Delete measurement updates which occurred after this time.
     * @param endTime
     *      End time used as filter. Delete measurement updates which occurred before this time.
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime);

}
