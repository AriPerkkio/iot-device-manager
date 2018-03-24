package web.domain.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "get_devices", procedureName = "get_devices", resultClasses = Device.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_device_type_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_device_group_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_configuration_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_authentication_key", type = String.class, mode = ParameterMode.IN)
    }),
    @NamedStoredProcedureQuery(name = "add_device", procedureName = "add_device", resultClasses = Device.class,
        parameters = {
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_device_type_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_device_group_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_configuration_id", type = Integer.class, mode = ParameterMode.IN)
    }),
    @NamedStoredProcedureQuery(name = "update_device", procedureName = "update_device", resultClasses = Device.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_authentication_key", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_device_type_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_device_group_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_configuration_id", type = Integer.class, mode = ParameterMode.IN)
    }),
    @NamedStoredProcedureQuery(name = "delete_device", procedureName = "delete_device",
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_authentication_key", type = String.class, mode = ParameterMode.IN)
    })
})

@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("deviceTypeId", deviceTypeId)
                .append("deviceGroupId", deviceGroupId)
                .append("configurationId", configurationId)
                .append("authenticationKey", authenticationKey)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        return new EqualsBuilder()
                .append(id, device.id)
                .append(name, device.name)
                .append(deviceTypeId, device.deviceTypeId)
                .append(deviceGroupId, device.deviceGroupId)
                .append(configurationId, device.configurationId)
                .append(authenticationKey, device.authenticationKey)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(deviceTypeId)
                .append(deviceGroupId)
                .append(configurationId)
                .append(authenticationKey)
                .toHashCode();
    }
}
