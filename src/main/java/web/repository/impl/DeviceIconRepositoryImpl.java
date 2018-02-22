package web.repository.impl;

import org.springframework.stereotype.Repository;
import web.domain.entity.DeviceIcon;
import web.repository.DeviceIconRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.Collection;

@Repository
public class DeviceIconRepositoryImpl implements DeviceIconRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<DeviceIcon> getDeviceIcons(Integer id, String name) {
        StoredProcedureQuery getDeviceIconsQuery =
            entityManager.createNamedStoredProcedureQuery("get_device_icons")
                .setParameter("f_id", id)
                .setParameter("f_name", name);

        return getDeviceIconsQuery.getResultList();
    }

    @Override
    public DeviceIcon addDeviceIcon(DeviceIcon deviceIcon) {
        StoredProcedureQuery addDeviceIconQuery =
            entityManager.createNamedStoredProcedureQuery("add_device_icon")
                .setParameter("p_name", deviceIcon.getName());

        return (DeviceIcon) addDeviceIconQuery.getSingleResult();
    }

    @Override
    public DeviceIcon updateDeviceIcon(Integer id, String name, DeviceIcon deviceIcon) {
        StoredProcedureQuery updateDeviceQuery =
            entityManager.createNamedStoredProcedureQuery("update_device_icon")
                .setParameter("f_id", id)
                .setParameter("f_name", name)
                .setParameter("p_name", deviceIcon.getName());

        return (DeviceIcon) updateDeviceQuery.getSingleResult();
    }

    @Override
    public Boolean deleteDeviceIcon(Integer id, String name) {
        StoredProcedureQuery deleteDeviceIconQuery =
            entityManager.createNamedStoredProcedureQuery("delete_device_icon")
                .setParameter("f_id", id)
                .setParameter("f_name", name);

        return BigInteger.ONE.equals(deleteDeviceIconQuery.getSingleResult());
    }
}
