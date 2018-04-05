package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import web.domain.entity.*;
import web.domain.response.ErrorCode;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.exception.ExceptionWrapper;
import web.mapper.ConfigurationMapper;
import web.mapper.DeviceGroupMapper;
import web.mapper.DeviceTypeMapper;
import web.repository.ConfigurationRepository;
import web.repository.DeviceGroupRepository;
import web.repository.DeviceRepository;
import web.repository.DeviceTypeRepository;
import web.service.DeviceService;
import web.service.DeviceTypeService;
import web.service.LocationService;
import web.service.MeasurementService;
import web.validators.FilterValidator;

import java.util.Collection;
import java.util.Date;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.DeviceMapper.mapToCollection;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceGroupRepository deviceGroupRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceTypeService deviceTypeService;
    private final ConfigurationRepository configurationRepository;
    private final MeasurementService measurementService;
    private final LocationService locationService;

    DeviceServiceImpl(DeviceRepository deviceRepository, DeviceGroupRepository deviceGroupRepository,
                      DeviceTypeRepository deviceTypeRepository, DeviceTypeService deviceTypeService,
                      ConfigurationRepository configurationRepository, MeasurementService measurementService,
                      LocationService locationService) {
        this.deviceRepository = deviceRepository;
        this.deviceGroupRepository = deviceGroupRepository;
        this.deviceTypeRepository = deviceTypeRepository;
        this.deviceTypeService = deviceTypeService;
        this.configurationRepository = configurationRepository;
        this.measurementService = measurementService;
        this.locationService = locationService;
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

            Integer deviceGroupId = getDevice(id).getDeviceGroupId();
            if(deviceGroupId == null) {
                throw new ExceptionWrapper("Get device's group failed", "Device Group ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            Collection<DeviceGroup> deviceGroups = deviceGroupRepository.getDeviceGroups(deviceGroupId, null);
            if(CollectionUtils.isEmpty(deviceGroups) || !deviceGroups.stream().findFirst().isPresent()) {
                throwNotFoundException(String.format("[deviceGroupId: %d]", deviceGroupId));
            }

            DeviceGroup devicegroup = deviceGroups.stream().findFirst().get();

            return new ResponseWrapper(DeviceGroupMapper.mapToCollection(devicegroup));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device's group failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addGroupForDevice(Integer id, DeviceGroup deviceGroup) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getDeviceGroupId() != null) {
                throw new ExceptionWrapper(
                    "Add group for device failed",
                    String.format("Device belongs to group %d already. Modify device's group ID to change its group.", device.getDeviceGroupId()),
                    ErrorCode.PARAMETER_CONFLICT);
            }

            // Add new group and use its ID for device's groupId
            DeviceGroup addedDeviceGroup = deviceGroupRepository.addDeviceGroup(deviceGroup);
            device.setDeviceGroupId(addedDeviceGroup.getId());
            deviceRepository.updateDevice(id, null, null, device);

            return new ResponseWrapper(DeviceGroupMapper.mapToCollection(addedDeviceGroup));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add group for device failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper updateDevicesGroup(Integer id, DeviceGroup deviceGroup) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getDeviceGroupId() == null) {
                throw new ExceptionWrapper(
                    "Update device's group failed",
                    "Device Group ID is null",
                    ErrorCode.PARAMETER_CONFLICT);
            }

            DeviceGroup updatedDeviceGroup = deviceGroupRepository.updateDeviceGroup(device.getDeviceGroupId(), null, deviceGroup);

            return new ResponseWrapper(DeviceGroupMapper.mapToCollection(updatedDeviceGroup));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Update device's group failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteDevicesGroup(Integer id) {
        try{
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getDeviceGroupId() == null) {
                throw new ExceptionWrapper("Delete device's group failed", "Device Group ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            Boolean deleteSuccessful = deviceGroupRepository.deleteDeviceGroup(device.getDeviceGroupId(), null);

            if(!deleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete device's group failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper getDevicesType(Integer id) {
        try {
            FilterValidator.checkForMinimumFilters(id);

            Integer deviceTypeId = getDevice(id).getDeviceTypeId();
            if(deviceTypeId == null) {
                throw new ExceptionWrapper("Get device's type failed", "Device Type ID is null", ErrorCode.NO_ITEMS_FOUND);
            }

            Collection<DeviceType> deviceTypes = deviceTypeRepository.getDeviceTypes(deviceTypeId, null, null);
            if(CollectionUtils.isEmpty(deviceTypes) || !deviceTypes.stream().findFirst().isPresent()) {
                throwNotFoundException(String.format("[deviceTypeId: %d]", deviceTypeId));
            }

            DeviceType deviceType = deviceTypes.stream().findFirst().get();

            return new ResponseWrapper(DeviceTypeMapper.mapToCollection(deviceType));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device's type failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addTypeForDevice(Integer id, DeviceType deviceType) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getDeviceTypeId() != null) {
                throw new ExceptionWrapper(
                    "Add type for device failed",
                    String.format("Device belongs to type %d already. Modify device's type ID to change its type.", device.getDeviceTypeId()),
                    ErrorCode.PARAMETER_CONFLICT);
            }

            // Add new type and use its ID for device's typeId
            DeviceType addedDeviceType = deviceTypeRepository.addDeviceType(deviceType);
            device.setDeviceTypeId(addedDeviceType.getId());
            deviceRepository.updateDevice(id, null, null, device);

            return new ResponseWrapper(DeviceTypeMapper.mapToCollection(addedDeviceType));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add type for device failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper updateDevicesType(Integer id, DeviceType deviceType) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getDeviceTypeId() == null) {
                throw new ExceptionWrapper(
                    "Update device's type failed",
                    "Device Type ID is null",
                    ErrorCode.PARAMETER_CONFLICT);
            }

            DeviceType updatedDeviceType = deviceTypeRepository.updateDeviceType(device.getDeviceTypeId(), null, deviceType);

            return new ResponseWrapper(DeviceTypeMapper.mapToCollection(updatedDeviceType));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Update device's type failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteDevicesType(Integer id) {
        try{
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getDeviceTypeId() == null) {
                throw new ExceptionWrapper("Delete device's type failed", "Device Type ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            Boolean deleteSuccessful = deviceTypeRepository.deleteDeviceType(device.getDeviceTypeId(), null);

            if(!deleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete device's type failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper getDevicesIconInformation(Integer id) {
        try{
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getDeviceTypeId() == null) {
                throw new ExceptionWrapper("Get device's icon failed", "Device Type ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            return deviceTypeService.getTypesIcon(device.getDeviceTypeId());
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device's icon failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper renameDevicesIcon(Integer id, DeviceIcon deviceIcon) {
        try{
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getDeviceTypeId() == null) {
                throw new ExceptionWrapper("Rename device's icon failed", "Device Type ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            return deviceTypeService.renameTypesIcon(device.getDeviceTypeId(), deviceIcon);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Rename device's icon failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteDevicesIcon(Integer id) {
        try{
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getDeviceTypeId() == null) {
                throw new ExceptionWrapper("Delete device's icon failed", "Device Type ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            return deviceTypeService.deleteTypesIcon(device.getDeviceTypeId());
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete device's icon failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper getDevicesConfiguration(Integer id) {
        try {
            FilterValidator.checkForMinimumFilters(id);

            Integer configurationId = getDevice(id).getConfigurationId();
            if (configurationId == null) {
                throw new ExceptionWrapper("Get device's configuration failed", "Device Configuration ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            Collection<Configuration> configurations = configurationRepository.getConfigurations(configurationId, null);
            if (CollectionUtils.isEmpty(configurations) || !configurations.stream().findFirst().isPresent()) {
                throwNotFoundException(String.format("[configurationId: %d]", configurationId));
            }

            Configuration configuration = configurations.stream().findFirst().get();

            return new ResponseWrapper(ConfigurationMapper.mapToCollection(configuration));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device's configuration failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addConfigurationForDevice(Integer id, Configuration configuration) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getConfigurationId() != null) {
                throw new ExceptionWrapper(
                    "Add configuration for device failed",
                    String.format("Configuration %d is already set for device. Modify device's configuration ID to change its configuration.", device.getConfigurationId()),
                    ErrorCode.PARAMETER_CONFLICT);
            }

            // Add new configuration and use its ID for device's configurationId
            Configuration addedConfiguration = configurationRepository.addConfiguration(configuration);
            device.setConfigurationId(addedConfiguration.getId());
            deviceRepository.updateDevice(id, null, null, device);

            return new ResponseWrapper(ConfigurationMapper.mapToCollection(addedConfiguration));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add configuration for device failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper updateDevicesConfiguration(Integer id, Configuration configuration) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            Device device = getDevice(id);

            if(device.getConfigurationId() == null) {
                throw new ExceptionWrapper(
                    "Update device's configuration failed",
                    "Configuration ID is null",
                    ErrorCode.PARAMETER_CONFLICT);
            }

            Configuration updatedConfiguration = configurationRepository.updateConfiguration(device.getConfigurationId(), null, configuration);

            return new ResponseWrapper(ConfigurationMapper.mapToCollection(updatedConfiguration));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Update device's configuration failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteDevicesConfiguration(Integer id) {
        try {
            FilterValidator.checkForMinimumFilters(id);

            Integer configurationId = getDevice(id).getConfigurationId();
            if (configurationId == null) {
                throw new ExceptionWrapper("Delete device's configuration failed", "Device Configuration ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            Boolean deleteSuccessful = configurationRepository.deleteConfiguration(configurationId, null);

            if(!deleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete device's configuration failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper getDevicesMeasurements(Integer id, Date exactTime, Date startTime, Date endTime) {
        return measurementService.getMeasurements(id, exactTime, startTime, endTime);
    }

    @Override
    public ResponseWrapper addMeasurementForDevice(Integer id, Measurement measurement) {
        measurement.setDeviceId(id);
        return measurementService.addMeasurement(measurement);
    }

    @Override
    public ResponseWrapper deleteDevicesMeasurements(Integer id, Date exactTime, Date startTime, Date endTime) {
        return measurementService.deleteMeasurements(id, exactTime, startTime, endTime);
    }

    @Override
    public ResponseWrapper getDevicesLocations(Integer id, Date exactTime, Date startTime, Date endTime) {
        return locationService.getLocations(id, exactTime, startTime, endTime);
    }

    @Override
    public ResponseWrapper addLocationForDevice(Integer id, Location location) {
        location.setDeviceId(id);
        return locationService.addLocation(location);
    }

    @Override
    public ResponseWrapper deleteDevicesLocations(Integer id, Date exactTime, Date startTime, Date endTime) {
        return locationService.deleteLocations(id, exactTime, startTime, endTime);
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