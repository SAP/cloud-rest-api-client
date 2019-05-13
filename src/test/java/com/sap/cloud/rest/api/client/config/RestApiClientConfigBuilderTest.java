package com.sap.cloud.rest.api.client.config;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.net.URL;
import java.security.KeyStore;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.protocol.BasicHttpContext;
import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.auth.basic.BasicAuthentication;
import com.sap.cloud.rest.api.client.auth.cert.ClientCertAuthentication;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfig;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfigBuilder;
import com.sap.cloud.rest.api.client.auth.none.NoAuthentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthAuthentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;
import com.sap.cloud.rest.api.client.config.RestApiClientConfigBuilder;
import com.sap.cloud.rest.api.client.http.Proxy;

public class RestApiClientConfigBuilderTest {

    private static final char[] TEST_CLIENT_SECRET = "testClientSecret".toCharArray();
    private static final String TEST_CLIENT_ID = "testClientID";
    private static final String TEST_OAUTH_SERVER_URL = "https://testOauthServerURL";
    private static final String TEST_HOSTNAME = "www.example.com";
    private static final String TEST_HOST = "https://" + TEST_HOSTNAME;
    private static final String TEST_HOST_WITH_PORT = "https://www.example.com:69";
    private static final String TEST_USER = "example_user";
    private static final KeyStore TEST_KEYSTORE = mock(KeyStore.class);
    private static final char[] TEST_PASSWORD = "example-password".toCharArray();
    private static final String TEST_ALIAS = "exampleAlias";
    private static final KeystoreConfig TEST_KEYSTORE_CONFIG = KeystoreConfigBuilder.getBuilder()
            .keystore(TEST_KEYSTORE)
            .keystorePassword(TEST_PASSWORD)
            .keyAlias(TEST_ALIAS)
            .build();

    private static final String PROXY_HOST = "proxy.int";
    private static final int PROXY_PORT = 8888;
    private static final Proxy PROXY_CREDENTIALS = new Proxy(PROXY_HOST, PROXY_PORT);
    private static final HttpHost PROXY_HTTP_HOST = new HttpHost(PROXY_HOST, PROXY_PORT,
            PROXY_CREDENTIALS.getProxyScheme());

    @Test
    public void buildWithHostOnlyTest() {
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .build();

        assertEquals(config.getHost(), TEST_HOST);

        // Assert default authentication is used
        NoAuthentication authentication = (NoAuthentication) config.getAuthentication();
        assertEquals(AuthenticationType.NO_AUTH, authentication.getAuthenticationType());
    }

    @Test
    public void buildWithAuthenticationTest() {
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .authentication(new BasicAuthentication(TEST_USER, TEST_PASSWORD))
                .build();

        BasicAuthentication authentication = (BasicAuthentication) config.getAuthentication();
        assertEquals(TEST_USER, authentication.getUsername());
        assertArrayEquals(TEST_PASSWORD, authentication.getPassword());
        assertEquals(AuthenticationType.BASIC, authentication.getAuthenticationType());
    }

    @Test
    public void buildWithBasicAuthTest() {
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .basicAuthentication(TEST_USER, TEST_PASSWORD)
                .build();

        BasicAuthentication authentication = (BasicAuthentication) config.getAuthentication();
        assertEquals(TEST_USER, authentication.getUsername());
        assertArrayEquals(TEST_PASSWORD, authentication.getPassword());
        assertEquals(AuthenticationType.BASIC, authentication.getAuthenticationType());
    }

    @Test
    public void buildWithClientCertAuthTest() {
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .clientCertAuthentication(TEST_KEYSTORE_CONFIG)
                .build();

        ClientCertAuthentication authentication = (ClientCertAuthentication) config.getAuthentication();
        assertEquals(TEST_KEYSTORE_CONFIG, authentication.getKeystoreConfig());
        assertEquals(AuthenticationType.CLIENT_CERT, authentication.getAuthenticationType());
    }

