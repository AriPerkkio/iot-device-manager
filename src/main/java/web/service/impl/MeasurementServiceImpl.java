package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.Measurement;
import web.domain.response.ErrorWrapper;
import web.domain.response.ResponseWrapper;
import web.repository.MeasurementRepository;
import web.service.MeasurementService;

import java.util.Date;

import static web.domain.response.ErrorCode.INTERNAL_ERROR;

@Service
public class MeasurementServiceImpl implements MeasurementService {
    final MeasurementRepository measurementRepository;

    MeasurementServiceImpl(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Override
    public ResponseWrapper getMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        try {
            return new ResponseWrapper(measurementRepository.getMeasurements(deviceId, exactTime, startTime, endTime));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper addMeasurement(Measurement measurement) {
        try {
            return new ResponseWrapper(measurementRepository.addMeasurement(measurement));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper deleteMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        try {
            return new ResponseWrapper(measurementRepository.deleteMeasurements(deviceId, exactTime, startTime, endTime));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }
}
