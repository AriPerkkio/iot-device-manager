package web.mapper;

import javaslang.control.Option;
import net.hamnaberg.json.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.domain.entity.Device;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DeviceMapper {
    private static final String DEVICES_URI = "/devices";

    private DeviceMapper() {
        // Private constructor for static
    }

    public static Collection mapToCollection(Device device) {
        return mapToCollection(Collections.singletonList(device));
    }

    public static Collection mapToCollection(java.util.Collection<Device> devices) {
        List<Item> items = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        devices.forEach(device -> {
            items.add(DeviceMapper.mapToItem(device));
            links.addAll(DeviceMapper.mapToLinks(device));
        });

        return Collection.create(
            ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(),
            links,
            items,
            DeviceMapper.getQueries(),
            DeviceMapper.getTemplate(),
            null);
    }

    private static Item mapToItem(Device device) {
        URI href = buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), device.getId());

        // Mandatory fields
        Value deviceId = Value.of(device.getId());
        Value deviceName = Value.of(device.getName());
        Value authenticationKey = Value.of(device.getAuthenticationKey());

        // Optional fields
        Value groupId = getOptionalValue(device.getDeviceGroupId());
        Value typeId = getOptionalValue(device.getDeviceTypeId());
        Value configurationId = getOptionalValue(device.getConfigurationId());

        List<Property> properties = Arrays.asList(
            Property.value("deviceId", Option.of("Device ID"), deviceId),
            Property.value("deviceName", Option.of("Device Name"), deviceName),
            Property.value("deviceGroupId", Option.of("Device Group ID"), groupId),
            Property.value("deviceTypeId", Option.of("Device Type ID"), typeId),
            Property.value("configurationId", Option.of("Configuration ID"), configurationId),
            Property.value("authenticationKey", Option.of("Authentication Key"), authenticationKey)
        );

        return Item.create(href, properties);
    }

    private static Value getOptionalValue(Integer optionalValue) {
        if(optionalValue == null) {
            return Value.NULL;
        }

        return Value.of(optionalValue);
    }

    private static URI buildHref(URI uri, Integer id) {
        return buildHref(uri, id, "");
    }


    private static URI buildHref(URI uri, Integer id, String postFix) {
        return uri.resolve(DEVICES_URI + String.format("/%d", id) + postFix);
    }

    private static List<Link> mapToLinks(Device device) {
        URI baseUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        Integer id = device.getId();

        String groupRel = String.format("device %s group", id);
        URI groupHref = buildHref(baseUri, id, "/group");

        String typeRel = String.format("device %s type", id);
        URI typeHref = buildHref(baseUri, id, "/type");

        String iconRel = String.format("device %s icon", id);
        URI iconHref = buildHref(baseUri, id, "/icon");

        String configurationRel = String.format("device %s configuration", id);
        URI configurationHref = buildHref(baseUri, id, "/configuration");

        String measurementsRel = String.format("device %s measurements", id);
        URI measurementsHref = buildHref(baseUri, id, "/measurements");

        String locationsRel = String.format("device %s locations", id);
        URI locationsHref = buildHref(baseUri, id, "/locations");


        return Arrays.asList(
            Link.create(groupHref, groupRel, Option.of("Device Group")),
            Link.create(typeHref, typeRel, Option.of("Device Type")),
            Link.create(iconHref, iconRel, Option.of("Device icon")),
            Link.create(configurationHref, configurationRel, Option.of("Configuration")),
            Link.create(measurementsHref, measurementsRel, Option.of("Measurements")),
            Link.create(locationsHref, locationsRel, Option.of("Locations"))
        );
    }

    private static List<Query> getQueries() {
        return Collections.singletonList((
            Query.create(
                ServletUriComponentsBuilder.fromCurrentRequest().build().toUri(),
                "search",
                Option.of("Search"),
                Arrays.asList(
                    Property.value("id", Value.of("")),
                    Property.value("name", Value.of("")),
                    Property.value("deviceTypeId", Value.of("")),
                    Property.value("deviceGroupId", Value.of("")),
                    Property.value("configurationId", Value.of("")),
                    Property.value("authenticationKey", Value.of(""))
                )
        )));
    }

    private static Template getTemplate() {
        return Template.create(
            Arrays.asList(
                Property.value("name", Option.of("Device name"), Option.of(Value.of(""))),
                Property.value("deviceTypeId", Option.of("Device Type Identifier"), Option.of(Value.of(""))),
                Property.value("deviceGroupId", Option.of("Device Group Identifier"), Option.of(Value.of(""))),
                Property.value("configurationId", Option.of("Configuration Identifier"), Option.of(Value.of("")))
            )
        );
    }
}
