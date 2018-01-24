package web.domain.response;

import java.util.Collection;

/**
 * Generic response wrapper to pass payload and errors to client
 */
public class ResponseWrapper {
    private Object payload;
    private Collection<String> errors;

    public ResponseWrapper() {
        // Default
    }

    public ResponseWrapper(Object payload, Collection<String> errors) {
        this.payload = payload;
        this.errors = errors;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Collection<String> getErrors() {
        return errors;
    }

    public void setErrors(Collection<String> errors) {
        this.errors = errors;
    }
}
