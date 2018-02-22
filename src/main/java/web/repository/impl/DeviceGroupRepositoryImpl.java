package web.repository.impl;

import org.springframework.stereotype.Repository;
import web.domain.entity.DeviceGroup;
import web.repository.DeviceGroupRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.Collection;

@Repository
public class DeviceGroupRepositoryImpl implements DeviceGroupRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<DeviceGroup> getDeviceGroups(Integer id, String name){
        StoredProcedureQuery getDeviceGroupsQuery =
            entityManager.createNamedStoredProcedureQuery("get_device_groups")
                .setParameter("f_id", id)
                .setParameter("f_name", name);

        return getDeviceGroupsQuery.getResultList();
    }

    @Override
    public DeviceGroup addDeviceGroup(DeviceGroup deviceGroup){
        StoredProcedureQuery addDeviceGroupQuery =
            entityManager.createNamedStoredProcedureQuery("add_device_group")
                .setParameter("p_name", deviceGroup.getName())
                .setParameter("p_description", deviceGroup.getDescription());

        return (DeviceGroup) addDeviceGroupQuery.getSingleResult();
    }

    @Override
    public DeviceGroup updateDeviceGroup(Integer id, String name, DeviceGroup deviceGroup){
        StoredProcedureQuery updateDeviceGroupQuery =
            entityManager.createNamedStoredProcedureQuery("update_device_group")
                .setParameter("f_id", id)
                .setParameter("f_name", name)
                .setParameter("p_name", deviceGroup.getName())
                .setParameter("p_description", deviceGroup.getDescription());

        return (DeviceGroup) updateDeviceGroupQuery.getSingleResult();
    }

    @Override
    public Boolean deleteDeviceGroup(Integer id, String name){
        StoredProcedureQuery deleteDeviceQuery =
            entityManager.createNamedStoredProcedureQuery("delete_device_group")
                .setParameter("f_id", id)
                .setParameter("f_name", name);

        return BigInteger.ONE.equals(deleteDeviceQuery.getSingleResult());
    }
}
