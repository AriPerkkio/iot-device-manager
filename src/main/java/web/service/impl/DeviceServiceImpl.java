package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.Device;
import web.domain.response.ResponseWrapper;
import web.repository.DeviceRepository;
import web.service.DeviceService;
import web.validators.FilterValidator;

import java.util.Collections;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public ResponseWrapper getDevices(Integer id, String name, Integer deviceTypeId, Integer deviceGroupId,
                                      Integer configurationId, String authenticationKey) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceRepository.getDevices(id, name, deviceTypeId, deviceGroupId, configurationId,
                    authenticationKey));
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

    @Override
    public ResponseWrapper updateDevice(Integer id, String name, String authenticationKey, Device device) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            FilterValidator.checkForMinimumFilters(id, name, authenticationKey);
            response.setPayload(deviceRepository.updateDevice(id, name, authenticationKey, device));
        } catch(Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper deleteDevice(Integer id, String name, String authenticationKey) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            FilterValidator.checkForMinimumFilters(id, name, authenticationKey);
            response.setPayload(deviceRepository.deleteDevice(id, name, authenticationKey));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }
}