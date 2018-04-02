package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import web.domain.entity.Device;
import web.domain.entity.DeviceGroup;
import web.domain.response.ErrorCode;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.exception.ExceptionWrapper;
import web.mapper.DeviceGroupMapper;
import web.repository.DeviceGroupRepository;
import web.repository.DeviceRepository;
import web.service.DeviceService;
import web.validators.FilterValidator;

import java.util.Collection;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.DeviceMapper.mapToCollection;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceGroupRepository deviceGroupRepository;

    DeviceServiceImpl(DeviceRepository deviceRepository, DeviceGroupRepository deviceGroupRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceGroupRepository = deviceGroupRepository;
    }

    @Override
    public ResponseWrapper getDevices(Integer id, String name, Integer deviceTypeId, Integer deviceGroupId,
                                      Integer configurationId, String authenticationKey) {
        try {
            Collection<Device> devices = deviceRepository.getDevices(
                id, name, deviceTypeId, deviceGroupId, configurationId, authenticationKey);

            if(CollectionUtils.isEmpty(devices)) {
                throwNotFoundException(String.format(
                    "[id: %d, name: %s, deviceTypeId: %s, deviceGroupId: %s, configurationId: %s, authenticationKey: %s]",
                    id, name, deviceTypeId, deviceGroupId, configurationId, authenticationKey));
            }

            return new ResponseWrapper(mapToCollection(devices));
        } catch(Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get devices failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addDevice(Device device) {
        try {
            Device addedDevice = deviceRepository.addDevice(device);

            return new ResponseWrapper(mapToCollection(addedDevice));
        } catch(Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add device failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper updateDevice(Integer id, String name, String authenticationKey, Device device) {
        try {
            FilterValidator.checkForMinimumFilters(id, name, authenticationKey);
            validateDeviceExists(id, name, authenticationKey);

            Device updatedDevice = deviceRepository.updateDevice(id, name, authenticationKey, device);

            // TODO, fix commit calls during single stored procedure. Currently update procedures return old item - not the updated one
            updatedDevice.setName(device.getName());
            updatedDevice.setConfigurationId(device.getConfigurationId());
            updatedDevice.setDeviceGroupId(device.getDeviceGroupId());
            updatedDevice.setDeviceTypeId(device.getDeviceTypeId());

            return new ResponseWrapper(mapToCollection(updatedDevice));
        } catch(Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Update device failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteDevice(Integer id, String name, String authenticationKey) {
        try {
            FilterValidator.checkForMinimumFilters(id, name, authenticationKey);
            validateDeviceExists(id, name, authenticationKey);

            Boolean deleteSuccessful = deviceRepository.deleteDevice(id, name, authenticationKey);

            if (!deleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete device failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper getDevicesGroup(Integer id) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            validateDeviceExists(id, null, null);

            // Device Group ID must be set
            Integer deviceGroupId = getDevice(id).getDeviceGroupId();
            if(deviceGroupId == null) {
                throw new ExceptionWrapper("Get device's group failed", "Device Group ID is null", ErrorCode.NO_ITEMS_FOUND);
            }

            Collection<DeviceGroup> deviceGroups = deviceGroupRepository.getDeviceGroups(deviceGroupId, null);
            if(CollectionUtils.isEmpty(deviceGroups) || !deviceGroups.stream().findFirst().isPresent()) {
                throwNotFoundException(String.format("[deviceGroupId: %d]", id));
            }

            DeviceGroup devicegroup = deviceGroups.stream().findFirst().get();

            return new ResponseWrapper(DeviceGroupMapper.mapToCollection(devicegroup));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device's group failed");
        }

        return null;
    }

    @Override
    public void validateDeviceExists(Integer id, String name, String authenticationKey) throws NotFoundException {
        Collection<Device> devices = deviceRepository.getDevices(id, name, null, null, null, authenticationKey);

        if(CollectionUtils.isEmpty(devices)) {
            throwNotFoundException(String.format("[id: %d, name: %s, authenticationKey: %s]", id, name, authenticationKey));
        }
    }

    /**
     * Get device by ID
     */
    private Device getDevice(Integer id) throws Exception {
        Collection<Device> devices = deviceRepository.getDevices(id, null, null, null, null, null);

        if(CollectionUtils.isEmpty(devices) || !devices.stream().findFirst().isPresent()) {
            throwNotFoundException(String.format("[id: %d]", id));
        }

        return devices.stream()
                .findFirst()
                .get();
    }
}