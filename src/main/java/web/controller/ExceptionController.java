package web.controller;

import net.hamnaberg.json.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import web.domain.response.ErrorCode;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionWrapper;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionController {

    private final HttpHeaders httpHeaders;

    ExceptionController(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    @ExceptionHandler({Exception.class})
    public ResponseWrapper unhandledExceptionHandler(Exception ex, HttpServletRequest request) throws Exception {
        Error error = Error.create("Unhandled error", ErrorCode.INTERNAL_ERROR.getCode(), ex.toString());
        URI href = new URI(request.getRequestURL().toString());

        return new ResponseWrapper(error.toCollection(href), httpHeaders, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ ExceptionWrapper.class })
    public ResponseWrapper exceptionHandler(ExceptionWrapper ex, HttpServletRequest request) throws Exception {
        Error error = Error.create(ex.getTitle(), ex.getCodeAsString(), ex.getMessage());
        URI href = new URI(request.getRequestURL().toString());

        return new ResponseWrapper(error.toCollection(href), httpHeaders,resolveHttpStatus(ex.getCode()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseWrapper invalidParameterHandling(Exception ex, HttpServletRequest request) throws Exception {

        // TODO remove, leaking internal information
        String message = ex.toString();

        if(ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException mismatchEx = (MethodArgumentTypeMismatchException) ex;
            message = String.format("Invalid value (%s) for parameter %s", mismatchEx.getName(), mismatchEx.getValue().toString());
        } else if(ex instanceof  HttpMessageNotReadableException) {
            message = "Request body missing or invalid";
        }

        Error error = Error.create("Parameter validation error", ErrorCode.PARAMETER_VALIDATION_ERROR.getCode(), message);
        URI href = new URI(request.getRequestURL().toString());

        return new ResponseWrapper(error.toCollection(href), httpHeaders, BAD_REQUEST);
    }

    private HttpStatus resolveHttpStatus(ErrorCode errorCode) {
        switch(errorCode) {
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
}
