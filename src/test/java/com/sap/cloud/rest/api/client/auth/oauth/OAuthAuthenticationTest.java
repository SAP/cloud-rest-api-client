package com.sap.cloud.rest.api.client.auth.oauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthAuthentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfigBuilder;

public class OAuthAuthenticationTest {

    private static final char[] TEST_CLIENT_SECRET = "testClientSecret".toCharArray();
    private static final String TEST_CLIENT_ID = "testClientID";
    private static final String TEST_OAUTH_SERVER_URL = "https://testOauthServerURL";

    private static final OAuthServerConfig OAUTH_SERVER_CONFIG = OAuthServerConfigBuilder.getBuilder()
            .oAuthServerHost(TEST_OAUTH_SERVER_URL)
            .clientID(TEST_CLIENT_ID)
            .clientSecret(TEST_CLIENT_SECRET)
            .build();

    @Test
    public void testValidOAuthAuthentication() {
        OAuthAuthentication authentication = new OAuthAuthentication(OAUTH_SERVER_CONFIG);

        assertEquals(OAUTH_SERVER_CONFIG, authentication.getOAuthServerConfig());
        assertEquals(AuthenticationType.OAUTH, authentication.getAuthenticationType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullOAuthAuthenticationTest() {
        new OAuthAuthentication(null);
    }
}
