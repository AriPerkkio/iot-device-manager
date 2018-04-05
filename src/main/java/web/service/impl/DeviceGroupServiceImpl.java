package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import web.domain.entity.Device;
import web.domain.entity.DeviceGroup;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.mapper.DeviceMapper;
import web.repository.DeviceGroupRepository;
import web.repository.DeviceRepository;
import web.service.DeviceGroupService;
import web.validators.FilterValidator;

import java.util.Collection;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.DeviceGroupMapper.mapToCollection;

@Service
public class DeviceGroupServiceImpl implements DeviceGroupService {

    final DeviceGroupRepository deviceGroupRepository;
    final DeviceRepository deviceRepository;

    DeviceGroupServiceImpl(DeviceGroupRepository deviceGroupRepository, DeviceRepository deviceRepository) {
        this.deviceGroupRepository = deviceGroupRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public ResponseWrapper getDeviceGroups(Integer id, String name) {
        try {
            Collection<DeviceGroup> deviceGroups = deviceGroupRepository.getDeviceGroups(id, name);

            if(CollectionUtils.isEmpty(deviceGroups)) {
                throwNotFoundException(String.format("[id: %d, name: %s]",id, name));
            }

            return new ResponseWrapper(mapToCollection(deviceGroups));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device groups failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addDeviceGroup(DeviceGroup deviceGroup) {
        try {
            DeviceGroup addedDeviceGroup = deviceGroupRepository.addDeviceGroup(deviceGroup);

            return new ResponseWrapper(mapToCollection(addedDeviceGroup));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add device group failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper updateDeviceGroup(Integer id, String name, DeviceGroup deviceGroup) {
        try {
            FilterValidator.checkForMinimumFilters(id, name);
            validateGroupExists(id, name);

            DeviceGroup updatedDeviceGroup = deviceGroupRepository.updateDeviceGroup(id, name, deviceGroup);

            // TODO, fix commit calls during single stored procedure. Currently update procedures return old item - not the updated one
            updatedDeviceGroup.setName(deviceGroup.getName());
            updatedDeviceGroup.setDescription(deviceGroup.getDescription());

            return new ResponseWrapper(mapToCollection(updatedDeviceGroup));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Update device group failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteDeviceGroup(Integer id, String name) {
        try {
            FilterValidator.checkForMinimumFilters(id, name);
            validateGroupExists(id, name);

            Boolean deleteSuccessful = deviceGroupRepository.deleteDeviceGroup(id, name);

            if(!deleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Update device group failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper getGroupsDevices(Integer id) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            validateGroupExists(id, null);
            Collection<Device> devices = deviceRepository.getDevices(null, null, null, id, null, null);

            if(CollectionUtils.isEmpty(devices)) {
                throwNotFoundException(String.format("[deviceGroupId: %d]",id));
            }

            return new ResponseWrapper(DeviceMapper.mapToCollection(devices));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get group's devices failed");
        }
        return null;
    }

    @Override
    public ResponseWrapper addDeviceToGroup(Integer id, Device device) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            validateGroupExists(id, null);

            device.setDeviceGroupId(id);
            Device addedDevice = deviceRepository.addDevice(device);

            return new ResponseWrapper(DeviceMapper.mapToCollection(addedDevice));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add device to group failed");
        }
        return null;
    }

    /**
     * Validate a group matching given parameters exists
     *
     * @param id
     *      Device Group ID used as filter
     * @param name
     *      Device Group name used as filter
     * @throws NotFoundException
     *      Exception thrown when group matching given parameters was not found
     */
    private void validateGroupExists(Integer id, String name) throws NotFoundException {
        Collection <DeviceGroup> deviceGroups = deviceGroupRepository.getDeviceGroups(id, name);

        if (CollectionUtils.isEmpty(deviceGroups)) {
            throwNotFoundException(String.format("[id: %d, name: %s]", id, name));
        }
    }
}
