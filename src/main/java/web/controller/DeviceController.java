package web.controller;

import org.springframework.web.bind.annotation.*;
import web.domain.Device;
import web.repository.DeviceRepository;

import java.util.Collection;

@RestController
@RequestMapping("/rest")
public class DeviceController {

    private final DeviceRepository deviceRepository;

    DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }


    // TODO Validate RequestParams and body, regex for string properties (characters allowed in URLs only)

    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public Collection<Device> getDevices(@RequestParam(name="name", required = false) String name) {
        return deviceRepository.getDevices(name);
    }

    @RequestMapping(value = "/device", method = RequestMethod.POST)
    public Device addDevice(@RequestBody Device device) {
        return deviceRepository.addDevice(device);
    }
}
