package com.sap.cloud.rest.api.client.model;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.PROXY_AUTHENTICATE;
import static org.apache.http.HttpHeaders.PROXY_AUTHORIZATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;

/**
 * Represents an HTTP request. Contains the {@link HttpUriRequest} and the
 * request body.
 */
public class Request<T> {

    static final String MASKED_VALUE = "********";

    private static final List<String> AUTHORIZATION_HEADERS_LIST = Arrays.asList(
            AUTHORIZATION, PROXY_AUTHENTICATE, PROXY_AUTHORIZATION);

    private static final String HTTP_URI_REQUEST_DISPLAY_NAME = "Http request";

    private final HttpUriRequest httpUriRequest;

    private final T entity;

    public Request(HttpUriRequest httpUriRequest) {
        this(httpUriRequest, null);
    }

    public Request(HttpUriRequest httpUriRequest, T body) {
        isNotNull(HTTP_URI_REQUEST_DISPLAY_NAME, httpUriRequest);

        this.httpUriRequest = httpUriRequest;
        this.entity = body;
    }

    public HttpUriRequest getHttpRequest() {
        return httpUriRequest;
    }

    public Header[] getHeaders() {
        return httpUriRequest.getAllHeaders();
    }

    public T getEntity() {
        return entity;
    }

    /**
     * Returns a String representation of the {@link Request}. Hides the
     * Authorization header if existent.
     */
    public String toString() {
        return new ToStringBuilder(Request.class.getName(), ToStringStyle.JSON_STYLE)
                .append("httpRequest", httpUriRequest)
                .append("requestHeaders", Arrays.toString(sanitizeSensitiveHeaders(getHeaders())))
                .append("requestEntity", entity)
                .toString();
    }

    private Header[] sanitizeSensitiveHeaders(Header[] headers) {
        List<Header> headerList = new ArrayList<>(Arrays.asList(headers))
                .stream()
                .map(this::sanitizeHeader)
                .collect(Collectors.toList());
        
        return headerList.toArray(new Header[headerList.size()]);
    }

    private Header sanitizeHeader(Header header) {
        if (isAuthorizationHeader(header)) {
            return new BasicHeader(header.getName(), MASKED_VALUE);
        }
        return header;
    }

    private boolean isAuthorizationHeader(Header header) {
        String headerName = header.getName();
        if (AUTHORIZATION_HEADERS_LIST.contains(headerName)) {
            return true;
        }
        return false;
    }
}