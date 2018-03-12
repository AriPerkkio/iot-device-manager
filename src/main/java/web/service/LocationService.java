package web.service;

import web.domain.entity.Location;
import web.domain.response.ResponseWrapper;

import java.util.Date;

public interface LocationService {

    /**
     * Get locations matching given parameters.
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
    ResponseWrapper getLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime);

    /**
     * Add location to the database
     *
     * @param location
     *      Location to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addLocation(Location location);

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
    ResponseWrapper deleteLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime);
}
