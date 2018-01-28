package web.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "device_icon")
public class DeviceIcon {

    public DeviceIcon() {
        // Default constructor
    }

    public DeviceIcon(Integer id, String path) {
        this.id = id;
        this.path = path;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String path;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
