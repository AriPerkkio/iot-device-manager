package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import web.domain.entity.Device;
import web.domain.entity.DeviceIcon;
import web.domain.entity.DeviceType;
import web.domain.response.ErrorCode;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.exception.ExceptionWrapper;
import web.repository.DeviceTypeRepository;
import web.service.DeviceIconService;
import web.service.DeviceService;
import web.service.DeviceTypeService;
import web.validators.FilterValidator;

import java.util.Collection;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.DeviceTypeMapper.mapToCollection;

@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceIconService deviceIconService;
    private final DeviceService deviceService;

    DeviceTypeServiceImpl(DeviceTypeRepository deviceTypeRepository, DeviceIconService deviceIconService,
                          @Lazy DeviceService deviceService) {
        this.deviceTypeRepository = deviceTypeRepository;
        this.deviceIconService = deviceIconService;
        this.deviceService = deviceService;
    }

    @Override
    public ResponseWrapper getDeviceTypes(Integer id, String name, Integer deviceIconId) {
        try {
            Collection <DeviceType> deviceTypes = deviceTypeRepository.getDeviceTypes(id, name, deviceIconId);

            if(CollectionUtils.isEmpty(deviceTypes)) {
                throwNotFoundException(String.format("[id: %d, name: %s, deviceIconId: %d]",id, name, deviceIconId));
            }

            return new ResponseWrapper(mapToCollection(deviceTypes));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device types failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addDeviceType(DeviceType deviceType) {
        try {
            DeviceType addedDeviceType = deviceTypeRepository.addDeviceType(deviceType);

            return new ResponseWrapper(mapToCollection(addedDeviceType));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add device type failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper updateDeviceType(Integer id, String name, DeviceType deviceType) {
        try {
            FilterValidator.checkForMinimumFilters(id, name);
            validateTypeExists(id, name);

            DeviceType updatedDeviceType = deviceTypeRepository.updateDeviceType(id, name, deviceType);

            // TODO, fix commit calls during single stored procedure. Currently update procedures return old item - not the updated one
            updatedDeviceType.setName(deviceType.getName());
            updatedDeviceType.setDeviceIconId(deviceType.getDeviceIconId());

            return new ResponseWrapper(mapToCollection(updatedDeviceType));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Update device type failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteDeviceType(Integer id, String name) {
        try {
            FilterValidator.checkForMinimumFilters(id, name);
            validateTypeExists(id, name);

            Boolean deleteSuccessful = deviceTypeRepository.deleteDeviceType(id, name);

            if(!deleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete device type failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper getTypesIcon(Integer id) {
        try {
            FilterValidator.checkForMinimumFilters(id);

            DeviceType deviceType = getDeviceType(id, null);

            if(deviceType.getDeviceIconId() == null) {
                throw new ExceptionWrapper("Get device type's icon failed", "DeviceIcon ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            return deviceIconService.getDeviceIcons(deviceType.getDeviceIconId(), null);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device type's icon failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper renameTypesIcon(Integer id, DeviceIcon deviceIcon) {
        try{
            FilterValidator.checkForMinimumFilters(id);

            DeviceType deviceType = getDeviceType(id, null);

            if(deviceType.getDeviceIconId() == null) {
                throw new ExceptionWrapper("Rename device type's icon failed", "DeviceIcon ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            return deviceIconService.updateDeviceIcon(deviceType.getDeviceIconId(), null, deviceIcon);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Rename device type's icon failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteTypesIcon(Integer id) {
        try {
            FilterValidator.checkForMinimumFilters(id);

            DeviceType deviceType = getDeviceType(id, null);

            if(deviceType.getDeviceIconId() == null) {
                throw new ExceptionWrapper("delete device type's icon failed", "DeviceIcon ID is null", ErrorCode.PARAMETER_CONFLICT);
            }

            return deviceIconService.deleteDeviceIcon(deviceType.getDeviceIconId(), null);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete device type's icon failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper getTypesDevices(Integer id, Integer deviceGroupId, Integer configurationId) {
        try {
            FilterValidator.checkForMinimumFilters(id);

            return deviceService.getDevices(null, null, id, deviceGroupId, configurationId, null);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get type's devices failed");
        }
        return null;
    }

    @Override
    public ResponseWrapper addDeviceWithType(Integer id, Device device) {
        try {
            FilterValidator.checkForMinimumFilters(id);
            device.setDeviceTypeId(id);

            return deviceService.addDevice(device);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add device with type failed");
        }
        return null;
    }


    /**
     * Validate a type matching given parameters exists
     *
     * @param id
     *      Device type ID used as filter
     * @param name
     *      Device type name used as filter
     * @throws NotFoundException
     *      Exception thrown when type matching given parameters was not found
     */
    private void validateTypeExists(Integer id, String name) throws NotFoundException {
        getDeviceType(id, name);
    }

    private DeviceType getDeviceType(Integer id, String name) throws NotFoundException {
        Collection<DeviceType> deviceTypes = deviceTypeRepository.getDeviceTypes(id, name, null);

        if(CollectionUtils.isEmpty(deviceTypes) || !deviceTypes.stream().findFirst().isPresent()) {
            throwNotFoundException(String.format("[id: %d, name: %s]", id, name));
        }

        return deviceTypes.stream().findFirst().get();
    }
}
