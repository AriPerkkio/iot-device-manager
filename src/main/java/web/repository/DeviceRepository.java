package web.repository;

import web.domain.entity.Device;

import java.util.Collection;

public interface DeviceRepository {

    Collection<Device> getDevices(Integer id, String name, Integer deviceTypeId, Integer deviceGroupId,
                                  Integer configurationId, String authenticationKey);

    Device addDevice(Device device);

}
