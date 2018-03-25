package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import web.domain.entity.Device;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.repository.DeviceRepository;
import web.service.DeviceService;
import web.validators.FilterValidator;

import java.util.Collection;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.DeviceMapper.mapToCollection;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public ResponseWrapper getDevices(Integer id, String name, Integer deviceTypeId, Integer deviceGroupId,
                                      Integer configurationId, String authenticationKey) {
        try {
            Collection<Device> devices = deviceRepository.getDevices(
                id, name, deviceTypeId, deviceGroupId, configurationId, authenticationKey);

            if(devices.size() == 0) {
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

    /**
     * Validate a device matching given parameters exists
     *
     * @param id
     *      Device ID used as filter
     * @param name
     *      Device name used as filter
     * @param authenticationKey
     *      Device authentication key used as filter
     * @throws NotFoundException
     *      Exception thrown when device matching given parameters was not found
     */
    private void validateDeviceExists(Integer id, String name, String authenticationKey) throws NotFoundException {
        Integer deviceCount = deviceRepository.getDevices(id, name, null, null, null, authenticationKey).size();

        if(deviceCount == 0) {
            throwNotFoundException(String.format("[id: %d, name: %s, authenticationKey: %s]", id, name, authenticationKey));
        }
    }

}