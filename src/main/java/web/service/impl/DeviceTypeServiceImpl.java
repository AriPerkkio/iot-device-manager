package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import web.domain.entity.DeviceType;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.repository.DeviceTypeRepository;
import web.service.DeviceTypeService;
import web.validators.FilterValidator;

import java.util.Collection;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.DeviceTypeMapper.mapToCollection;

@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    private final DeviceTypeRepository deviceTypeRepository;

    DeviceTypeServiceImpl(DeviceTypeRepository deviceTypeRepository) {
        this.deviceTypeRepository = deviceTypeRepository;
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
        Collection<DeviceType> deviceTypes = deviceTypeRepository.getDeviceTypes(id, name, null);

        if(CollectionUtils.isEmpty(deviceTypes)) {
            throwNotFoundException(String.format("[id: %d, name: %s]", id, name));
        }
    }
}
