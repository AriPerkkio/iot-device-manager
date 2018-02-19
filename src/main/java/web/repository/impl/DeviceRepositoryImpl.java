package web.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web.domain.entity.Device;
import web.repository.DeviceRepository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Device> getDevices(Integer id, String name, Integer deviceTypeId, Integer deviceGroupId,
                                         Integer configurationId, String authenticationKey) {
        StoredProcedureQuery getDevicesQuery =
            entityManager.createNamedStoredProcedureQuery("get_devices")
                .setParameter("f_id", id)
                .setParameter("f_name", name)
                .setParameter("f_device_type_id", deviceTypeId)
                .setParameter("f_device_group_id", deviceGroupId)
                .setParameter("f_configuration_id", configurationId)
                .setParameter("f_authentication_key", authenticationKey);

        return getDevicesQuery.getResultList();
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

    @Override
    public Device updateDevice(Integer id, String name, String authenticationKey, Device device) {
        StoredProcedureQuery updateDeviceQuery =
                entityManager.createNamedStoredProcedureQuery("update_device")
                        .setParameter("f_id", id)
                        .setParameter("f_name", name)
                        .setParameter("f_authentication_key", authenticationKey)
                        .setParameter("p_name", device.getName())
                        .setParameter("p_device_type_id", device.getDeviceTypeId())
                        .setParameter("p_device_group_id", device.getDeviceGroupId())
                        .setParameter("p_configuration_id", device.getConfigurationId());

        return (Device) updateDeviceQuery.getSingleResult();
    }

    @Override
    public Boolean deleteDevice(Integer id, String name, String authenticationKey) {
        StoredProcedureQuery deleteDeviceQuery =
                entityManager.createNamedStoredProcedureQuery("delete_device")
                        .setParameter("f_id", id)
                        .setParameter("f_name", name)
                        .setParameter("f_authentication_key", authenticationKey);

        return BigInteger.ONE.equals(deleteDeviceQuery.getSingleResult());
    }
}
