package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.Location;
import web.domain.response.ErrorWrapper;
import web.domain.response.ResponseWrapper;
import web.repository.LocationRepository;
import web.service.LocationService;

import java.util.Date;

import static web.domain.response.ErrorCode.INTERNAL_ERROR;

@Service
public class LocationServiceImpl implements LocationService {

    final LocationRepository locationRepository;

    LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public ResponseWrapper getLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        try {
            return new ResponseWrapper(locationRepository.getLocations(deviceId, exactTime, startTime, endTime));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper addLocation(Location location) {
        try {
            return new ResponseWrapper(locationRepository.addLocation(location));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper deleteLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        try {
            return new ResponseWrapper(locationRepository.deleteLocations(deviceId, exactTime, startTime, endTime));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }
}
