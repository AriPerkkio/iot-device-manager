package web.domain.response;

import web.domain.entity.DeviceIcon;

public class DeviceTypeResponse {
    private Integer id;

    private String name;

    private DeviceIcon deviceIcon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceIcon getDeviceIcon() {
        return deviceIcon;
    }

    public void setDeviceIcon(DeviceIcon deviceIcon) {
        this.deviceIcon = deviceIcon;
    }
}
