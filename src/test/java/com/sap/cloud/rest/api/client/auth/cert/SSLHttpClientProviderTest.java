package com.sap.cloud.rest.api.client.auth.cert;

import static com.sap.cloud.rest.api.client.utils.test.HttpClientProviderUtils.getHttpClientProviderForAuthentication;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.http.client.HttpClient;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.cert.ClientCertAuthentication;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfigBuilder;
import com.sap.cloud.rest.api.client.auth.cert.SSLHttpClientProvider;
import com.sap.cloud.rest.api.client.http.HttpClientCreationException;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

public class SSLHttpClientProviderTest {

    private static final String KEY_ALIAS = "testAlias";
    private static char[] KEYSTORE_PASSWORD = "test1234".toCharArray();
    private static final String KEYSTORE_TYPE = "JKS";
    private static KeyStore keystore;

    @BeforeClass
    public static void setup() throws GeneralSecurityException, IOException {
        String path = Thread
                .currentThread()
                .getContextClassLoader()
                .getResource("test-keystore.jks")
                .getPath();

        keystore = loadKeystore(path);
    }

    @Test
    public void createHttpClient_notNull() throws HttpClientCreationException {
        ClientCertAuthentication authentication = new ClientCertAuthentication(KeystoreConfigBuilder.getBuilder()
                .keystore(keystore)
                .keystorePassword(KEYSTORE_PASSWORD)
                .keyAlias(KEY_ALIAS)
                .build());

        HttpClientProvider httpClientProvider = getHttpClientProviderForAuthentication(authentication);

        assertTrue(httpClientProvider instanceof SSLHttpClientProvider);

        HttpClient httpClient = httpClientProvider.createHttpClient();
        assertNotNull(httpClient);
    }

    @Test(expected = HttpClientCreationException.class)
    public void createHttpClientWrongKeystorePassword_throwsException() throws HttpClientCreationException {
        ClientCertAuthentication authentication = new ClientCertAuthentication(KeystoreConfigBuilder.getBuilder()
                .keystore(keystore)
                .keystorePassword("wrong-password".toCharArray())
                .keyAlias(KEY_ALIAS)
                .build());

        HttpClientProvider httpClientProvider = getHttpClientProviderForAuthentication(authentication);

        httpClientProvider.createHttpClient();
    }

    private static KeyStore loadKeystore(String path)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
        InputStream keystoreInputStream = new FileInputStream(path);

        keystore.load(keystoreInputStream, KEYSTORE_PASSWORD);

        return keystore;
    }
}
