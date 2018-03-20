package web.validators;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

public class FilterValidator {

    /**
     * Verifies that at least one filter is defined
     *
     * @param filters
     *      Collection of filters to validate
     */
    public static void checkForMinimumFilters(Object ...filters) throws Exception {

        // Array filter = CollectionUtils.arrayToList(filters).stream().filter(Objects::nonNull).close();

        if (ArrayUtils.isEmpty(filters) || CollectionUtils.arrayToList(filters).isEmpty()) {
            throw new Exception("Invalid filters. At least one filter is required.");
        }
    }
}
