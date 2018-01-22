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
        final String name = filterName != null ? filterName : "";

        StoredProcedureQuery getDeviceQuery =
            entityManager.createNamedStoredProcedureQuery("get_devices")
                .setParameter("f_name", name);

        return getDeviceQuery.getResultList();
    }

    @Override
    public Device addDevice(Device device) {

        // TODO Util method for getting "default null value" for each data type
        final String name = device.getName();
        final Integer deviceTypeId = device.getDeviceTypeId();
        final Integer deviceGroupId = device.getDeviceGroupId() != null ? device.getDeviceGroupId() : 0;
        final Integer configurationId = device.getConfigurationId() != null ? device.getConfigurationId() : 0;

        StoredProcedureQuery addDeviceQuery =
            entityManager.createNamedStoredProcedureQuery("add_device")
                .setParameter("p_name", name)
                .setParameter("p_device_type_id", deviceTypeId)
                .setParameter("p_device_group_id", deviceGroupId)
                .setParameter("p_configuration_id", configurationId);

        return (Device) addDeviceQuery.getSingleResult();
    }
}
