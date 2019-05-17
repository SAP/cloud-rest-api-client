package com.sap.cloud.rest.api.client.auth.cert;

import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContexts;

import com.sap.cloud.rest.api.client.http.HttpClientCreationException;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

/**
 *  Creates HTTP Client with Mutual TLS authentication. 
 */
public class SSLHttpClientProvider extends HttpClientProvider {

    private static final String SSL_CONTEXT_ERROR_MESSAGE = "Could not create SSLContext";

    private static final int DEFAULT_TIMEOUT_MILLIS = 30000;

    private KeystoreConfig keystoreConfig;
    private int timeoutInMillis;

    /**
     * Constructor using Client Certificate Authentication configuration.
     * @param authentication Client Certificate Authentication configuration.
     */
    public SSLHttpClientProvider(ClientCertAuthentication authentication) {
        this(authentication, DEFAULT_TIMEOUT_MILLIS);
    }

    /**
     * Constructor using Client Certificate Authentication configuration.
     * @param authentication authentication Client Certificate Authentication configuration.
     * @param timeoutInMillis Client timeout in milliseconds.
     */
    public SSLHttpClientProvider(ClientCertAuthentication authentication, int timeoutInMillis) {
        this.keystoreConfig = authentication.getKeystoreConfig();
        this.timeoutInMillis = timeoutInMillis;
    }

    @Override
    public HttpClient createHttpClient(HttpRoutePlanner routePlanner) throws HttpClientCreationException {
        try {
            return HttpClients.custom()
                    .useSystemProperties()
                    .setRoutePlanner(routePlanner)
                    .setDefaultRequestConfig(createRequestConfig())
                    .setSSLContext(createSSLContext())
                    .build();
        } catch (GeneralSecurityException e) {
            throw new HttpClientCreationException(SSL_CONTEXT_ERROR_MESSAGE, e);
        }
    }

    private SSLContext createSSLContext() throws KeyManagementException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException {
        KeyStore keystore = keystoreConfig.getKeystore();

        return SSLContexts.custom()
                .loadKeyMaterial(keystore, keystoreConfig.getKeystorePassword(), privateKeyStrategy())
                .build();
    }

    private PrivateKeyStrategy privateKeyStrategy() {
        return (Map<String, PrivateKeyDetails> aliases, Socket socket) -> keystoreConfig.getKeyAlias();
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(timeoutInMillis)
                .setConnectionRequestTimeout(timeoutInMillis)
                .setSocketTimeout(timeoutInMillis)
                .build();
    }
}