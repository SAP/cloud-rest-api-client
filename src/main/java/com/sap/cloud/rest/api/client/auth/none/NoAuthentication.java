package com.sap.cloud.rest.api.client.auth.none;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;

/**
 * A class that represents no authentication for {@link RestApiClientConfig}.
 */
public class NoAuthentication implements Authentication {

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.NO_AUTH;
    }
}
