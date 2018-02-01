package web.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.domain.response.ResponseWrapper;
import web.service.DeviceIconService;

@RestController
@RequestMapping("/rest")
public class DeviceIconController {

    private final DeviceIconService deviceIconService;

    DeviceIconController(DeviceIconService deviceIconService){
        this.deviceIconService = deviceIconService;
    }

    @RequestMapping(value = "/device-icon", method = RequestMethod.POST)
    public ResponseWrapper uploadDeviceIcon(@RequestBody MultipartFile icon,
                                            @RequestParam("name") String name) {
        return deviceIconService.uploadDeviceIcon(icon, name);
    }

    @RequestMapping(value = "/device-icon", method = RequestMethod.GET)
    public ResponseEntity<Resource> getDeviceIcon(String name) {
        Resource icon = deviceIconService.getDeviceIcon(name);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + icon.getFilename() + "\"")
                .body(icon);
    }
}