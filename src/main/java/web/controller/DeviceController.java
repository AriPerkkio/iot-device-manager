package web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class DeviceController {

    @RequestMapping(value = "/device", method = RequestMethod.GET)
    public String getDevice() {
        return "Device 101";
    }
}
