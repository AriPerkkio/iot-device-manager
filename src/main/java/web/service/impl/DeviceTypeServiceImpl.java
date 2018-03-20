package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.DeviceType;
import web.domain.response.ErrorWrapper;
import web.domain.response.ResponseWrapper;
import web.repository.DeviceTypeRepository;
import web.service.DeviceTypeService;
import web.validators.FilterValidator;

import static web.domain.response.ErrorCode.INTERNAL_ERROR;

@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    private final DeviceTypeRepository deviceTypeRepository;

    DeviceTypeServiceImpl(DeviceTypeRepository deviceTypeRepository) {
        this.deviceTypeRepository = deviceTypeRepository;
    }

    @Override
    public ResponseWrapper getDeviceTypes(Integer id, String name, Integer deviceIconId) {
        try {
            return new ResponseWrapper(deviceTypeRepository.getDeviceTypes(id, name, deviceIconId));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper addDeviceType(DeviceType deviceType) {
        try {
            return new ResponseWrapper(deviceTypeRepository.addDeviceType(deviceType));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper updateDeviceType(Integer id, String name, DeviceType deviceType) {
        try {
            FilterValidator.checkForMinimumFilters(id, name);
            return new ResponseWrapper(deviceTypeRepository.updateDeviceType(id, name, deviceType));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper deleteDeviceType(Integer id, String name) {
        try {
            FilterValidator.checkForMinimumFilters(id, name);
            return new ResponseWrapper(deviceTypeRepository.deleteDeviceType(id, name));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }
}
