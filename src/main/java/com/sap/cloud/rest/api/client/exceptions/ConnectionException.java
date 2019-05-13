package com.sap.cloud.rest.api.client.exceptions;

import java.io.IOException;

import com.sap.cloud.rest.api.client.model.Request;

/**
 * This exception is a wrapper for IO related exceptions.
 * 
 * It contains the request made before the exception occurred.
 *
 */
public class ConnectionException extends RestApiClientException {

    private static final long serialVersionUID = 1L;

    private final transient Request<String> request;

    public ConnectionException(String message, IOException cause, Request<String> request) {
        super(message, cause);

        this.request = request;
    }

    public Request<String> getRequest() {
        return request;
    }
}
