package web.repository;

import web.domain.entity.Location;

import java.util.Collection;
import java.util.Date;

public interface LocationRepository {
    /**
     * Get locations matching given parameters. Parameters are optional and ignored when null value passed.
     *
     * @param deviceId
     *          Device ID used as filter
     * @param exactTime
     *          Time used as filter. Search location updates which match timestamp
     * @param startTime
     *          Start time used as filter. Search location updates which occurred after this time.
     * @param endTime
     *          End time used as filter. Search location updates which occurred before this time.
     * @return
     *          Locations matching given parameters
     */
    Collection<Location> getLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime);

    /**
     * Add location to the database
     *
     * @param location
     *          Location to add. If time is not defined current time is used
     * @return
     *          Inserted location
     */
    Location addLocation(Location location);

    /**
     * Delete locations matching given parameters.
     *
     * @param deviceId
     *          Device ID used as filter
     * @param exactTime
     *          Time used as filter. Delete location updates which match timestamp
     * @param startTime
     *          Start time used as filter. Delete location updates which occurred after this time.
     * @param endTime
     *          End time used as filter. Delete location updates which occurred before this time.
     * @return
     */
    Boolean deleteLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime);
}
