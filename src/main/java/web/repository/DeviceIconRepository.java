package web.repository;

import web.domain.entity.DeviceIcon;

public interface DeviceIconRepository {

    DeviceIcon addDeviceIcon(String name);

    boolean deviceIconExists(String name);
}
