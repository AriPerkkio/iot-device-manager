package web.domain.response;

import web.domain.entity.*;

public class DeviceResponse {

    private String name;

    private String authenticationKey;

    private Configuration configuration;

    private DeviceGroup deviceGroup;

    private DeviceIcon deviceIcon;

    private DeviceTypeResponse deviceType;

    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }



    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public DeviceGroup getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(DeviceGroup deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public DeviceIcon getDeviceIcon() {
        return deviceIcon;
    }

    public void setDeviceIcon(DeviceIcon deviceIcon) {
        this.deviceIcon = deviceIcon;
    }

    public DeviceTypeResponse getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeResponse deviceType) {
        this.deviceType = deviceType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}