package com.sap.cloud.rest.api.client.model;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents the context in which the exception was thrown, e.g the request
 * that was made and the response received.
 *
 */
public class HttpExchangeContext {

    static final String REQUEST_DISPLAY_NAME = "Request";
    static final String RESPONSE_DISPLAY_NAME = "Response";

    private final Request<String> request;
    private final Response<String> response;

    public HttpExchangeContext(final Request<String> request, final Response<String> response) {
        isNotNull(REQUEST_DISPLAY_NAME, request);
        isNotNull(RESPONSE_DISPLAY_NAME, response);

        this.request = request;
        this.response = response;
    }

    public Request<String> getRequest() {
        return request;
    }

    public Response<String> getResponse() {
        return response;
    }

    public String toString() {
        return new ToStringBuilder(HttpExchangeContext.class.getName(), ToStringStyle.JSON_STYLE)
                .append("request", request)
                .append("response", response)
                .toString();
    }
}
