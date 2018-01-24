package web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.domain.entity.Configuration;
import web.domain.entity.DeviceGroup;
import web.domain.entity.DeviceIcon;
import web.domain.entity.Location;
import web.domain.response.DeviceResponse;
import web.domain.response.DeviceTypeResponse;
import web.domain.response.ResponseWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/rest")
public class DeviceInformationController {

    // TODO is this endpoint required
    @RequestMapping(value = "/devices-information", method = RequestMethod.GET)
    public ResponseWrapper getDevices(
            @RequestParam(name="name", required = false) String name) {
        DeviceResponse deviceResponse = new DeviceResponse();

        deviceResponse.setConfiguration(new Configuration());
        deviceResponse.setDeviceGroup(new DeviceGroup());
        deviceResponse.setDeviceIcon(new DeviceIcon());
        deviceResponse.setDeviceType(new DeviceTypeResponse());
        deviceResponse.setLocation(new Location());

        Collection<DeviceResponse> deviceResponses = new ArrayList<>();
        deviceResponses.add(deviceResponse);
        deviceResponses.add(deviceResponse);

        return new ResponseWrapper(deviceResponses, Collections.singleton("Unknown error"));
    }
}
