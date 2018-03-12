package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.Location;
import web.domain.response.ResponseWrapper;
import web.repository.LocationRepository;
import web.service.LocationService;

import java.util.Collections;
import java.util.Date;

@Service
public class LocationServiceImpl implements LocationService {

    final LocationRepository locationRepository;

    LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public ResponseWrapper getLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(locationRepository.getLocations(deviceId, exactTime, startTime, endTime));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper addLocation(Location location) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(locationRepository.addLocation(location));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;

    }

    @Override
    public ResponseWrapper deleteLocations(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(locationRepository.deleteLocations(deviceId, exactTime, startTime, endTime));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }
}
