package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.domain.Device;

public interface DeviceRepository extends CrudRepository<Device, Long> {

}
