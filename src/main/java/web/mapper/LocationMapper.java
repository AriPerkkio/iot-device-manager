package web.mapper;

import javaslang.control.Option;
import net.hamnaberg.json.Collection;
import net.hamnaberg.json.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.domain.entity.Location;

import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static web.mapper.MapperUtils.buildHref;

public class LocationMapper {

    private static final String LOCATIONS_URI = "/api/locations";
    private static final String DEVICES_URI = "/api/devices";

    private LocationMapper() {
        // Private constructor for static
    }

    /**
     * Map {@link Location} to {@link Location}
     *
     * @param location
     *      Location to map
     * @return
     *      Collection containing location
     */
    public static Collection mapToCollection(Location location) {
        return mapToCollection(Collections.singletonList(location));
    }

    /**
     * Map {@link java.util.Collection<Location>} to {@link net.hamnaberg.json.Collection}
     *
     * @param locations
     *      Collection of locations to map
     * @return
     *      Collection containing locations
     */
    public static Collection mapToCollection(java.util.Collection<Location> locations) {
        List<Item> items = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        locations.forEach(location -> {
            items.add(mapToItem(location));

            Link link = mapToLink(location);
            if(!links.contains(link)) {
                links.add(link);
            }
        });

        return Collection.create(
            ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(),
            links,
            items,
            getQueries(),
            getTemplate(),
            null);
    }

    private static Item mapToItem(Location location) {
        URI href = buildHref(
            ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(),
            LOCATIONS_URI,
            null,
            String.format("?deviceId=%d", location.getDeviceId()));

        // Mandatory fields
        Value deviceId = Value.of(location.getDeviceId());
        Value latitude = Value.of(location.getLatitude());
        Value longitude = Value.of(location.getLongitude());
        Value timeStamp = Value.of(formatTime(location.getTime()));

        List<Property> properties = Arrays.asList(
            Property.value("deviceId", Option.of("Device ID"), deviceId),
            Property.value("latitude", Option.of("Latitude"), latitude),
            Property.value("longitude", Option.of("Longitude"), longitude),
            Property.value("time", Option.of("Timestamp"), timeStamp)
        );

        return Item.create(href, properties);
    }

    private static Link mapToLink(Location location) {
        URI baseUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        Integer id = location.getDeviceId();

        String deviceRel = "device";
        URI deviceHref = buildHref(baseUri, DEVICES_URI, null, String.format("?id=%d", id));

        return Link.create(deviceHref, deviceRel, Option.of("Device"));
    }

    private static List<Query> getQueries() {
        return Collections.singletonList((
            Query.create(
                buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), LOCATIONS_URI),
                "search",
                Option.of("Search"),
                Arrays.asList(
                    Property.value("deviceId", Value.of("")),
                    Property.value("exactTime", Value.of("")),
                    Property.value("startTime", Value.of("")),
                    Property.value("endTime", Value.of(""))
                )
            )));
    }

    private static Template getTemplate() {
        return Template.create(
            Arrays.asList(
                Property.value("deviceId", Option.of("Device ID"), Option.of(Value.of(""))),
                Property.value("latitude", Option.of("Latitude"), Option.of(Value.of(""))),
                Property.value("longitude", Option.of("Longitude"), Option.of(Value.of(""))),
                Property.value("time", Option.of("Timestamp"), Option.of(Value.of("")))
            )
        );
    }

    /**
     * Format time to string
     *
     * @param time
     *      Time to format
     * @return
     *      Formatted string
     */
    public static String formatTime(Date time) {
        if(time == null) {
            return null;
        }

        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            .format(new Timestamp(time.getTime()));
    }
}