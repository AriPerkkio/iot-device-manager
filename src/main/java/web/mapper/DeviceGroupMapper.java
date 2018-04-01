package web.mapper;

import javaslang.control.Option;
import net.hamnaberg.json.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.domain.entity.DeviceGroup;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static web.mapper.MapperUtils.buildHref;
import static web.mapper.MapperUtils.getOptionalValue;

public class DeviceGroupMapper {
    private static final String DEVICE_GROUPS_URI = "/device-groups";

    private DeviceGroupMapper() {
        // Private constructor for static
    }

    /**
     * Map {@link DeviceGroup} to {@link Collection}
     *
     * @param deviceGroup
     *      Device group to map
     * @return
     *      Collection containing device group
     */
    public static Collection mapToCollection(DeviceGroup deviceGroup) {
        return mapToCollection(Collections.singletonList(deviceGroup));
    }

    /**
     * Map {@link java.util.Collection<DeviceGroup>} to {@link net.hamnaberg.json.Collection}
     *
     * @param deviceGroups
     *      Collection of device groups to map
     * @return
     *      Collection containing device groups
     */
    public static Collection mapToCollection(java.util.Collection<DeviceGroup> deviceGroups) {
        List<Item> items = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        deviceGroups.forEach(group -> {
            items.add(mapToItem(group));
            links.addAll(mapToLinks(group));
        });

        return Collection.create(
                ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(),
                links,
                items,
                getQueries(),
                getTemplate(),
                null);
    }

    private static Item mapToItem(DeviceGroup deviceGroup) {
        URI href = buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), DEVICE_GROUPS_URI, deviceGroup.getId());

        // Mandatory fields
        Value id = Value.of(deviceGroup.getId());
        Value name = Value.of(deviceGroup.getName());

        // Optional fields
        Value description = getOptionalValue(deviceGroup.getDescription());

        List<Property> properties = Arrays.asList(
                Property.value("id", Option.of("Device Group ID"), id),
                Property.value("name", Option.of("Device Group Name"), name),
                Property.value("description", Option.of("Description"), description)
        );

        return Item.create(href, properties);
    }

    private static List<Link> mapToLinks(DeviceGroup deviceGroup) {
        URI baseUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        Integer id = deviceGroup.getId();

        String devicesRel = String.format("group %d devices", id);
        URI devicesHref = buildHref(baseUri, DEVICE_GROUPS_URI, id, "/devices");

        String measurementsRel = String.format("group %d measurements", id);
        URI measurementsHref = buildHref(baseUri, DEVICE_GROUPS_URI, id, "/measurements");

        String locationsRel = String.format("group %d locations", id);
        URI locationsHref = buildHref(baseUri, DEVICE_GROUPS_URI, id, "/locations");

        return Arrays.asList(
                Link.create(devicesHref, devicesRel, Option.of("Devices")),
                Link.create(measurementsHref, measurementsRel, Option.of("Measurements")),
                Link.create(locationsHref, locationsRel, Option.of("Locations"))
        );
    }

    private static List<Query> getQueries() {
        return Collections.singletonList((
            Query.create(
                buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), DEVICE_GROUPS_URI),
                "search",
                Option.of("search"),
                Arrays.asList(
                    Property.value("id", Value.of("")),
                    Property.value("name", Value.of(""))
                )
        )));
    }

    private static Template getTemplate() {
        return Template.create(
            Arrays.asList(
                Property.value("name", Option.of("Group name"), Option.of(Value.of(""))),
                Property.value("description", Option.of("Description"), Option.of(Value.of("")))
            )
        );
    }
}
