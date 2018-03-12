package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.DeviceGroup;
import web.domain.response.ResponseWrapper;
import web.repository.DeviceGroupRepository;
import web.service.DeviceGroupService;

import java.util.Collections;

@Service
public class DeviceGroupServiceImpl implements DeviceGroupService {

    final DeviceGroupRepository deviceGroupRepository;

    DeviceGroupServiceImpl(DeviceGroupRepository deviceGroupRepository) {
        this.deviceGroupRepository = deviceGroupRepository;
    }

    @Override
    public ResponseWrapper getDeviceGroups(Integer id, String name) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceGroupRepository.getDeviceGroups(id, name));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper addDeviceGroup(DeviceGroup deviceGroup) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceGroupRepository.addDeviceGroup(deviceGroup));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper updateDeviceGroup(Integer id, String name, DeviceGroup deviceGroup) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceGroupRepository.updateDeviceGroup(id, name, deviceGroup));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper deleteDeviceGroup(Integer id, String name) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(deviceGroupRepository.deleteDeviceGroup(id, name));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }
}
