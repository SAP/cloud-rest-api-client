package com.sap.cloud.rest.api.client.utils.test;

import org.apache.http.client.methods.HttpUriRequest;
import org.mockito.ArgumentMatcher;

/**
 * Custom matcher that accepts an {@link HttpUriRequest} argument with the given
 * url.
 *
 */
public class RequestMatcher extends ArgumentMatcher<HttpUriRequest> {

    private String url;
    private String method;

    public RequestMatcher(String method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public boolean matches(Object argument) {
        HttpUriRequest request = (HttpUriRequest) argument;

        return method.equals(request.getMethod()) && url.equals(request.getURI().toString());
    }

}
