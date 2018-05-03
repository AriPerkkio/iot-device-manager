package web.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import javaslang.control.Option;
import net.hamnaberg.json.Link;
import net.hamnaberg.json.Value;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.domain.entity.*;
import web.domain.response.ErrorCode;
import web.exception.ExceptionWrapper;

import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

/**
 * Helper utilities for collection+json mappers
 */
public class MapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MapperUtils() {
        // Private constructor for static
    }

    /**
     * {@link #buildHref(URI uri, String resourceUri, Integer id, String postFix)}
     */
    public static URI buildHref(URI uri, String resourceUri) {
        return buildHref(uri, resourceUri, null, "");
    }

    /**
     * {@link #buildHref(URI uri, String resourceUri, Integer id, String postFix)}
     */
    public static URI buildHref(URI uri, String resourceUri, Integer id) {
        return buildHref(uri, resourceUri, id, "");
    }

    /**
     * Build URI from various parameters
     *
     * @param uri
     *      URI used to resolve root context
     * @param resourceUri
     *      Resource which is being described
     * @param id
     *      Resource ID
     * @param postFix
     *      Postfix for URI
     * @return
     *      Build URI
     */
    public static URI buildHref(URI uri, String resourceUri, Integer id, String postFix) {
        final String idUri = id == null ? "" : String.format("/%d", id);

        return uri.resolve(resourceUri + idUri + postFix);
    }

    /**
     * Get {@link Value} for HashMap
     * @param optionalValue
     *      HashMap value
     * @return
     *      {@link Value} for given HashMap
     * @throws Exception
     *      Exception thrown when JSON parse fails
     */
    public static Value getOptionalValue(HashMap optionalValue) throws Exception {
        if(optionalValue == null) {
            return Value.NULL;
        }

        return getOptionalValue(objectMapper.writeValueAsString(optionalValue));
    }

    /**
     * Get {@link Value} for Integer
     * @param optionalValue
     *      Integer value
     * @return
     *      {@link Value} for given Integer
     */
    public static Value getOptionalValue(Integer optionalValue) {
        if(optionalValue == null) {
            return Value.NULL;
        }

        return Value.of(optionalValue);
    }

    /**
     * Get {@link Value} for String
     * @param optionalValue
     *      String value
     * @return
     *      {@link Value} for given String
     */
    public static Value getOptionalValue(String optionalValue) {
        if(optionalValue == null) {
            return Value.NULL;
        }

        return Value.of(optionalValue);
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

    /**
     * Get application/collection+json profile Link for given resource. Maps resource into api-console url.
     *
     * @param collection
     *      Collection of API resources requiring profile link
     * @return
     *      {@link Link} for given resource's profile
     */
    public static Link getProfileLink(Collection collection) {
        // Base URL used by api-console. Profiles are under Types sub-page
        final String profileHref = "/api-doc/index.html#/docs/";
        String typeHrefPostfix = "";

        Optional resourceOpt = collection.stream().findFirst();
        if(!resourceOpt.isPresent()) {
            return null;
        }

        Object resource = resourceOpt.get();

        if(resource instanceof Device) {
            typeHrefPostfix = "types-0";
        } else if(resource instanceof DeviceGroup) {
            typeHrefPostfix = "types-1";
        } else if(resource instanceof DeviceIcon) {
            typeHrefPostfix = "types-2";
        } else if(resource instanceof DeviceType) {
            typeHrefPostfix = "types-3";
        } else if(resource instanceof Location) {
            typeHrefPostfix = "types-4";
        } else if(resource instanceof Measurement) {
            typeHrefPostfix = "types-5";
        } else if(resource instanceof Configuration) {
            typeHrefPostfix = "types-6";
        } else {
            throw new ExceptionWrapper(
                "Unknown entity",
                String.format("getProfileLink encountered unknown entity: %s", resource.getClass().toString()),
                ErrorCode.INTERNAL_ERROR
            );
        }

        URI profileLink = buildHref(
            ServletUriComponentsBuilder.fromCurrentRequest().build().toUri(), profileHref + typeHrefPostfix);

        return Link.create(profileLink, "profile", Option.of("Profile"));
    }
}
