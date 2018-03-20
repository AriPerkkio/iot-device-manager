package web.domain.response;

public enum ErrorCode {
    PARAMETER_VALIDATION_ERROR(1001),
    NO_ITEMS_FOUND(1002),
    INTERNAL_ERROR(1003),
    PARAMETER_CONFLICT(1004);

    final Integer code;

    ErrorCode(Integer code) {
        this.code = code;
    }
}
