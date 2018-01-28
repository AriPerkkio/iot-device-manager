package web.repository;

import web.domain.entity.DeviceIcon;

public interface DeviceIconRepository {

    DeviceIcon addDeviceIcon(String path);

    boolean deviceIconExists(String path);
}
