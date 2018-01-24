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
    public ResponseWrapper getDevice(
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="authenticationKey", required = false) String authenticationKey) {

        return deviceService.getDevice(name, authenticationKey);
    }

    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public ResponseWrapper getDevices(
            @RequestParam(name="deviceTypeId", required = false) Integer deviceTypeId,
            @RequestParam(name="deviceGroupId", required = false) Integer deviceGroupId,
            @RequestParam(name="configurationId", required = false) Integer configurationId) {
        return deviceService.getDevices(deviceTypeId, deviceGroupId, configurationId);
    }

    @RequestMapping(value = "/device", method = RequestMethod.POST)
    public ResponseWrapper addDevice(@RequestBody Device device) {
        return deviceService.addDevice(device);
    }
}
