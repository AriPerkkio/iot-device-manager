package web.repository.impl;

import org.springframework.stereotype.Repository;
import web.domain.entity.Device;
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
    public Collection<Device> getDevices(Integer deviceTypeId, Integer deviceGroupId, Integer configurationId) {
        StoredProcedureQuery getDevicesQuery =
            entityManager.createNamedStoredProcedureQuery("get_devices")
                .setParameter("f_name", null)
                .setParameter("f_device_type_id", deviceTypeId)
                .setParameter("f_device_group_id", deviceGroupId)
                .setParameter("f_configuration_id", configurationId)
                .setParameter("f_authentication_key", null);

        return getDevicesQuery.getResultList();
    }

    @Override
    public Device getDevice(String name, String authenticationKey) {

        if(name == null && authenticationKey == null) {
            throw new IllegalArgumentException("Either name or authenticationKey required.");
        }

        StoredProcedureQuery getDeviceQuery =
                entityManager.createNamedStoredProcedureQuery("get_devices")
                        .setParameter("f_name", name)
                        .setParameter("f_device_type_id", null)
                        .setParameter("f_device_group_id", null)
                        .setParameter("f_configuration_id", null)
                        .setParameter("f_authentication_key", authenticationKey);

        return (Device) getDeviceQuery.getSingleResult();
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
