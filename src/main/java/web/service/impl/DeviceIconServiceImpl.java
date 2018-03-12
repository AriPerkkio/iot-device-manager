package web.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.domain.entity.DeviceIcon;
import web.domain.response.ResponseWrapper;
import web.repository.DeviceIconRepository;
import web.service.DeviceIconService;
import web.validators.FilterValidator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.regex.Pattern;

@Service
public class DeviceIconServiceImpl implements DeviceIconService {

    private final DeviceIconRepository deviceIconRepository;
    private final Path path;
    private final String iconsLocation;
    private final Pattern filenamePattern = Pattern.compile("^[A-Za-z0-9-_]{1,25}.(png)$");

    DeviceIconServiceImpl(DeviceIconRepository deviceIconRepository,
                          @Value("${deviceicon.upload.location.root}") String root,
                          @Value("${deviceicon.upload.location.icons}") String iconsLocation) {
        this.deviceIconRepository = deviceIconRepository;
        this.iconsLocation = iconsLocation;
        this.path = Paths.get(root + iconsLocation);
    }

    @Override
    public ResponseWrapper addDeviceIcon(MultipartFile icon, String name) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        try {
            DeviceIcon deviceIcon = new DeviceIcon(null, name);

            Files.copy(icon.getInputStream(), path.resolve(deviceIcon.getName()));
            responseWrapper.setPayload(deviceIconRepository.addDeviceIcon(deviceIcon));
        } catch (Exception e) {
            responseWrapper.setErrors(Collections.singletonList(e.toString()));
        }

        return responseWrapper;
    }

    @Override
    public Boolean deleteDeviceIcon(Integer id, String name) {
        try {
            Boolean result = deviceIconRepository.deleteDeviceIcon(id, name);

            if (result) {
                Files.delete(path.resolve(name));
            }

            return result;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Resource getDeviceIcon(Integer id, String name) {
        try {
            DeviceIcon deviceIcon = deviceIconRepository.getDeviceIcons(id, name).stream().findFirst().get();

            return new UrlResource(path.resolve(deviceIcon.getName()).toUri());
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public ResponseWrapper getDeviceIcons(Integer id, String name) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        try {
            responseWrapper.setPayload(deviceIconRepository.getDeviceIcons(id, name));
        } catch (Exception e) {
            responseWrapper.setErrors(Collections.singletonList(e.toString()));
        }

        return responseWrapper;
    }

    @Override
    public ResponseWrapper updateDeviceIcon(Integer id, String name, DeviceIcon deviceIcon) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        try {
            FilterValidator.checkForMinimumFilters(id, name);
            responseWrapper.setPayload(deviceIconRepository.updateDeviceIcon(id, name, deviceIcon));
        } catch (Exception e) {
            responseWrapper.setErrors(Collections.singletonList(e.toString()));
        }

        return responseWrapper;
    }
}
