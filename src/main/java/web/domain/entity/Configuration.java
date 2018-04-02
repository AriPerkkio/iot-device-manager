package web.domain.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashMap;

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "get_configurations", procedureName = "get_configurations", resultClasses = Configuration.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "add_configuration", procedureName = "add_configuration", resultClasses = Configuration.class,
        parameters = {
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_description", type = String.class, mode = ParameterMode.IN),
            // JSON inserted as String
            @StoredProcedureParameter(name = "p_content", type = String.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "update_configuration", procedureName = "update_configuration", resultClasses = Configuration.class,
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_name", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_description", type = String.class, mode = ParameterMode.IN),
            // JSON inserted as String
            @StoredProcedureParameter(name = "p_content", type = String.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "delete_configuration", procedureName = "delete_configuration",
        parameters = {
            @StoredProcedureParameter(name = "f_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_name", type = String.class, mode = ParameterMode.IN)
        })
})

@Entity
@Table(name = "configuration")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9_ .,-]{1,50}")
    private String name;

    @Pattern(regexp = "[A-Za-z0-9_ .,-]{1,100}")
    private String description;

    // HashMap used to represent MySQL JSON column
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private HashMap content;

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

    public HashMap getContent() {
        return content;
    }

    public void setContent(HashMap content) {
        this.content = content;
    }
}
