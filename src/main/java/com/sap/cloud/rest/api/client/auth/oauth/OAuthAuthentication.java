package com.sap.cloud.rest.api.client.auth.oauth;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;

/**
 * A class that represents OAuth authentication for {@link RestApiClientConfig}.
 * Requires {@link OAuthServerConfig}.
 */
public class OAuthAuthentication implements Authentication {

    private static final String OAUTH_SERVER_CONFIG_DISPLAY_NAME = "OAuth Server Config";

    private final OAuthServerConfig oauthConfig;

    public OAuthAuthentication(OAuthServerConfig config) {
        isNotNull(OAUTH_SERVER_CONFIG_DISPLAY_NAME, config);
        
        this.oauthConfig = config;
    }

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.OAUTH;
    }

    public OAuthServerConfig getOAuthServerConfig() {
        return this.oauthConfig;
    }

}
