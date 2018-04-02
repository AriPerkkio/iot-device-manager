package web.mapper;

import javaslang.control.Option;
import net.hamnaberg.json.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.domain.entity.DeviceType;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static web.mapper.MapperUtils.buildHref;
import static web.mapper.MapperUtils.getOptionalValue;

public class DeviceTypeMapper {
    private static final String DEVICE_TYPES_URI = "/device-types";

    private DeviceTypeMapper() {
        // Private constructor for static
    }

    /**
     * Map {@link DeviceType} to {@link Collection}
     *
     * @param deviceType
     *      Device type to map
     * @return
     *      Collection containing device type
     */
    public static Collection mapToCollection(DeviceType deviceType) {
        return mapToCollection(Collections.singletonList(deviceType));
    }

    /**
     * Map {@link java.util.Collection<DeviceType>} to {@link net.hamnaberg.json.Collection}
     *
     * @param deviceTypes
     *      Collection of device types to map
     * @return
     *      Collection containing device types
     */
    public static Collection mapToCollection(java.util.Collection<DeviceType> deviceTypes) {
        List<Item> items = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        deviceTypes.forEach(type -> {
            items.add(mapToItem(type));
            links.addAll(mapToLinks(type));
        });

        return Collection.create(
            ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(),
            links,
            items,
            getQueries(),
            getTemplate(),
            null);
    }

    private static Item mapToItem(DeviceType deviceType) {
        URI href = buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), DEVICE_TYPES_URI, deviceType.getId());

        // Mandatory fields
        Value id = Value.of(deviceType.getId());
        Value name = Value.of(deviceType.getName());

        // Optional fields
        Value deviceIconId = getOptionalValue(deviceType.getDeviceIconId());

        List<Property> properties = Arrays.asList(
            Property.value("id", Option.of("Device Type ID"), id),
            Property.value("name", Option.of("Device Type Name"), name),
            Property.value("deviceIconId", Option.of("Device Icon ID"), deviceIconId)
        );

        return Item.create(href, properties);
    }

    private static List<Link> mapToLinks(DeviceType deviceType) {
        URI baseUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        Integer id = deviceType.getId();

        String devicesRel = String.format("type %d devices", id);
        URI devicesHref = buildHref(baseUri, DEVICE_TYPES_URI, id, "/devices");

        String iconRel = String.format("type %d icon", id);
        URI iconHref = buildHref(baseUri, DEVICE_TYPES_URI, id, "/icon");

        return Arrays.asList(
            Link.create(devicesHref, devicesRel, Option.of("Devices")),
            Link.create(iconHref, iconRel, Option.of("Device icon"))
        );
    }

    private static List<Query> getQueries() {
        return Collections.singletonList((
            Query.create(
                buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), DEVICE_TYPES_URI),
                "search",
                Option.of("search"),
                Arrays.asList(
                    Property.value("id", Value.of("")),
                    Property.value("name", Value.of("")),
                    Property.value("deviceIconId", Value.of(""))
                )
            )));
    }

    private static Template getTemplate() {
        return Template.create(
            Arrays.asList(
                Property.value("name", Option.of("Type name"), Option.of(Value.of(""))),
                Property.value("deviceIconId", Option.of("Device icon identifier"), Option.of(Value.of("")))
            )
        );
    }
}