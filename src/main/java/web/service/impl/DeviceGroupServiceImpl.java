package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import web.domain.entity.Device;
import web.domain.entity.DeviceGroup;
import web.domain.entity.Location;
import web.domain.entity.Measurement;
import web.domain.response.ErrorCode;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.exception.ExceptionWrapper;
import web.mapper.DeviceMapper;
import web.mapper.LocationMapper;
import web.mapper.MeasurementMapper;
import web.repository.DeviceGroupRepository;
import web.repository.DeviceRepository;
import web.repository.LocationRepository;
import web.repository.MeasurementRepository;
import web.service.DeviceGroupService;
import web.validators.FilterValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.DeviceGroupMapper.mapToCollection;
import static web.mapper.MapperUtils.formatTime;

@Service
public class DeviceGroupServiceImpl implements DeviceGroupService {

    final DeviceGroupRepository deviceGroupRepository;
    final DeviceRepository deviceRepository;
    final MeasurementRepository measurementRepository;
    final LocationRepository locationRepository;

    DeviceGroupServiceImpl(DeviceGroupRepository deviceGroupRepository, DeviceRepository deviceRepository,
                           MeasurementRepository measurementRepository, LocationRepository locationRepository) {
        this.deviceGroupRepository = deviceGroupRepository;
        this.deviceRepository = deviceRepository;
        this.measurementRepository = measurementRepository;
        this.locationRepository = locationRepository;
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

    @Override
    public ResponseWrapper getGroupsMeasurements(Integer id, Date exactTime, Date startTime, Date endTime) {
        try {
            FilterValidator.checkForMinimumFilters(id, exactTime, startTime, endTime);
            validateGroupExists(id, null);

            Collection<Device> devices = deviceRepository.getDevices(null, null, null, id, null, null);

            if(CollectionUtils.isEmpty(devices)) {
                throw new ExceptionWrapper(
                    "Get group's measurements failed",
                    String.format("No devices found for group %d", id),
                    ErrorCode.NO_ITEMS_FOUND);
            }

            Collection<Measurement> measurements = new ArrayList<>();
            for(Device device : devices) {
                measurements.addAll(measurementRepository.getMeasurements(device.getId(), exactTime, startTime, endTime));
            }

            if(CollectionUtils.isEmpty(measurements)) {
                String parameters = String.format("exactTime %s, startTime %s, endTime: %s",
                    formatTime(exactTime), formatTime(startTime), formatTime(endTime));
                throw new ExceptionWrapper(
                    "Get group's measurements failed",
                    String.format("No measurements found for group %d with parameters: %s", id, parameters),
                    ErrorCode.NO_ITEMS_FOUND);
            }

            return new ResponseWrapper(MeasurementMapper.mapToCollection(measurements));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get group's measurements failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteGroupsMeasurements(Integer id, Date exactTime, Date startTime, Date endTime) {
        try {
            FilterValidator.checkForMinimumFilters(id, exactTime, startTime, endTime);
            validateGroupExists(id, null);

            Collection<Device> devices = deviceRepository.getDevices(null, null, null, id, null, null);

            if(CollectionUtils.isEmpty(devices)) {
                throw new ExceptionWrapper(
                    "Delete group's measurements failed",
                    String.format("No devices found for group %d", id),
                    ErrorCode.NO_ITEMS_FOUND);
            }

            // Report success if at least one delete operation was successful
            Boolean atLeastOneDeleteSuccessful = false;
            for(Device device : devices) {
                Boolean deleteSuccessful = measurementRepository.deleteMeasurements(device.getId(), exactTime, startTime, endTime);

                if(deleteSuccessful) {
                    atLeastOneDeleteSuccessful = true;
                }
            }

            if(!atLeastOneDeleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete group's measurements failed");
        }

        return null;
    }


    @Override
    public ResponseWrapper getGroupsLocations(Integer id, Date exactTime, Date startTime, Date endTime) {
        try {
            FilterValidator.checkForMinimumFilters(id, exactTime, startTime, endTime);
            validateGroupExists(id, null);

            Collection<Device> devices = deviceRepository.getDevices(null, null, null, id, null, null);

            if(CollectionUtils.isEmpty(devices)) {
                throw new ExceptionWrapper(
                    "Get group's location updates failed",
                    String.format("No devices found for group %d", id),
                    ErrorCode.NO_ITEMS_FOUND);
            }

            Collection<Location> locations = new ArrayList<>();
            for(Device device : devices) {
                locations.addAll(locationRepository.getLocations(device.getId(), exactTime, startTime, endTime));
            }

            if(CollectionUtils.isEmpty(locations)) {
                String parameters = String.format("exactTime %s, startTime %s, endTime: %s",
                    formatTime(exactTime), formatTime(startTime), formatTime(endTime));
                throw new ExceptionWrapper(
                    "Get group's location updates failed",
                    String.format("No locations found for group %d with parameters: %s", id, parameters),
                    ErrorCode.NO_ITEMS_FOUND);
            }

            return new ResponseWrapper(LocationMapper.mapToCollection(locations));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get group's location updates failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteGroupsLocations(Integer id, Date exactTime, Date startTime, Date endTime) {
        try {
            FilterValidator.checkForMinimumFilters(id, exactTime, startTime, endTime);
            validateGroupExists(id, null);

            Collection<Device> devices = deviceRepository.getDevices(null, null, null, id, null, null);

            if(CollectionUtils.isEmpty(devices)) {
                throw new ExceptionWrapper(
                    "Delete group's location updates failed",
                    String.format("No devices found for group %d", id),
                    ErrorCode.NO_ITEMS_FOUND);
            }

            // Report success if at least one delete operation was successful
            Boolean atLeastOneDeleteSuccessful = false;
            for(Device device : devices) {
                Boolean deleteSuccessful = locationRepository.deleteLocations(device.getId(), exactTime, startTime, endTime);

                if(deleteSuccessful) {
                    atLeastOneDeleteSuccessful = true;
                }
            }

            if(!atLeastOneDeleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete group's location updates failed");
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
