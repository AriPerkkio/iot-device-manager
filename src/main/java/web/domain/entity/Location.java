package web.domain.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "get_locations", procedureName = "get_locations", resultClasses = Location.class,
        parameters = {
            @StoredProcedureParameter(name = "f_device_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_exact_time", type = Date.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_start_time", type = Date.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_end_time", type = Date.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "add_location", procedureName = "add_location", resultClasses = Location.class,
        parameters = {
            @StoredProcedureParameter(name = "p_device_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_latitude", type = BigDecimal.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_longitude", type = BigDecimal.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "p_time", type = Date.class, mode = ParameterMode.IN)
        }),
    @NamedStoredProcedureQuery(name = "delete_locations", procedureName = "delete_locations",
        parameters = {
            @StoredProcedureParameter(name = "f_device_id", type = Integer.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_exact_time", type = Date.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_start_time", type = Date.class, mode = ParameterMode.IN),
            @StoredProcedureParameter(name = "f_end_time", type = Date.class, mode = ParameterMode.IN)
        })
})
@Entity
@Table(name = "location")
public class Location {
    @Id
    private Integer deviceId;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Date time;

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
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

        Location location = (Location) o;

        return new EqualsBuilder()
            .append(deviceId, location.deviceId)
            .append(latitude, location.latitude)
            .append(longitude, location.longitude)
            .append(time, location.time)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(deviceId)
            .append(latitude)
            .append(longitude)
            .append(time)
            .toHashCode();
    }
}
