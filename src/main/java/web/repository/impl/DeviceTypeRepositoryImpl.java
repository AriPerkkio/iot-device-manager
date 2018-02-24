package web.repository.impl;

import org.springframework.stereotype.Repository;
import web.domain.entity.DeviceType;
import web.repository.DeviceTypeRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.Collection;

@Repository
public class DeviceTypeRepositoryImpl implements DeviceTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<DeviceType> getDeviceTypes(Integer id, String name, Integer deviceIconId) {
        StoredProcedureQuery getDeviceTypesQuery =
            entityManager.createNamedStoredProcedureQuery("get_device_types")
                .setParameter("f_id", id)
                .setParameter("f_name", name)
                .setParameter("f_device_icon_id", deviceIconId);

        return getDeviceTypesQuery.getResultList();
    }

    @Override
    public DeviceType addDeviceType(DeviceType deviceType) {
        StoredProcedureQuery addDeviceTypeQuery =
            entityManager.createNamedStoredProcedureQuery("add_device_type")
                .setParameter("p_name", deviceType.getName())
                .setParameter("p_device_icon_id", deviceType.getDeviceIconId());

        return (DeviceType) addDeviceTypeQuery.getSingleResult();
    }

    @Override
    public DeviceType updateDeviceType(Integer id, String name, DeviceType deviceType) {
        StoredProcedureQuery updateDeviceTypeQuery =
            entityManager.createNamedStoredProcedureQuery("update_device_type")
                .setParameter("f_id", id)
                .setParameter("f_name", name)
                .setParameter("p_name", deviceType.getName())
                .setParameter("p_device_icon_id", deviceType.getDeviceIconId());

        return (DeviceType) updateDeviceTypeQuery.getSingleResult();
    }

    @Override
    public Boolean deleteDeviceType(Integer id, String name) {
        StoredProcedureQuery deleteDeviceTypeQuery =
            entityManager.createNamedStoredProcedureQuery("delete_device_type")
                .setParameter("f_id", id)
                .setParameter("f_name", name);

        return BigInteger.ONE.equals(deleteDeviceTypeQuery.getSingleResult());
    }
}
