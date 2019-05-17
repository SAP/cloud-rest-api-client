package com.sap.cloud.rest.api.client.auth.cert;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;

/**
 * A class that represents client cert authentication for
 * {@link RestApiClientConfig}.
 */
public class ClientCertAuthentication implements Authentication {

    private static final String KEYSTORE_CONFIG_DISPLAY_NAME = "Keystore configuration";

    private final KeystoreConfig keystoreConfig;

    /**
     * Creates ClientCestAuthentication using specified keystore.
     * @param keystoreConfig Keystore configuration.
     */
    public ClientCertAuthentication(KeystoreConfig keystoreConfig) {
        isNotNull(KEYSTORE_CONFIG_DISPLAY_NAME, keystoreConfig);

        this.keystoreConfig = keystoreConfig;
    }

    /**
     * @return Returns the {@link KeystoreConfig}
     */
    public KeystoreConfig getKeystoreConfig() {
        return keystoreConfig;
    }

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.CLIENT_CERT;
    }
}
