package web.mapper;

import javaslang.control.Option;
import net.hamnaberg.json.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.domain.entity.DeviceIcon;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static web.mapper.MapperUtils.buildHref;
import static web.mapper.MapperUtils.getProfileLink;

public class DeviceIconMapper {
    private static final String DEVICE_ICONS_URI = "/api/device-icons";

    private DeviceIconMapper() {
        // Private constructor for static
    }

    /**
     * Map {@link DeviceIcon} to {@link Collection}
     *
     * @param deviceIcon
     *      Device icon to map
     * @return
     *      Collection containing device icon
     */
    public static Collection mapToCollection(DeviceIcon deviceIcon) {
        return mapToCollection(Collections.singletonList(deviceIcon));
    }

    /**
     * Map {@link java.util.Collection<DeviceIcon>} to {@link net.hamnaberg.json.Collection}
     *
     * @param deviceIcons
     *      Collection of device icons to map
     * @return
     *      Collection containing device icons
     */
    public static Collection mapToCollection(java.util.Collection<DeviceIcon> deviceIcons) {
        List<Item> items = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        // Add profile link
        links.add(getProfileLink(deviceIcons));

        deviceIcons.forEach(icon -> {
            items.add(mapToItem(icon));
            links.addAll(mapToLinks(icon));
        });

        return Collection.create(
                ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(),
                links,
                items,
                getQueries(),
                null,
                null);
    }

    private static Item mapToItem(DeviceIcon deviceIcon) {
        URI href = buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), DEVICE_ICONS_URI, deviceIcon.getId());

        Value id = Value.of(deviceIcon.getId());
        Value name = Value.of(deviceIcon.getName());

        List<Property> properties = Arrays.asList(
                Property.value("id", Option.of("Device Icon ID"), id),
                Property.value("name", Option.of("Device Icon Name"), name)
        );

        return Item.create(href, properties);
    }

    private static List<Link> mapToLinks(DeviceIcon deviceIcon) {
        URI baseUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        Integer id = deviceIcon.getId();

        String typeRel = String.format("icon %d types", id);
        URI typeHref = buildHref(baseUri, DEVICE_ICONS_URI, id, "/types");

        return Collections.singletonList(
                Link.create(typeHref, typeRel, Option.of("Device Types"))
        );
    }

    private static List<Query> getQueries() {
        return Arrays.asList(
            Query.create(
                buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), DEVICE_ICONS_URI),
                "search",
                Option.of("Search"),
                Arrays.asList(
                    Property.value("id", Value.of("")),
                    Property.value("name", Value.of(""))
                )
            ),
            Query.create(
                buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), DEVICE_ICONS_URI+"/name.png"),
                "image",
                Option.of("Search image"),
                Collections.emptyList()
            ));
    }
}