    @Test
    public void buildWithOAuthTest() {
        OAuthServerConfig oAuthServerConfig = new OAuthServerConfig(TEST_OAUTH_SERVER_URL, TEST_CLIENT_ID,
                TEST_CLIENT_SECRET);
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .oAuthAuthentication(oAuthServerConfig)
                .build();

        OAuthAuthentication authentication = (OAuthAuthentication) config.getAuthentication();
        assertEquals(TEST_OAUTH_SERVER_URL, authentication.getOAuthServerConfig().getOAuthServerHost());
        assertEquals(TEST_CLIENT_ID, authentication.getOAuthServerConfig().getClientID());
        assertArrayEquals(TEST_CLIENT_SECRET, authentication.getOAuthServerConfig().getClientSecret());
        assertEquals(AuthenticationType.OAUTH, authentication.getAuthenticationType());
    }

    @Test
    public void buildWithNoAuthTest() {
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .noAuthentication()
                .build();

        NoAuthentication authentication = (NoAuthentication) config.getAuthentication();
        assertEquals(AuthenticationType.NO_AUTH, authentication.getAuthenticationType());
    }

    @Test
    public void buildWithProxyTest() throws Exception {
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .proxy(PROXY_CREDENTIALS)
                .build();

        HttpRoute route = config.getRoutePlanner()
                .determineRoute(new HttpHost(TEST_HOSTNAME), RequestBuilder.get().build(), new BasicHttpContext());

        assertEquals(2, route.getHopCount());
        assertEquals(PROXY_HTTP_HOST, route.getHopTarget(0));
        assertEquals(PROXY_HTTP_HOST, route.getProxyHost());
    }

    @Test
    public void buildWithNullProxyTest() throws Exception {
        Proxy nullProxy = null;
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .proxy(nullProxy)
                .build();

        assertTrue(config.getRoutePlanner() instanceof DefaultRoutePlanner);
    }

    @Test
    public void buildWithRoutePlannerTest() throws Exception {
        HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(PROXY_HTTP_HOST);
                
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .proxy(routePlanner)
                .build();

        HttpRoute route = config.getRoutePlanner()
                .determineRoute(new HttpHost(TEST_HOSTNAME), RequestBuilder.get().build(), new BasicHttpContext());

        assertEquals(2, route.getHopCount());
        assertEquals(PROXY_HTTP_HOST, route.getHopTarget(0));
        assertEquals(PROXY_HTTP_HOST, route.getProxyHost());
    }

    @Test
    public void buildWithNullRoutePlannerTest() throws Exception {
        HttpRoutePlanner nullRoutePlanner = null;
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .proxy(nullRoutePlanner)
                .build();

        assertTrue(config.getRoutePlanner() instanceof DefaultRoutePlanner);
    }

    @Test
    public void buildWithProxyAndAuthenticationTest() throws Exception {
        RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .authentication(new BasicAuthentication(TEST_USER, TEST_PASSWORD))
                .proxy(PROXY_CREDENTIALS)
                .build();

        BasicAuthentication authentication = (BasicAuthentication) config.getAuthentication();
        assertEquals(TEST_USER, authentication.getUsername());
        assertArrayEquals(TEST_PASSWORD, authentication.getPassword());
        assertEquals(AuthenticationType.BASIC, authentication.getAuthenticationType());

        HttpRoute route = config.getRoutePlanner()
                .determineRoute(new HttpHost(TEST_HOSTNAME), RequestBuilder.get().build(), new BasicHttpContext());

        assertEquals(2, route.getHopCount());
        assertEquals(PROXY_HTTP_HOST, route.getHopTarget(0));
        assertEquals(PROXY_HTTP_HOST, route.getProxyHost());
    }

    @Test
    public void buildWithHostFromUrlTest() throws Exception {
        final RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .noAuthentication()
                .host(new URL(TEST_HOST))
                .build();

        assertEquals(TEST_HOST, config.getHost());
    }

    @Test
    public void buildWithHostWithPortFromUrlTest() throws Exception {
        final RestApiClientConfig config = RestApiClientConfigBuilder.getBuilder()
                .noAuthentication()
                .host(new URL(TEST_HOST_WITH_PORT))
                .build();

        assertEquals(TEST_HOST_WITH_PORT, config.getHost());
    }

    @Test
    public void getRestApiClientBuilderTest() {
        assertTrue(RestApiClientConfigBuilder.getBuilder() instanceof RestApiClientConfigBuilder);
    }
}
