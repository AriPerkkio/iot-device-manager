package web.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "get_device_icons", procedureName = "get_device_icons", resultClasses = DeviceIcon.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "add_device_icon", procedureName = "add_device_icon", resultClasses = DeviceIcon.class,
        parameters = {
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "update_device_icon", procedureName = "update_device_icon", resultClasses = DeviceIcon.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "delete_device_icon", procedureName = "delete_device_icon",
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN)
        })
})

@Entity
@Table(name = "device_icon")
public class DeviceIcon {

    public DeviceIcon() {
        // Default constructor
    }

    public DeviceIcon(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

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
}
