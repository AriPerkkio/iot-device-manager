package web.domain;

import javax.persistence.*;

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "get_devices", procedureName = "get_devices",
        resultClasses = Device.class,
        parameters = {
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN) }),
    @NamedStoredProcedureQuery(name = "add_device", procedureName = "add_device",
        resultClasses = Device.class,
        parameters = {
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_device_type_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_device_group_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_configuration_id", type = Integer.class, mode = ParameterMode.IN) })
})

@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private Integer deviceTypeId;

    private Integer deviceGroupId;

    private Integer configurationId;

    private String authenticationKey;

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

    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Integer getDeviceGroupId() {
        return deviceGroupId;
    }

    public void setDeviceGroupId(Integer deviceGroupId) {
        this.deviceGroupId = deviceGroupId;
    }

    public Integer getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Integer configurationId) {
        this.configurationId = configurationId;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }
}
