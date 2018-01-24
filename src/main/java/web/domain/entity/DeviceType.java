package web.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "device_type")
public class DeviceType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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
