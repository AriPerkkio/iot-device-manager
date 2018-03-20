package web.domain.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ErrorWrapper {
    private final ErrorCode errorCode;
    private final String errorMessage;

    public ErrorWrapper(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("errorCode", errorCode)
            .append("errorMessage", errorMessage)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ErrorWrapper that = (ErrorWrapper) o;

        return new EqualsBuilder()
            .append(errorCode, that.errorCode)
            .append(errorMessage, that.errorMessage)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(errorCode)
            .append(errorMessage)
            .toHashCode();
    }
}
