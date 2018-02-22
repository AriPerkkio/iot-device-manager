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
    public ResponseWrapper uploadDeviceIcon(MultipartFile icon, String name) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        DeviceIcon deviceIcon = new DeviceIcon();
        deviceIcon.setName(name);

        try {
            validateFilename(deviceIcon);
            validateFilenameIsUnique(deviceIcon);

            Files.copy(icon.getInputStream(), path.resolve(deviceIcon.getName()));

            responseWrapper.setPayload(deviceIconRepository.addDeviceIcon(deviceIcon));
        } catch (Exception e) {
            responseWrapper.setErrors(Collections.singletonList(e.toString()));
        }

        return responseWrapper;
    }

    @Override
    public Resource getDeviceIcon(DeviceIcon deviceIcon) {
        try {
            validateFilename(deviceIcon);
            return new UrlResource(path.resolve(deviceIcon.getName()).toUri());
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    private void validateFilename(DeviceIcon deviceIcon) throws Exception {
        // TODO move to validator
    }

    private void validateFilenameIsUnique(DeviceIcon deviceIcon) throws Exception {
        // TODO move to validator
    }
}
