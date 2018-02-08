package web.domain.entity;

import javax.persistence.*;

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
