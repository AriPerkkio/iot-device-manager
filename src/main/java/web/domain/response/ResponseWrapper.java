package web.domain.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.*;

/**
 * Generic response wrapper to pass payload and errors to client
 */
public class ResponseWrapper extends ResponseEntity {
    private Object payload;
    private ErrorWrapper error;
    private HttpStatus httpStatus;

    public ResponseWrapper(Object payload) {
        super(payload, HttpStatus.OK);
    }

    public ResponseWrapper(ErrorWrapper error) {
        super(error, resolveHttpStatus(error));
    }

    /**
     * Set erroneous HTTP status based on errrorWrappers content
     *
     * @param error
     *      ErrorWrapper to check
     * @return
     *      Erroneous HttpStatus
     */
    private static HttpStatus resolveHttpStatus(ErrorWrapper error) {
        switch(error.getErrorCode()) {
            case PARAMETER_VALIDATION_ERROR:
                return BAD_REQUEST;
            case NO_ITEMS_FOUND:
                return NOT_FOUND;
            case PARAMETER_CONFLICT:
                return CONFLICT;
            case INTERNAL_ERROR:
                return INTERNAL_SERVER_ERROR;
            default:
                return INTERNAL_SERVER_ERROR;
        }
    }

    public Object getPayload() {
        return payload;
    }

    public ErrorWrapper getError() {
        return error;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
