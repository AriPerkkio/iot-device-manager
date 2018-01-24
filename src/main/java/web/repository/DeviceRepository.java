package web.repository;

import web.domain.entity.Device;

import java.util.Collection;

public interface DeviceRepository {

    Collection<Device> getDevices(Integer deviceTypeId, Integer deviceGroupId, Integer configurationId);

    Device getDevice(String name, String authenticationKey);

    Device addDevice(Device device);

}
