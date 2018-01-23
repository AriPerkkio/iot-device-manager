package web.repository.impl;

import org.springframework.stereotype.Repository;
import web.domain.Device;
import web.repository.DeviceRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.Collection;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Device> getDevices(String filterName) {
        StoredProcedureQuery getDeviceQuery =
            entityManager.createNamedStoredProcedureQuery("get_devices")
                .setParameter("f_name", filterName);

        return getDeviceQuery.getResultList();
    }

    @Override
    public Device addDevice(Device device) {
        StoredProcedureQuery addDeviceQuery =
            entityManager.createNamedStoredProcedureQuery("add_device")
                .setParameter("p_name", device.getName())
                .setParameter("p_device_type_id", device.getDeviceTypeId())
                .setParameter("p_device_group_id", device.getDeviceGroupId())
                .setParameter("p_configuration_id", device.getConfigurationId());

        return (Device) addDeviceQuery.getSingleResult();
    }
}
