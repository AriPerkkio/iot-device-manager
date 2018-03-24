package web.exception;

import web.domain.response.ErrorCode;

public class ExceptionWrapper extends RuntimeException {
    private String title;
    private String message;
    private ErrorCode code;

    public ExceptionWrapper(String title, String message, ErrorCode code) {
        this.title = title;
        this.message = message;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getCodeAsString() {
        return code.getCode();
    }
}
