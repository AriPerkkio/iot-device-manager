package web.domain.response;

public enum ErrorCode {
    // Use string values due to net.hamnaberg.json.Error.code being String
    PARAMETER_VALIDATION_ERROR("1001"),
    NO_ITEMS_FOUND("1002"),
    INTERNAL_ERROR("1003"),
    PARAMETER_CONFLICT("1004");

    final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
