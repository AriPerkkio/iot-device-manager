package web.controller;

import net.hamnaberg.json.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import web.domain.response.ErrorCode;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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

    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, ConstraintViolationException.class })
    public ResponseWrapper invalidParameterHandling(Exception ex, HttpServletRequest request) throws Exception {

        // TODO remove, leaking internal information
        String message = ex.toString();

        if(ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException mismatchEx = (MethodArgumentTypeMismatchException) ex;
            message = String.format("Invalid value (%s) for parameter %s", mismatchEx.getName(), mismatchEx.getValue().toString());
        } else if(ex instanceof MissingServletRequestParameterException) {
            message = String.format("Missing request parameter %s", ((MissingServletRequestParameterException) ex).getParameterName());
        } else if(ex instanceof  HttpMessageNotReadableException) {
            message = "Request body missing or invalid";
        } else if(ex instanceof ConstraintViolationException) {
            message = "";
            for(ConstraintViolation cv: ((ConstraintViolationException) ex).getConstraintViolations()) {
                message = message.concat(cv.getInvalidValue() + " " + cv.getMessage() + ". ");
            }
            message = message.trim();
        }

        Error error = Error.create("Parameter validation error", ErrorCode.PARAMETER_VALIDATION_ERROR.getCode(), message);
        URI href = new URI(request.getRequestURL().toString());

        return new ResponseWrapper(error.toCollection(href), httpHeaders, BAD_REQUEST);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseWrapper httpRequestExceptionHandler(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) throws Exception {
        String message = String.format("Method %s not supported for %s",
            ex.getMethod(),
            request.getServletPath());

        Error error = Error.create("HTTP request error", ErrorCode.INTERNAL_ERROR.getCode(), message);
        URI href = new URI(request.getRequestURL().toString());

        return new ResponseWrapper(error.toCollection(href), httpHeaders, NOT_IMPLEMENTED);
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
