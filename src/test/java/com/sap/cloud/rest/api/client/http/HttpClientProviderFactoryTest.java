package com.sap.cloud.rest.api.client.http;

import static com.sap.cloud.rest.api.client.utils.test.HttpClientProviderUtils.getHttpClientProviderForAuthentication;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.basic.BasicAuthentication;
import com.sap.cloud.rest.api.client.auth.basic.BasicHttpClientProvider;
import com.sap.cloud.rest.api.client.auth.cert.ClientCertAuthentication;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfig;
import com.sap.cloud.rest.api.client.auth.cert.SSLHttpClientProvider;
import com.sap.cloud.rest.api.client.auth.none.NoAuthHttpClientProvider;
import com.sap.cloud.rest.api.client.auth.none.NoAuthentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthAuthentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthHttpClientProvider;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

public class HttpClientProviderFactoryTest {

    @Test
    public void createBasicHttpClientProviderTest() {
        Authentication authentication = new BasicAuthentication("username", "password".toCharArray());
        HttpClientProvider basicHttpClientProvider = getHttpClientProviderForAuthentication(authentication);
        assertTrue(basicHttpClientProvider instanceof BasicHttpClientProvider);
    }

    @Test
    public void createClientCertHttpClientProviderTest() {
        Authentication authentication = new ClientCertAuthentication(mock(KeystoreConfig.class));
        HttpClientProvider clientCertHttpClientProvider = getHttpClientProviderForAuthentication(authentication);
        assertTrue(clientCertHttpClientProvider instanceof SSLHttpClientProvider);
    }

    @Test
    public void createOauthHttpClientProviderTest() {
        OAuthServerConfig oAuthServerConfig = new OAuthServerConfig("http://test", "dummyClientID",
                "dummyClientSecret".toCharArray());
        Authentication authentication = new OAuthAuthentication(oAuthServerConfig);
        HttpClientProvider oauthHttpClientProvider = getHttpClientProviderForAuthentication(authentication);
        assertTrue(oauthHttpClientProvider instanceof OAuthHttpClientProvider);
    }

    @Test
    public void createNoAuthHttpClientProviderTest() {
        Authentication authentication = new NoAuthentication();
        HttpClientProvider noAuthHttpClientProvider = getHttpClientProviderForAuthentication(authentication);
        ;
        assertTrue(noAuthHttpClientProvider instanceof NoAuthHttpClientProvider);
    }
}
