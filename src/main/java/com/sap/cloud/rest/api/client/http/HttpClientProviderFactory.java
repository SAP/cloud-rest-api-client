package com.sap.cloud.rest.api.client.http;

import static com.sap.cloud.rest.api.client.auth.oauth.AccessTokenProviderFactory.createClientCredentialsAccessTokenProvider;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.basic.BasicAuthentication;
import com.sap.cloud.rest.api.client.auth.basic.BasicHttpClientProvider;
import com.sap.cloud.rest.api.client.auth.cert.ClientCertAuthentication;
import com.sap.cloud.rest.api.client.auth.cert.SSLHttpClientProvider;
import com.sap.cloud.rest.api.client.auth.none.NoAuthHttpClientProvider;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthAuthentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthHttpClientProvider;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;

public class HttpClientProviderFactory {

    public static HttpClientProvider createHttpClientProvider(RestApiClientConfig config) {
        Authentication authentication = config.getAuthentication();
        switch (authentication.getAuthenticationType()) {
        case BASIC:
            return new BasicHttpClientProvider((BasicAuthentication) authentication);
        case CLIENT_CERT:
            return new SSLHttpClientProvider((ClientCertAuthentication) authentication);
        case OAUTH:
            OAuthAuthentication oAuthAuthentication = (OAuthAuthentication) authentication;
            return new OAuthHttpClientProvider(
                    createClientCredentialsAccessTokenProvider(config),
                    oAuthAuthentication.getOAuthServerConfig().getoAuthHeaderKey());
        case NO_AUTH:
        default:
            return new NoAuthHttpClientProvider();
        }
    }
}
