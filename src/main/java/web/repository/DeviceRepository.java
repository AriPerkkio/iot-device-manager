package web.repository;

import web.domain.Device;

import java.util.Collection;

public interface DeviceRepository {

    // TODO add rest of the filters
    Collection<Device> getDevices(String name);

    Device addDevice(Device device);

}
