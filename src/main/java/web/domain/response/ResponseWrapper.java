package web.domain.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Generic response wrapper to pass payload and errors to client
 */
public class ResponseWrapper extends ResponseEntity {
    // TODO as private String payload;
    private Object payload;
    private HttpStatus httpStatus;

    public ResponseWrapper(Object payload) {
        super(payload.toString(), HttpStatus.OK);
        this.payload = payload;
        this.httpStatus = HttpStatus.OK;
    }

    public ResponseWrapper(Object payload, HttpStatus httpStatus) {
        super(payload.toString(), httpStatus);
        this.payload = payload;
        this.httpStatus = httpStatus;
    }

    public ResponseWrapper(Object payload, HttpHeaders httpHeaders, HttpStatus httpStatus) {
        super(payload.toString(), httpHeaders, httpStatus);
        this.payload = payload;
        this.httpStatus = httpStatus;
    }

    public Object getPayload() {
        return payload;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
