package com.sap.cloud.rest.api.client.auth.cert;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.security.KeyStore;

import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.auth.cert.ClientCertAuthentication;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfig;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfigBuilder;

public class ClientCertAuthenticationTest {

    private static final KeyStore KEYSTORE = mock(KeyStore.class);
    private static final char[] PASSWORD = "password".toCharArray();
    private static final String ALIAS = "alias";

    private static final KeystoreConfig KEYSTORE_CONFIG = KeystoreConfigBuilder.getBuilder()
            .keystore(KEYSTORE)
            .keystorePassword(PASSWORD)
            .keyAlias(ALIAS)
            .build();

    @Test
    public void validClientCertAuthenticationTest() {
        ClientCertAuthentication authentication = new ClientCertAuthentication(KEYSTORE_CONFIG);

        assertEquals(KEYSTORE_CONFIG, authentication.getKeystoreConfig());
        assertEquals(AuthenticationType.CLIENT_CERT, authentication.getAuthenticationType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullKeystoreConfigClientCertAuthenticationTest() {
       new ClientCertAuthentication(null);
    }
}
