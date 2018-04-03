package web.domain.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "get_measurements", procedureName = "get_measurements", resultClasses = Measurement.class,
        parameters = {
            @StoredProcedureParameter(name = "f_device_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_exact_time", type = Date.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_start_time", type = Date.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_end_time", type = Date.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "add_measurement", procedureName = "add_measurement", resultClasses = Measurement.class,
        parameters = {
            @StoredProcedureParameter(name = "p_device_id", type = Integer.class, mode = ParameterMode.IN),
            // JSON inserted as String
            @StoredProcedureParameter(name = "p_content", type = String.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_time", type = Date.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "delete_measurements", procedureName = "delete_measurements",
        parameters = {
            @StoredProcedureParameter(name = "f_device_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_exact_time", type = Date.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_start_time", type = Date.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_end_time", type = Date.class, mode = ParameterMode.IN)
        })
})

@Entity
@Table(name = "measurement")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Measurement {
    @Id
    private Integer id;

    private Integer deviceId;

    // HashMap used to represent MySQL JSON column
    @Type(type = "json")
    @Column(columnDefinition = "json")
    @NotNull
    private HashMap content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public HashMap getContent() {
        return content;
    }

    public void setContent(HashMap content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Measurement that = (Measurement) o;

        return new EqualsBuilder()
            .append(deviceId, that.deviceId)
            .append(content, that.content)
            .append(time, that.time)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(deviceId)
            .append(content)
            .append(time)
            .toHashCode();
    }
}
