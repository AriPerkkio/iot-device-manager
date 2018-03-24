package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import web.domain.entity.Device;
import web.domain.response.ErrorCode;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionWrapper;
import web.repository.DeviceRepository;
import web.service.DeviceService;
import web.validators.FilterValidator;

import java.util.Collection;

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
                throw new NotFoundException("");
            }

            return new ResponseWrapper(mapToCollection(devices));
        } catch(NotFoundException e) {
            throw new ExceptionWrapper("Devices not found", "Devices not found matching given parameters", ErrorCode.NO_ITEMS_FOUND);
        } catch(Exception e) {
            throw new ExceptionWrapper("Get devices failed", "Unable to get devices", ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public ResponseWrapper addDevice(Device device) {
        try {
            Device addedDevice = deviceRepository.addDevice(device);

            return new ResponseWrapper(mapToCollection(addedDevice));
        } catch(Exception e) {
            throw new ExceptionWrapper("Add device failed", "Unable to add device", ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public ResponseWrapper updateDevice(Integer id, String name, String authenticationKey, Device device) {
        try {
            FilterValidator.checkForMinimumFilters(id, name, authenticationKey);
            Device updatedDevice = deviceRepository.updateDevice(id, name, authenticationKey, device);

            return new ResponseWrapper(mapToCollection(updatedDevice));
        } catch(ExceptionWrapper e) {
            throw new ExceptionWrapper(e.getTitle(), e.getMessage(), e.getCode());
        } catch(Exception e) {
            throw new ExceptionWrapper("Update device failed", "Unable to update device", ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public ResponseWrapper deleteDevice(Integer id, String name, String authenticationKey) {
        try {
            FilterValidator.checkForMinimumFilters(id, name, authenticationKey);
            Boolean deleteSuccessful = deviceRepository.deleteDevice(id, name, authenticationKey);

            if (!deleteSuccessful) {
                throw new HibernateError("Delete device failed");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch(ExceptionWrapper e) {
            throw new ExceptionWrapper(e.getTitle(), e.getMessage(), e.getCode());
        } catch(DataAccessException e) {
            throw new ExceptionWrapper("Delete device failed", "Database error occurred when deleting device", ErrorCode.INTERNAL_ERROR);
        } catch (HibernateError e) {
            throw new ExceptionWrapper("Delete device failed", "Unable to delete device", ErrorCode.INTERNAL_ERROR);
        } catch (Exception e) {
            throw new ExceptionWrapper("Unhandled exception", "Unhandled exception occurred during device delete operation", ErrorCode.INTERNAL_ERROR);
        }
    }
}