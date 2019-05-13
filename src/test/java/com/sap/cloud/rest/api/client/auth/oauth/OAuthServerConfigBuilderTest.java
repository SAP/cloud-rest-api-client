package com.sap.cloud.rest.api.client.auth.oauth;

import static com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig.DEFAULT_OAUTH_HEADER_KEY;
import static com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig.DEFAULT_OAUTH_SERVER_API_PATH;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.apache.http.HttpHeaders;
import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfigBuilder;

public class OAuthServerConfigBuilderTest {

    private static final String TEST_OAUTH_SERVER_HOST = "https://testOauthServerHost";
    private static final String TEST_OAUTH_SERVER_HOST_WITH_PORT = "https://testOauthServerHost:666";
    private static final String TEST_OAUTH_SERVER_API_PATH = "/test/api";
    private static final char[] TEST_CLIENT_SECRET = "testClientSecret".toCharArray();
    private static final String TEST_CLIENT_ID = "testClientID";
    private static final String TEST_OAUTH_HEADER_KEY = HttpHeaders.PROXY_AUTHORIZATION;

    @Test
    public void testBuildWithValidData() {
        OAuthServerConfig config = OAuthServerConfigBuilder.getBuilder()
                .oAuthServerHost(TEST_OAUTH_SERVER_HOST)
                .clientSecret(TEST_CLIENT_SECRET)
                .clientID(TEST_CLIENT_ID)
                .build();

        assertEquals(TEST_OAUTH_SERVER_HOST, config.getOAuthServerHost());
        assertEquals(DEFAULT_OAUTH_SERVER_API_PATH, config.getoAuthServerApiPath());
        assertEquals(DEFAULT_OAUTH_HEADER_KEY, config.getoAuthHeaderKey());
        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertArrayEquals(TEST_CLIENT_SECRET, config.getClientSecret());
    }

    @Test
    public void testBuildWithValidDataProvidedApiPath() {
        OAuthServerConfig config = OAuthServerConfigBuilder.getBuilder()
                .oAuthServerHost(TEST_OAUTH_SERVER_HOST)
                .oAuthServerApiPath(TEST_OAUTH_SERVER_API_PATH)
                .clientSecret(TEST_CLIENT_SECRET)
                .clientID(TEST_CLIENT_ID)
                .build();

        assertEquals(TEST_OAUTH_SERVER_HOST, config.getOAuthServerHost());
        assertEquals(TEST_OAUTH_SERVER_API_PATH, config.getoAuthServerApiPath());
        assertEquals(DEFAULT_OAUTH_HEADER_KEY, config.getoAuthHeaderKey());
        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertArrayEquals(TEST_CLIENT_SECRET, config.getClientSecret());
    }

    @Test
    public void testBuildWithValidDataProvidedHeaderKey() {
        OAuthServerConfig config = OAuthServerConfigBuilder.getBuilder()
                .oAuthServerHost(TEST_OAUTH_SERVER_HOST)
                .oAuthHeaderKey(TEST_OAUTH_HEADER_KEY)
                .clientSecret(TEST_CLIENT_SECRET)
                .clientID(TEST_CLIENT_ID)
                .build();

        assertEquals(TEST_OAUTH_SERVER_HOST, config.getOAuthServerHost());
        assertEquals(DEFAULT_OAUTH_SERVER_API_PATH, config.getoAuthServerApiPath());
        assertEquals(TEST_OAUTH_HEADER_KEY, config.getoAuthHeaderKey());
        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertArrayEquals(TEST_CLIENT_SECRET, config.getClientSecret());
    }

    @Test
    public void testBuildWithValidDataProvidedApiPathAndHeaderKey() {
        OAuthServerConfig config = OAuthServerConfigBuilder.getBuilder()
                .oAuthServerHost(TEST_OAUTH_SERVER_HOST)
                .oAuthServerApiPath(TEST_OAUTH_SERVER_API_PATH)
                .oAuthHeaderKey(TEST_OAUTH_HEADER_KEY)
                .clientSecret(TEST_CLIENT_SECRET)
                .clientID(TEST_CLIENT_ID)
                .build();

        assertEquals(TEST_OAUTH_SERVER_HOST, config.getOAuthServerHost());
        assertEquals(TEST_OAUTH_SERVER_API_PATH, config.getoAuthServerApiPath());
        assertEquals(TEST_OAUTH_HEADER_KEY, config.getoAuthHeaderKey());
        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertArrayEquals(TEST_CLIENT_SECRET, config.getClientSecret());
    }

    @Test
    public void testHostFromUrlWithoutPort() throws Exception {
        final OAuthServerConfig config = OAuthServerConfigBuilder.getBuilder()
                .oAuthServerHost(new URL(TEST_OAUTH_SERVER_HOST))
                .clientID(TEST_CLIENT_ID)
                .clientSecret(TEST_CLIENT_SECRET)
                .build();

        assertEquals(TEST_OAUTH_SERVER_HOST, config.getOAuthServerHost());
    }

    @Test
    public void testHostFromUrlWithPort() throws Exception {
        final OAuthServerConfig config = OAuthServerConfigBuilder.getBuilder()
                .oAuthServerHost(new URL(TEST_OAUTH_SERVER_HOST_WITH_PORT))
                .clientID(TEST_CLIENT_ID)
                .clientSecret(TEST_CLIENT_SECRET)
                .build();

        assertEquals(TEST_OAUTH_SERVER_HOST_WITH_PORT, config.getOAuthServerHost());
    }

}
