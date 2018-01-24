package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.Device;
import web.domain.response.ResponseWrapper;
import web.repository.DeviceRepository;
import web.service.DeviceService;

import java.util.Collections;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public ResponseWrapper getDevices(Integer deviceTypeId, Integer deviceGroupId, Integer configurationId) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceRepository.getDevices(deviceTypeId, deviceGroupId, configurationId));
        } catch(Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;

    }

    @Override
    public ResponseWrapper getDevice(String name, String authenticationKey) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceRepository.getDevice(name, authenticationKey));
        } catch(Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper addDevice(Device device) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceRepository.addDevice(device));
        } catch(Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }
}