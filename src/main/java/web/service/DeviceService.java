package web.service;

import web.domain.entity.Device;
import web.domain.response.ResponseWrapper;

public interface DeviceService {

    ResponseWrapper getDevices(Integer id, String name, Integer deviceTypeId, Integer deviceGroupId,
                               Integer configurationId, String authenticationKey);

    ResponseWrapper addDevice(Device device);

    ResponseWrapper updateDevice(Integer id, String name, String authenticationKey, Device device);
}
