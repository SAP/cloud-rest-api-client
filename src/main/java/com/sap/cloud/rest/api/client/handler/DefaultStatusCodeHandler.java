package com.sap.cloud.rest.api.client.handler;

import static java.text.MessageFormat.format;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.sap.cloud.rest.api.client.exceptions.ResponseException;
import com.sap.cloud.rest.api.client.exceptions.UnauthorizedException;
import com.sap.cloud.rest.api.client.model.HttpExchangeContext;

/**
 * This implementation of {@link StatusCodeHandler} has predefined handler for
 * 401 Unauthorized status code and a default one that is used when there is no
 * handler for a given status code.
 * 
 * The default handler throws {@link ResponseException}.
 *
 */
public class DefaultStatusCodeHandler implements StatusCodeHandler {

    static final String UNAUTHORIZED_MSG = "The user is not authorized for the current operation. Context: [{0}]";
    static final String HTTP_RESPOSNE_EXCEPTION_MSG = "An error HTTP response code was received from server. Context: [{0}]";

    private final Map<Integer, ContextHandler> handlers = new HashMap<>();

    protected DefaultStatusCodeHandler() {
        //prevent initialization from constructor
    }

    public DefaultStatusCodeHandler addHandler(int statusCode, ContextHandler handler) {
        handlers.put(statusCode, handler);
        return this;
    }

    public static DefaultStatusCodeHandler create() {
        return new DefaultStatusCodeHandler().addHandler(HttpStatus.SC_UNAUTHORIZED, defaultUnauthorizedHandler());
    }

    protected static ContextHandler defaultUnauthorizedHandler() {
        return context -> {
            throw new UnauthorizedException(format(UNAUTHORIZED_MSG, context), context);
        };
    }

    public void handleStatusCode(int statusCode, HttpExchangeContext context) throws ResponseException {
        handlers.getOrDefault(statusCode, defaultContextHandler()).handle(context);
    }

    protected ContextHandler defaultContextHandler() {
        return context -> {
            defaultStatusCodeHandling(context);
        };
    }

    protected void defaultStatusCodeHandling(HttpExchangeContext context) {
        if (context.getResponse().getStatusCode() >= 300) {
            throw new ResponseException(format(HTTP_RESPOSNE_EXCEPTION_MSG, context), context);
        }
    }
}
