package web.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "get_device_types", procedureName = "get_device_types", resultClasses = DeviceType.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_device_icon_id", type = Integer.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "add_device_type", procedureName = "add_device_type", resultClasses = DeviceType.class,
        parameters = {
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_device_icon_id", type = Integer.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "update_device_type", procedureName = "update_device_type", resultClasses = DeviceType.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_device_icon_id", type = Integer.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "delete_device_type", procedureName = "delete_device_type",
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN)
        })
})

@Entity
@Table(name = "device_type")
public class DeviceType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9_ .,-]{1,50}")
    private String name;

    private Integer deviceIconId;

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

    public Integer getDeviceIconId() {
        return deviceIconId;
    }

    public void setDeviceIconId(Integer deviceIconId) {
        this.deviceIconId = deviceIconId;
    }
}
