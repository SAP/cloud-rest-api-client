package com.sap.cloud.rest.api.client.exceptions;

/**
 * This exception is a base class for all other exceptions thrown by the client.
 */
public class RestApiClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RestApiClientException(String message) {
        super(message);
    }

    public RestApiClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
