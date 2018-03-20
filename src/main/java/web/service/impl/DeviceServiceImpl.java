package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.Device;
import web.domain.response.ErrorWrapper;
import web.domain.response.ResponseWrapper;
import web.repository.DeviceRepository;
import web.service.DeviceService;
import web.validators.FilterValidator;

import static web.domain.response.ErrorCode.INTERNAL_ERROR;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public ResponseWrapper getDevices(Integer id, String name, Integer deviceTypeId, Integer deviceGroupId,
                                      Integer configurationId, String authenticationKey) {
        try {
            return new ResponseWrapper(deviceRepository.getDevices(id, name, deviceTypeId, deviceGroupId, configurationId,
                    authenticationKey));
        } catch(Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper addDevice(Device device) {
        try {
            return new ResponseWrapper(deviceRepository.addDevice(device));
        } catch(Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper updateDevice(Integer id, String name, String authenticationKey, Device device) {
        try {
            FilterValidator.checkForMinimumFilters(id, name, authenticationKey);
            return new ResponseWrapper(deviceRepository.updateDevice(id, name, authenticationKey, device));
        } catch(Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper deleteDevice(Integer id, String name, String authenticationKey) {
        try {
            FilterValidator.checkForMinimumFilters(id, name, authenticationKey);
            return new ResponseWrapper(deviceRepository.deleteDevice(id, name, authenticationKey));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }
}