package web.domain.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.HashMap;

import static web.domain.response.ErrorCode.*;

/**
 * Generic response wrapper to pass payload and errors to client
 */
public class ResponseWrapper extends ResponseEntity {
    private Object payload;
    private HashMap<ErrorCode, String> errors;
    private HttpStatus httpStatus;

    public ResponseWrapper() {
        // Set status as OK by default
        super(null, HttpStatus.OK);
    }

    /**
     * Set 
     */
    private void setErrorHttpStatus() {
        if (errors.containsKey(PARAMETER_VALIDATION_ERROR)) {
            this.httpStatus = HttpStatus.BAD_REQUEST;

        } else if (errors.containsKey(NO_ITEMS_FOUND)) {
            this.httpStatus = HttpStatus.NOT_FOUND;

        } else if (errors.containsKey(PARAMETER_CONFLICT)) {
            this.httpStatus = HttpStatus.CONFLICT;

        } else if (errors.containsKey(INTERNAL_ERROR)) {
            this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            throw new RuntimeException("ErrorCode not implemented: " + errors.toString());
        }
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public HashMap<ErrorCode, String> getErrors() {
        return errors;
    }

    public void setErrors(Collection<String> errors) {
        // TODO update usage
        this.errors = new HashMap<>();
        setErrorHttpStatus();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
