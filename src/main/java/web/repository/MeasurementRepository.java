package web.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import web.domain.entity.Measurement;

import java.util.Collection;
import java.util.Date;

public interface MeasurementRepository {
    /**
     * Get measurements matching given parameters. Parameters are optional and ignored when null value passed.
     *
     * @param deviceId
     *          Device ID used as filter
     * @param exactTime
     *          Time used as filter. Search measurements which match timestamp
     * @param startTime
     *          Start time used as filter. Search measurements which occurred after this time.
     * @param endTime
     *          End time used as filter. Search measurements which occurred before this time.
     * @return
     *          Measurements matching given parameters
     */
    Collection<Measurement> getMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime);

    /**
     * Add measurement to the database
     *
     * @param measurement
     *          Measurement to add. If time is not defined current time is used
     * @return
     *          Inserted measurement
     */
    Measurement addMeasurement(Measurement measurement) throws JsonProcessingException;

    /**
     * Delete measurements matching given parameters.
     *
     * @param deviceId
     *          Device ID used as filter
     * @param exactTime
     *          Time used as filter. Delete measurements which match timestamp
     * @param startTime
     *          Start time used as filter. Delete measurements which occurred after this time.
     * @param endTime
     *          End time used as filter. Delete measurements which occurred before this time.
     * @return
     */
    Boolean deleteMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime);
}
