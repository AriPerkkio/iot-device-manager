package web.validators;

import org.springframework.validation.Errors;
import web.domain.response.ErrorCode;
import web.exception.ExceptionWrapper;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class FilterValidator {

    /**
     * Verifies that at least one filter is defined
     *
     * @param filters
     *      Collection of filters to validate
     */
    public static void checkForMinimumFilters(Object ...filters) throws ExceptionWrapper {

        Integer filterCount = Arrays.stream(filters)
            .filter(Objects::nonNull)
            .collect(Collectors.toList())
            .size();

        if (filterCount == 0) {
            throw new ExceptionWrapper("Filters missing", "At least one filter is required", ErrorCode.PARAMETER_VALIDATION_ERROR);
        }
    }

    /**
     * Validate request body errors
     *
     * @param errors
     *      Errors to check
     */
    public static void validateErrors(Errors errors) {
        if (errors.hasErrors()) {
            String message = String.format("Invalid value (%s) for parameter %s",
                errors.getFieldError().getRejectedValue(), errors.getFieldError().getField());

            throw new ExceptionWrapper("Request body validation error", message, ErrorCode.PARAMETER_VALIDATION_ERROR);
        }
    }

    /**
     * Validate request body errors. Ignore error caused by fieldToSkip parameter.
     * @param errors
     *      Error to validate
     * @param fieldToSkip
     *      Field which errors are ignored
     */
    public static void validateErrors(Errors errors, String fieldToSkip) {
        if(errors.hasErrors() && errors.getAllErrors().size() == 1 && fieldToSkip.equals(errors.getFieldError().getField())) {
            return;
        }
        validateErrors(errors);
    }
}
