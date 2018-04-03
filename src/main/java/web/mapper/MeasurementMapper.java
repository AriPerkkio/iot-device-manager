package web.mapper;

import javaslang.control.Option;
import net.hamnaberg.json.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.domain.entity.Measurement;
import web.domain.response.ErrorCode;
import web.exception.ExceptionWrapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static web.mapper.MapperUtils.buildHref;
import static web.mapper.MapperUtils.formatTime;
import static web.mapper.MapperUtils.getOptionalValue;

public class MeasurementMapper {

    private static final String MEASUREMENTS_URI = "/api/measurements";
    private static final String DEVICES_URI = "/api/devices";

    private MeasurementMapper() {
        // Private constructor for static
    }

    /**
     * Map {@link Measurement} to {@link Measurement}
     *
     * @param measurement
     *      Measurement to map
     * @return
     *      Collection containing measurement
     */
    public static Collection mapToCollection(Measurement measurement) {
        return mapToCollection(Collections.singletonList(measurement));
    }

    /**
     * Map {@link java.util.Collection<Measurement>} to {@link net.hamnaberg.json.Collection}
     *
     * @param measurements
     *      Collection of measurements to map
     * @return
     *      Collection containing measurements
     */
    public static Collection mapToCollection(java.util.Collection<Measurement> measurements) {
        List<Item> items = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        measurements.forEach(measurement -> {
            try {
                items.add(mapToItem(measurement));

                Link link = mapToLink(measurement);
                if (!links.contains(link)) {
                    links.add(link);
                }
            } catch(Exception e) {
                throw new ExceptionWrapper("Internal error", "Measurement mapping failed", ErrorCode.INTERNAL_ERROR);
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

    private static Item mapToItem(Measurement measurement) throws Exception {
        URI href = buildHref(
            ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(),
            MEASUREMENTS_URI,
            null,
            String.format("?deviceId=%d", measurement.getDeviceId()));

        // Mandatory fields
        Value deviceId = Value.of(measurement.getDeviceId());
        Value timeStamp = Value.of(formatTime(measurement.getTime()));

        // Optional fields
        Value content = getOptionalValue(measurement.getContent());

        List<Property> properties = Arrays.asList(
            Property.value("deviceId", Option.of("Device ID"), deviceId),
            Property.value("content", Option.of("Content"), content),
            Property.value("time", Option.of("Timestamp"), timeStamp)
        );

        return Item.create(href, properties);
    }

    private static Link mapToLink(Measurement measurement) {
        URI baseUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        Integer id = measurement.getDeviceId();

        String deviceRel = "device";
        URI deviceHref = buildHref(baseUri, DEVICES_URI, null, String.format("?id=%d", id));

        return Link.create(deviceHref, deviceRel, Option.of("Device"));
    }

    private static List<Query> getQueries() {
        return Collections.singletonList((
            Query.create(
                buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), MEASUREMENTS_URI),
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
                Property.value("content", Option.of("Content"), Option.of(Value.of(""))),
                Property.value("time", Option.of("Timestamp"), Option.of(Value.of("")))
            )
        );
    }

}