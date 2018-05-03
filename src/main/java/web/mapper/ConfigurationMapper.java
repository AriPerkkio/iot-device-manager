package web.mapper;

import javaslang.control.Option;
import net.hamnaberg.json.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.domain.entity.Configuration;
import web.domain.response.ErrorCode;
import web.exception.ExceptionWrapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static web.mapper.MapperUtils.buildHref;
import static web.mapper.MapperUtils.getOptionalValue;
import static web.mapper.MapperUtils.getProfileLink;

public class ConfigurationMapper {
    private static final String CONFIGURATIONS_URI = "/api/configurations";

    private ConfigurationMapper() {
        // Private constructor for static
    }

    /**
     * Map {@link Configuration} to {@link Collection}
     *
     * @param configuration
     *      Configuration to map
     * @return
     *      Collection containing configuration
     */
    public static Collection mapToCollection(Configuration configuration) throws Exception {
        return mapToCollection(Collections.singletonList(configuration));
    }

    /**
     * Map {@link java.util.Collection<Configuration>} to {@link net.hamnaberg.json.Collection}
     *
     * @param configurations
     *      Collection of configurations to map
     * @return
     *      Collection containing configurations
     */
    public static Collection mapToCollection(java.util.Collection<Configuration> configurations) {
        List<Item> items = new ArrayList<>();
        List<Link> links = new ArrayList<>();

        // Add profile link
        links.add(getProfileLink(configurations));

        configurations.forEach(configuration -> {
            try {
                items.add(mapToItem(configuration));
                links.addAll(mapToLinks(configuration));
            } catch (Exception e) {
                throw new ExceptionWrapper("Internal error", "Failed to map configuration", ErrorCode.INTERNAL_ERROR);
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

    private static Item mapToItem(Configuration configuration) throws Exception {
        URI href = buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), CONFIGURATIONS_URI, configuration.getId());

        // Mandatory fields
        Value id = Value.of(configuration.getId());
        Value name = Value.of(configuration.getName());

        // Optional fields
        Value description = getOptionalValue(configuration.getDescription());
        Value content = getOptionalValue(configuration.getContent());

        List<Property> properties = Arrays.asList(
            Property.value("id", Option.of("Configuration ID"), id),
            Property.value("name", Option.of("Name"), name),
            Property.value("description", Option.of("Description"), description),
            Property.value("content", Option.of("Configuration as JSON"), content)
        );

        return Item.create(href, properties);
    }

    private static List<Link> mapToLinks(Configuration configuration) {
        URI baseUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        Integer id = configuration.getId();

        String devicesRel = String.format("configuration %s devices", id);
        URI devicesHref = buildHref(baseUri, CONFIGURATIONS_URI, id, "/devices");

        return Collections.singletonList(
            Link.create(devicesHref, devicesRel, Option.of("Devices"))
        );
    }

    private static List<Query> getQueries() {
        return Collections.singletonList((
            Query.create(
                buildHref(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri(), CONFIGURATIONS_URI),
                "search",
                Option.of("Search"),
                Arrays.asList(
                    Property.value("id", Value.of("")),
                    Property.value("name", Value.of(""))
                )
            )));
    }

    private static Template getTemplate() {
        return Template.create(
            Arrays.asList(
                Property.value("name", Option.of("Configuration name"), Option.of(Value.of(""))),
                Property.value("description", Option.of("Description"), Option.of(Value.of(""))),
                Property.value("content", Option.of("Configuration as JSON"), Option.of(Value.of("")))
            )
        );
    }
}