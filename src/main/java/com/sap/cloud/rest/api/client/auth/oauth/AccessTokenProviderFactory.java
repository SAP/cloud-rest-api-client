package com.sap.cloud.rest.api.client.auth.oauth;

import static java.text.MessageFormat.format;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;
import com.sap.cloud.rest.api.client.config.RestApiClientConfigBuilder;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

public class AccessTokenProviderFactory {

    static final String NON_OAUTH_AUTHENTICATION_MSG = "The provided configuration is required to have an OAuth authentication and it has [{0}] authentication type instead.";

    public static ClientCredentialsAccessTokenProvider createClientCredentialsAccessTokenProvider(
            RestApiClientConfig config) {
        OAuthAuthentication oAuthAuthentication = extractOAuthAuthentication(config);
        OAuthServerConfig oAuthServerConfig = oAuthAuthentication.getOAuthServerConfig();

        RestApiClientConfig restApiClientConfig = RestApiClientConfigBuilder.getBuilder()
                .host(oAuthServerConfig.getOAuthServerHost())
                .basicAuthentication(oAuthServerConfig.getClientID(), oAuthServerConfig.getClientSecret())
                .proxy(config.getRoutePlanner())
                .build();

        return new ClientCredentialsAccessTokenProvider(restApiClientConfig, getApiPath(oAuthAuthentication));
    }

    public static ClientCredentialsAccessTokenProvider createClientCredentialsAccessTokenProvider(
            RestApiClientConfig config, HttpClientProvider httpClientProvider) {
        OAuthAuthentication oAuthAuthentication = extractOAuthAuthentication(config);
        OAuthServerConfig oAuthServerConfig = oAuthAuthentication.getOAuthServerConfig();

        RestApiClientConfig restApiClientConfig = RestApiClientConfigBuilder.getBuilder()
                .host(oAuthServerConfig.getOAuthServerHost())
                .basicAuthentication(oAuthServerConfig.getClientID(), oAuthServerConfig.getClientSecret())
                .proxy(config.getRoutePlanner())
                .build();

        return new ClientCredentialsAccessTokenProvider(restApiClientConfig, httpClientProvider,
                getApiPath(oAuthAuthentication));
    }

    private static OAuthAuthentication extractOAuthAuthentication(RestApiClientConfig config) {
        Authentication authentication = config.getAuthentication();
        AuthenticationType authenticationType = authentication.getAuthenticationType();
        if (authenticationType != AuthenticationType.OAUTH) {
            throw new IllegalArgumentException(format(NON_OAUTH_AUTHENTICATION_MSG, authenticationType));
        }
        return (OAuthAuthentication) config.getAuthentication();
    }

    private static String getApiPath(OAuthAuthentication oauthAuthentication) {
        return oauthAuthentication.getOAuthServerConfig().getoAuthServerApiPath();
    }
}
