package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.DeviceGroup;
import web.domain.response.ErrorWrapper;
import web.domain.response.ResponseWrapper;
import web.repository.DeviceGroupRepository;
import web.service.DeviceGroupService;

import static web.domain.response.ErrorCode.INTERNAL_ERROR;

@Service
public class DeviceGroupServiceImpl implements DeviceGroupService {

    final DeviceGroupRepository deviceGroupRepository;

    DeviceGroupServiceImpl(DeviceGroupRepository deviceGroupRepository) {
        this.deviceGroupRepository = deviceGroupRepository;
    }

    @Override
    public ResponseWrapper getDeviceGroups(Integer id, String name) {
        try {
            return new ResponseWrapper(deviceGroupRepository.getDeviceGroups(id, name));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper addDeviceGroup(DeviceGroup deviceGroup) {
        try {
            return new ResponseWrapper(deviceGroupRepository.addDeviceGroup(deviceGroup));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper updateDeviceGroup(Integer id, String name, DeviceGroup deviceGroup) {
        try {
            return new ResponseWrapper(deviceGroupRepository.updateDeviceGroup(id, name, deviceGroup));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper deleteDeviceGroup(Integer id, String name) {
        try {
            return new ResponseWrapper(deviceGroupRepository.deleteDeviceGroup(id, name));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }
}
