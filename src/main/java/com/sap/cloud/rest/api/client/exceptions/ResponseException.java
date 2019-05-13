package com.sap.cloud.rest.api.client.exceptions;

import com.sap.cloud.rest.api.client.model.HttpExchangeContext;
import com.sap.cloud.rest.api.client.model.Request;
import com.sap.cloud.rest.api.client.model.Response;

/**
 * This exception represents that an error response code has been received from
 * the remote service.
 * 
 * It contains the context of the HTTP exchange.
 */
public class ResponseException extends RestApiClientException {

    private static final long serialVersionUID = 1L;

    private final HttpExchangeContext context;

    public ResponseException(String message, HttpExchangeContext context) {
        super(message);

        this.context = context;
    }

    public ResponseException(String message, HttpExchangeContext context, Throwable cause) {
        super(message, cause);

        this.context = context;
    }

    public Request<String> getRequest() {
        return context.getRequest();
    }

    public Response<String> getResponse() {
        return context.getResponse();
    }

}
