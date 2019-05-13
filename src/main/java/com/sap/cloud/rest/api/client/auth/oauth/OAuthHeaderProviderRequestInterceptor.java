package com.sap.cloud.rest.api.client.auth.oauth;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * HTTP request interceptor called during the Rest API Client execution in case
 * of OAuth authentication. It retrieves the access token and adds it as a
 * Bearer header to the request. The header key is provided in the constructor.
 *
 */
public class OAuthHeaderProviderRequestInterceptor implements HttpRequestInterceptor {

    private static final String BEARER_HEADER_PREFIX = "Bearer ";

    private final AccessTokenProvider accessTokenProvider;
    private final String oAuthHeaderKey;

    public OAuthHeaderProviderRequestInterceptor(AccessTokenProvider accessTokenProvider, String oAuthHeaderKey) {
        this.accessTokenProvider = accessTokenProvider;
        this.oAuthHeaderKey = oAuthHeaderKey;
    }

    @Override
    public void process(final HttpRequest request, final HttpContext context) {
        request.addHeader(oAuthHeaderKey, BEARER_HEADER_PREFIX + accessTokenProvider.retrieveAccessToken());
    }

}
