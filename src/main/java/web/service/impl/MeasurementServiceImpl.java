package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import web.domain.entity.Measurement;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.repository.MeasurementRepository;
import web.service.DeviceService;
import web.service.MeasurementService;
import web.validators.FilterValidator;

import java.util.Collection;
import java.util.Date;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.MapperUtils.formatTime;
import static web.mapper.MeasurementMapper.mapToCollection;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final DeviceService deviceService;

    MeasurementServiceImpl(MeasurementRepository measurementRepository, DeviceService deviceService) {
        this.measurementRepository = measurementRepository;
        this.deviceService = deviceService;
    }

    @Override
    public ResponseWrapper getMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        try {
            Collection<Measurement> measurements = measurementRepository.getMeasurements(deviceId, exactTime, startTime, endTime);

            if(CollectionUtils.isEmpty(measurements)) {
                throwNotFoundException(String.format(
                    "[deviceId: %d, exactTime: %s, startTime: %s, endTime: %s]",
                    deviceId, formatTime(exactTime), formatTime(startTime), formatTime(endTime)));
            }

            return new ResponseWrapper(mapToCollection(measurements));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get measurements failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addMeasurement(Measurement measurement) {
        try {
            deviceService.validateDeviceExists(measurement.getDeviceId(), null, null);
            Measurement addedMeasurement = measurementRepository.addMeasurement(measurement);

            return new ResponseWrapper(mapToCollection(addedMeasurement));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add measurement failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        try {
            FilterValidator.checkForMinimumFilters(deviceId, exactTime, startTime, endTime);
            validateMeasurementExists(deviceId, exactTime, startTime, endTime);

            Boolean deleteSuccessful = measurementRepository.deleteMeasurements(deviceId, exactTime, startTime, endTime);

            if (!deleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete measurements failed");
        }

        return null;
    }

    private void validateMeasurementExists(Integer deviceId, Date exactTime, Date startTime, Date endTime) throws NotFoundException  {
        Collection<Measurement> measurements = measurementRepository.getMeasurements(deviceId, exactTime, startTime, endTime);

        if(CollectionUtils.isEmpty(measurements)) {
            throwNotFoundException(String.format(
                "[deviceId: %d, exactTime: %s, startTime: %s, endTime: %s]",
                deviceId, formatTime(exactTime), formatTime(startTime), formatTime(endTime)));
        }
    }
}
