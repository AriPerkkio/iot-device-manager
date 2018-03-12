package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.DeviceType;
import web.domain.response.ResponseWrapper;
import web.repository.DeviceTypeRepository;
import web.service.DeviceTypeService;
import web.validators.FilterValidator;

import java.util.Collections;

@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    private final DeviceTypeRepository deviceTypeRepository;

    DeviceTypeServiceImpl(DeviceTypeRepository deviceTypeRepository) {
        this.deviceTypeRepository = deviceTypeRepository;
    }

    @Override
    public ResponseWrapper getDeviceTypes(Integer id, String name, Integer deviceIconId) {
        final ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceTypeRepository.getDeviceTypes(id, name, deviceIconId));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper addDeviceType(DeviceType deviceType) {
        final ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceTypeRepository.addDeviceType(deviceType));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper updateDeviceType(Integer id, String name, DeviceType deviceType) {
        final ResponseWrapper response = new ResponseWrapper();

        try {
            FilterValidator.checkForMinimumFilters(id, name);
            response.setPayload(deviceTypeRepository.updateDeviceType(id, name, deviceType));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper deleteDeviceType(Integer id, String name) {
        final ResponseWrapper response = new ResponseWrapper();

        try {
            FilterValidator.checkForMinimumFilters(id, name);
            response.setPayload(deviceTypeRepository.deleteDeviceType(id, name));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }
}
