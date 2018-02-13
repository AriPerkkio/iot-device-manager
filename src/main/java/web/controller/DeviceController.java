package web.controller;

import org.springframework.web.bind.annotation.*;
import web.domain.entity.Device;
import web.domain.response.ResponseWrapper;
import web.repository.DeviceRepository;
import web.service.DeviceService;

@RestController
@RequestMapping("/rest")
public class DeviceController {

    private final DeviceService deviceService;

    DeviceController(DeviceRepository deviceRepository, DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @RequestMapping(value = "/device", method = RequestMethod.GET)
    public ResponseWrapper getDevices(
            @RequestParam(name="id", required = false) Integer id,
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="deviceTypeId", required = false) Integer deviceTypeId,
            @RequestParam(name="deviceGroupId", required = false) Integer deviceGroupId,
            @RequestParam(name="configurationId", required = false) Integer configurationId,
            @RequestParam(name="authenticationKey", required = false) String authenticationKey) {
        return deviceService.getDevices(id, name, deviceTypeId, deviceGroupId, configurationId, authenticationKey);
    }

    @RequestMapping(value = "/device", method = RequestMethod.POST)
    public ResponseWrapper addDevice(@RequestBody Device device) {
        return deviceService.addDevice(device);
    }
}
