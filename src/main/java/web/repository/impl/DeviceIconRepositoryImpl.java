package web.repository.impl;

import org.springframework.stereotype.Repository;
import web.domain.entity.DeviceIcon;
import web.repository.DeviceIconRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DeviceIconRepositoryImpl implements DeviceIconRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DeviceIcon addDeviceIcon(String path) {
        // TODO
        return new DeviceIcon(1, path);
    }

    @Override
    public boolean deviceIconExists(String path) {
        // TODO
        return false;
    }
}
