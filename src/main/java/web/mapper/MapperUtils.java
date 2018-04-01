package web.mapper;

import net.hamnaberg.json.Value;

import java.net.URI;

/**
 * Helper utilities for collection+json mappers
 */
class MapperUtils {

    private MapperUtils() {
        // Private constructor for static
    }

    static URI buildHref(URI uri, String resourceUri) {
        return buildHref(uri, resourceUri, null, "");
    }

    static URI buildHref(URI uri, String resourceUri, Integer id) {
        return buildHref(uri, resourceUri, id, "");
    }

    static URI buildHref(URI uri, String resourceUri, Integer id, String postFix) {
        final String idUri = id == null ? "" : String.format("/%d", id);

        return uri.resolve(resourceUri + idUri + postFix);
    }

    static Value getOptionalValue(Integer optionalValue) {
        if(optionalValue == null) {
            return Value.NULL;
        }

        return Value.of(optionalValue);
    }

    static Value getOptionalValue(String optionalValue) {
        if(optionalValue == null) {
            return Value.NULL;
        }

        return Value.of(optionalValue);
    }
}
