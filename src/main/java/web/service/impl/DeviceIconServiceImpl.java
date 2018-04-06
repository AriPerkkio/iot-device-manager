package web.service.impl;

import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import web.domain.entity.DeviceIcon;
import web.domain.entity.DeviceType;
import web.domain.response.ErrorCode;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.exception.ExceptionWrapper;
import web.mapper.DeviceTypeMapper;
import web.repository.DeviceIconRepository;
import web.repository.DeviceTypeRepository;
import web.service.DeviceIconService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.regex.Pattern;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.DeviceIconMapper.mapToCollection;
import static web.validators.FilterValidator.checkForMinimumFilters;

@Service
public class DeviceIconServiceImpl implements DeviceIconService {

    private final DeviceIconRepository deviceIconRepository;
    private final DeviceTypeRepository deviceTypeRepository;

    private final Path path;
    private final String iconsLocation;
    private final String filenameRegex = "^[A-Za-z0-9-_]{1,25}.(png)$";
    private final Pattern filenamePattern = Pattern.compile(filenameRegex);

    DeviceIconServiceImpl(DeviceIconRepository deviceIconRepository, DeviceTypeRepository deviceTypeRepository,
                          @Value("${deviceicon.upload.location.root}") String root,
                          @Value("${deviceicon.upload.location.icons}") String iconsLocation) {
        this.deviceIconRepository = deviceIconRepository;
        this.deviceTypeRepository = deviceTypeRepository;
        this.iconsLocation = iconsLocation;
        this.path = Paths.get(root + iconsLocation);
    }

    @Override
    public ResponseWrapper getDeviceIcons(Integer id, String name) {
        try {
            validateDeviceIconName(name);
            Collection<DeviceIcon> deviceIcons = deviceIconRepository.getDeviceIcons(id, name);

           if(CollectionUtils.isEmpty(deviceIcons)) {
                throwNotFoundException(String.format("[id: %d, name: %s]",id, name));
           }

            return new ResponseWrapper(mapToCollection(deviceIcons));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device icons failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addDeviceIcon(MultipartFile icon, String name) {
        try {
            checkForMinimumFilters(name);
            validateDeviceIconName(name);

            DeviceIcon deviceIcon = new DeviceIcon(null, name);
            DeviceIcon addedDeviceIcon = deviceIconRepository.addDeviceIcon(deviceIcon);
            Files.copy(icon.getInputStream(), path.resolve(deviceIcon.getName()));

            return new ResponseWrapper(mapToCollection(addedDeviceIcon));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add device icon failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteDeviceIcon(Integer id, String name) {
        try {
            checkForMinimumFilters(id, name);
            validateDeviceIconName(name);

            DeviceIcon deviceIcon = getDeviceIcon(id, name);
            Boolean deleteSuccessful = deviceIconRepository.deleteDeviceIcon(id, name);

            if (deleteSuccessful) {
                Files.delete(path.resolve(deviceIcon.getName()));
            } else {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete device icon failed");
        }

        return null;
    }

    @Override
    public Resource getDeviceIconFile(String name) {
        try {
            checkForMinimumFilters(name);
            validateDeviceIconName(name);
            validateDeviceIconExists(null, name);

            return new UrlResource(path.resolve(name).toUri());
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get device icon failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper updateDeviceIcon(Integer id, String name, DeviceIcon deviceIcon) {
        try {
            checkForMinimumFilters(id, name);
            validateDeviceIconName(deviceIcon.getName());
            validateDeviceIconName(name);

            DeviceIcon originalDeviceIcon = getDeviceIcon(id, name);
            DeviceIcon updatedDeviceIcon = deviceIconRepository.updateDeviceIcon(id, name, deviceIcon);
            Files.move(path.resolve(originalDeviceIcon.getName()), path.resolve(deviceIcon.getName()));

            // TODO, fix commit calls during single stored procedure. Currently update procedures return old item - not the updated one
            updatedDeviceIcon.setName(deviceIcon.getName());

            return new ResponseWrapper(mapToCollection(updatedDeviceIcon));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Update device icon failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper getIconsTypes(Integer id) {
        try {
            checkForMinimumFilters(id);
            validateDeviceIconExists(id, null);

            Collection<DeviceType> deviceTypes = deviceTypeRepository.getDeviceTypes(null, null, id);

            if(CollectionUtils.isEmpty(deviceTypes)) {
                throwNotFoundException(String.format("[deviceTypeId: %d]",id));
            }

            return new ResponseWrapper(DeviceTypeMapper.mapToCollection(deviceTypes));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get icon's types failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addTypeWithIcon(Integer id, DeviceType deviceType) {
        try {
            checkForMinimumFilters(id);
            validateDeviceIconExists(id, null);

            deviceType.setDeviceIconId(id);
            DeviceType addedDeviceType = deviceTypeRepository.addDeviceType(deviceType);

            return new ResponseWrapper(DeviceTypeMapper.mapToCollection(addedDeviceType));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add type with icon failed");
        }

        return null;
    }

    /**
     * Validate device icon name matches pattern
     */
    private void validateDeviceIconName(String name) {
        if(!StringUtils.isEmpty(name) && !filenamePattern.matcher(name).matches()) {
            throw new ExceptionWrapper(
                    "Parameter validation",
                    String.format("Device icon name %s does not meet regex pattern: %s", name, filenameRegex),
                    ErrorCode.PARAMETER_VALIDATION_ERROR);
        }
    }

    private void validateDeviceIconExists(Integer id, String name) throws NotFoundException {
        getDeviceIcon(id, name);
    }

    /**
     * Get device icon. Validates name. Verifies device icon exists in database and file system
     */
    private DeviceIcon getDeviceIcon(Integer id, String name) throws NotFoundException {
        validateDeviceIconName(name);
        Collection<DeviceIcon> deviceIcons = deviceIconRepository.getDeviceIcons(id, name);

        if(CollectionUtils.isEmpty(deviceIcons) || !deviceIcons.stream().findFirst().isPresent()) {
            throwNotFoundException(String.format("[id: %d, name: %s]", id, name));
        }

        DeviceIcon deviceIcon = deviceIcons.stream()
            .findFirst()
            .get();

        if(Files.notExists(path.resolve(deviceIcon.getName()))) {
            throwNotFoundException(String.format("[id: %d, name: %s]", id, name));
        }

        return deviceIcon;
    }
}
