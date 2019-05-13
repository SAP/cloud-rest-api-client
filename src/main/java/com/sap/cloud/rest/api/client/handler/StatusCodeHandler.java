package com.sap.cloud.rest.api.client.handler;

import com.sap.cloud.rest.api.client.exceptions.ResponseException;
import com.sap.cloud.rest.api.client.model.HttpExchangeContext;

/**
 * This class is a container for context handlers. It chooses which handler to
 * execute based on the given status code.
 */
public interface StatusCodeHandler {

    /**
     * Adds a handler for the given status code.
     * 
     * @param statusCode
     *            the status code, for which the handler should be used.
     * @param handler
     *            the handler to be used for the given status code.
     * @return the same class instance
     */
    StatusCodeHandler addHandler(int statusCode, ContextHandler handler);

    /**
     * Executes the context handler corresponding to the given status code.
     * 
     * @param statusCode
     *            the status code that should be handled.
     * @param context
     *            HTTP context, for which the status code was returned.
     */
    void handleStatusCode(int statusCode, HttpExchangeContext context) throws ResponseException;

    /**
     * Represents a function that is executed by the {@link StatusCodeHandler}
     * for the corresponding status code.
     *
     */
    @FunctionalInterface
    static interface ContextHandler {

        void handle(HttpExchangeContext context) throws ResponseException;

    }
}
