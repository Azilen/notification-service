package com.azilen.web.rest.errors;

public class SendNotificationException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private Integer errorCode;

    /**
     * Instantiates a new entity not found exception.
     */
    public SendNotificationException() {
        super();
    }

    /**
     * Instantiates a new entity not found exception.
     *
     * @param message the message
     * @param  errorCode the errorCode
     */
    public SendNotificationException(final String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;

    }

    /**
     * Instantiates a new entity not found exception.
     *
     * @param cause the cause
     * @param errorCode the errorCode
     */
    public SendNotificationException(Throwable cause, Integer errorCode) {
        super(cause);
        this.errorCode = errorCode;

    }

    /**
     * Instantiates a new entity not found exception.
     *
     * @param message the message
     * @param cause   the cause * @param errorCode the errorCode
     */
    public SendNotificationException(final String message, final Throwable cause, Integer errorCode) {
        super(message, cause);
        this.errorCode = errorCode;

    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
