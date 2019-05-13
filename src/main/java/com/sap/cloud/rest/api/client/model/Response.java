package com.sap.cloud.rest.api.client.model;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * Represents an HTTP response. Holds the {@link HttpResponse} and the response
 * entity.
 * 
 * @param <T>
 *            the type of the response entity
 */
public class Response<T> {

    private static final String HTTP_RESPONSE_DISPLAY_NAME = "Http response";

    private final HttpResponse httpResponse;

    private final T entity;

    public Response(HttpResponse httpResponse, T responseEntity) {
        isNotNull(HTTP_RESPONSE_DISPLAY_NAME, httpResponse);

        this.httpResponse = httpResponse;
        this.entity = responseEntity;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public int getStatusCode() {
        return httpResponse.getStatusLine().getStatusCode();
    }

    public Header[] getHeaders() {
        return httpResponse.getAllHeaders();
    }

    public T getEntity() {
        return entity;
    }

    public String toString() {
        return new ToStringBuilder(Response.class.getName(), ToStringStyle.JSON_STYLE)
                .append("httpResponse", httpResponse)
                .append("responseEntity", entity)
                .toString();
    }
}