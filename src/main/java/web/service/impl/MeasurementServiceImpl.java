package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.Measurement;
import web.domain.response.ResponseWrapper;
import web.repository.MeasurementRepository;
import web.service.MeasurementService;

import java.util.Collections;
import java.util.Date;

@Service
public class MeasurementServiceImpl implements MeasurementService {
    final MeasurementRepository measurementRepository;

    MeasurementServiceImpl(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Override
    public ResponseWrapper getMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(measurementRepository.getMeasurements(deviceId, exactTime, startTime, endTime));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper addMeasurement(Measurement measurement) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(measurementRepository.addMeasurement(measurement));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;

    }

    @Override
    public ResponseWrapper deleteMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(measurementRepository.deleteMeasurements(deviceId, exactTime, startTime, endTime));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }
}
