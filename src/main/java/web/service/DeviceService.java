package web.service;

import web.domain.entity.Device;
import web.domain.response.ResponseWrapper;

public interface DeviceService {

    ResponseWrapper getDevices(Integer deviceTypeId, Integer deviceGroupId, Integer configurationId);

    ResponseWrapper getDevice(String name, String authenticationKey);

    ResponseWrapper addDevice(Device device);
}
