package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import web.domain.Device;
import web.repository.DeviceRepository;

@RestController
@RequestMapping("/rest")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public Iterable<Device> getDevice() {
        return deviceRepository.findAll();
    }
}
