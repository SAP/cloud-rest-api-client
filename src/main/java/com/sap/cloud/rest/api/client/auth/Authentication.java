package com.sap.cloud.rest.api.client.auth;

import com.sap.cloud.rest.api.client.config.RestApiClientConfig;

/**
 * An interface that represents an authentication used by
 * {@link RestApiClientConfig}
 *
 */
public interface Authentication {

    /**
     * @return Returns the {@link AuthenticationType} value.
     */
    AuthenticationType getAuthenticationType();
}
