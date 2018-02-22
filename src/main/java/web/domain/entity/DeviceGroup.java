package web.domain.entity;

import javax.persistence.*;

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "get_device_groups", procedureName = "get_device_groups", resultClasses = DeviceGroup.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "add_device_group", procedureName = "add_device_group", resultClasses = DeviceGroup.class,
        parameters = {
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_description", type = String.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "update_device_group", procedureName = "update_device_group", resultClasses = DeviceGroup.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_description", type = String.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "delete_device_group", procedureName = "delete_device_group",
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN)
        })
})


@Entity
@Table(name = "device_group")
public class DeviceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
