package web.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import web.domain.entity.DeviceIcon;
import web.domain.response.ResponseWrapper;

public interface DeviceIconService {

    ResponseWrapper uploadDeviceIcon(MultipartFile icon, String name);

    Resource getDeviceIcon(DeviceIcon deviceIcon);
}
