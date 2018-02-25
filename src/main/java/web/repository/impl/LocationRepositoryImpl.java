package web.repository.impl;

import org.springframework.stereotype.Repository;
import web.domain.entity.Location;
import web.repository.LocationRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

@Repository
public class LocationRepositoryImpl implements LocationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Location> getLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        StoredProcedureQuery getLocationsQuery =
            entityManager.createNamedStoredProcedureQuery("get_locations")
                .setParameter("f_device_id", deviceId)
                .setParameter("f_exact_time", exactTime)
                .setParameter("f_start_time", startTime)
                .setParameter("f_end_time", endTime);

        return getLocationsQuery.getResultList();
    }

    @Override
    public Location addLocation(Location location) {
        StoredProcedureQuery addLocationQuery =
            entityManager.createNamedStoredProcedureQuery("add_location")
                .setParameter("p_device_id", location.getDeviceId())
                .setParameter("p_latitude", location.getLatitude())
                .setParameter("p_longitude", location.getLongitude())
                .setParameter("p_time", location.getTime());

        return (Location) addLocationQuery.getResultList().get(0);
    }

    @Override
    public Boolean deleteLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        StoredProcedureQuery deleteLocationsQuery =
            entityManager.createNamedStoredProcedureQuery("delete_locations")
                .setParameter("f_device_id", deviceId)
                .setParameter("f_exact_time", exactTime)
                .setParameter("f_start_time", startTime)
                .setParameter("f_end_time", endTime);

        return BigInteger.ONE.equals(deleteLocationsQuery.getSingleResult());
    }
}

