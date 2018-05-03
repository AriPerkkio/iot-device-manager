package web.exception;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import web.domain.response.ErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionHandlingUtils {

    private static final Pattern CONSTAINT_VIOLATION_REGEXP = Pattern.compile("FOREIGN KEY \\(`(.+?)`\\)");

    /**
     * Manages exception handling for repository access. Converts different types of Exceptions into ExceptionWrapper
     *
     * @param e
     *      Exception occurred during repository access
     * @param title
     *      Title used for ExceptionWrapper
     * @throws ExceptionWrapper
     *      Instance of ExceptionWrapper containing converted information about original exception
     */
    public static void validateRepositoryExceptions(Exception e, String title) throws ExceptionWrapper {
        // Initial values
        String message = e.toString();
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;

        if(e instanceof NotFoundException) {
            message = e.getLocalizedMessage();
            errorCode = ErrorCode.NO_ITEMS_FOUND;

        } else if(e instanceof DataIntegrityViolationException) {
            message = "Constraint violation";
            errorCode = ErrorCode.PARAMETER_CONFLICT;

            if(e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) e.getCause();
                String parsedError = parseConstraintViolationException(cve);

                if(parsedError == null) {
                    message = cve.getCause().getLocalizedMessage();
                } else {
                    message = String.format("Constraint error on %s", parsedError);
                }
            }
        } else if(e instanceof DataAccessException || e instanceof HibernateError) {
            message = "Database error occurred";

        } else if(e instanceof ExceptionWrapper) {
            throw (ExceptionWrapper) e;
        }

        throw new ExceptionWrapper(title, message, errorCode);
    }

    /**
     * Parse foreign key from constraint validation exception. Try-catch due to unstable implementation
     *
     * @param e
     *      Exception thrown
     * @return
     *      Parsed string containing foreign key
     */
    private static String parseConstraintViolationException(ConstraintViolationException e) {
            try {
                String sqlError = e.getCause().getLocalizedMessage();

                final Matcher matcher = CONSTAINT_VIOLATION_REGEXP.matcher(sqlError);
                matcher.find();

                return matcher.group(1);
            } catch (Exception ex) {
                return null;
            }
        }

    /**
     * Throws NotFoundException with fixed prefix message
     *
     * @param parameters
     *      Postfix added to fixed exception message
     * @throws NotFoundException
     *      NotFoundException thrown with message describing error
     */
    public static void throwNotFoundException(String parameters) throws NotFoundException {
        throw new NotFoundException("Items not found using given parameters: " + parameters);
    }
}
