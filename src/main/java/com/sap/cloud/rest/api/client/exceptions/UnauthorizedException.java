package com.sap.cloud.rest.api.client.exceptions;

import com.sap.cloud.rest.api.client.model.HttpExchangeContext;

/**
 * This exception represents an HTTP status code 401 Unauthorized.
 *
 */
public class UnauthorizedException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message, HttpExchangeContext context) {
        super(message, context);
    }
}
