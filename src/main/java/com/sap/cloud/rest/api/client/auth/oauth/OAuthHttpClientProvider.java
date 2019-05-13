package com.sap.cloud.rest.api.client.auth.oauth;

import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.HttpClientBuilder;

import com.sap.cloud.rest.api.client.http.HttpClientCreationException;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

public class OAuthHttpClientProvider extends HttpClientProvider {

    private final AccessTokenProvider accessTokenProvider;
    private final String oAuthHeaderKey;

    public OAuthHttpClientProvider(AccessTokenProvider accessTokenProvider) {
        this(accessTokenProvider, HttpHeaders.AUTHORIZATION);
    }
    
    public OAuthHttpClientProvider(AccessTokenProvider accessTokenProvider, String oAuthHeaderKey) {
        this.accessTokenProvider = accessTokenProvider;
        this.oAuthHeaderKey = oAuthHeaderKey;
    }

    @Override
    public HttpClient createHttpClient(HttpRoutePlanner routePlanner) throws HttpClientCreationException {
        return HttpClientBuilder.create()
                .useSystemProperties()
                .setRoutePlanner(routePlanner)
                .addInterceptorLast(new OAuthHeaderProviderRequestInterceptor(accessTokenProvider, oAuthHeaderKey))
                .build();
    }

}
