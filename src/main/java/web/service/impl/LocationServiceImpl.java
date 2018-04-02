package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import web.domain.entity.Location;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.repository.LocationRepository;
import web.service.DeviceService;
import web.service.LocationService;
import web.validators.FilterValidator;

import java.util.Collection;
import java.util.Date;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.LocationMapper.formatTime;
import static web.mapper.LocationMapper.mapToCollection;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final DeviceService deviceService;

    LocationServiceImpl(LocationRepository locationRepository, DeviceService deviceService) {
        this.locationRepository = locationRepository;
        this.deviceService = deviceService;
    }

    @Override
    public ResponseWrapper getLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        try {
            Collection<Location> locations = locationRepository.getLocations(deviceId, exactTime, startTime, endTime);

            if(CollectionUtils.isEmpty(locations)) {
                throwNotFoundException(String.format(
                    "[deviceId: %d, exactTime: %s, startTime: %s, endTime: %s]",
                    deviceId, formatTime(exactTime), formatTime(startTime), formatTime(endTime)));
            }

            return new ResponseWrapper(mapToCollection(locations));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get locations failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addLocation(Location location) {
        try {
            deviceService.validateDeviceExists(location.getDeviceId(), null, null);
            Location addedLocation = locationRepository.addLocation(location);

            return new ResponseWrapper(mapToCollection(addedLocation));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add location failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        try {
            FilterValidator.checkForMinimumFilters(deviceId, exactTime, startTime, endTime);
            validateLocationExists(deviceId, exactTime, startTime, endTime);

            Boolean deleteSuccessful = locationRepository.deleteLocations(deviceId, exactTime, startTime, endTime);

            if (!deleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete location failed");
        }

        return null;
    }

    private void validateLocationExists(Integer deviceId, Date exactTime, Date startTime, Date endTime) throws NotFoundException  {
        Collection<Location> locations = locationRepository.getLocations(deviceId, exactTime, startTime, endTime);

        if(CollectionUtils.isEmpty(locations)) {
            throwNotFoundException(String.format(
                "[deviceId: %d, exactTime: %s, startTime: %s, endTime: %s]",
                deviceId, formatTime(exactTime), formatTime(startTime), formatTime(endTime)));
        }
    }
}
