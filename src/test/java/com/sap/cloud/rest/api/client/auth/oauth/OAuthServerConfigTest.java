package com.sap.cloud.rest.api.client.auth.oauth;

import static com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig.CLIENT_ID_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig.CLIENT_SECRET_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig.DEFAULT_OAUTH_HEADER_KEY;
import static com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig.DEFAULT_OAUTH_SERVER_API_PATH;
import static com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig.OAUTH_HEADER_KEY_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig.OAUTH_SERVER_API_PATH_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig.OAUTH_SERVER_HOST_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.CANNOT_BE_BLANK_MSG;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.CANNOT_BE_NULL_MSG;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.NOT_VALID_URL_MSG;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.http.HttpHeaders;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig;

public class OAuthServerConfigTest {

    private static final String TEST_OAUTH_SERVER_HOST = "https://testOauthServerHost";
    private static final String TEST_OAUTH_SERVER_API_PATH = "/test/api";
    private static final char[] TEST_CLIENT_SECRET = "testClientSecret".toCharArray();
    private static final String TEST_CLIENT_ID = "testClientID";
    private static final String TEST_OAUTH_HEADER_KEY = HttpHeaders.PROXY_AUTHORIZATION;

    private static final String TEST_INCORRECT_OAUTH_SERVER_HOST = "incorrect host";

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void testCreateConfigWithValidData() {
        OAuthServerConfig config = new OAuthServerConfig(TEST_OAUTH_SERVER_HOST, TEST_CLIENT_ID, TEST_CLIENT_SECRET);

        assertEquals(TEST_OAUTH_SERVER_HOST, config.getOAuthServerHost());
        assertEquals(DEFAULT_OAUTH_SERVER_API_PATH, config.getoAuthServerApiPath());
        assertEquals(DEFAULT_OAUTH_HEADER_KEY, config.getoAuthHeaderKey());
        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertArrayEquals(TEST_CLIENT_SECRET, config.getClientSecret());
    }

    @Test
    public void testCreateConfigWithValidDataProvidedApiPath() {
        OAuthServerConfig config = new OAuthServerConfig(TEST_OAUTH_SERVER_HOST, TEST_OAUTH_SERVER_API_PATH,
                TEST_CLIENT_ID, TEST_CLIENT_SECRET);

        assertEquals(TEST_OAUTH_SERVER_HOST, config.getOAuthServerHost());
        assertEquals(TEST_OAUTH_SERVER_API_PATH, config.getoAuthServerApiPath());
        assertEquals(DEFAULT_OAUTH_HEADER_KEY, config.getoAuthHeaderKey());
        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertArrayEquals(TEST_CLIENT_SECRET, config.getClientSecret());
    }

    @Test
    public void testCreateConfigWithValidDataProvidedHeaderKey() {
        OAuthServerConfig config = new OAuthServerConfig(TEST_OAUTH_SERVER_HOST,
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_OAUTH_HEADER_KEY);

        assertEquals(TEST_OAUTH_SERVER_HOST, config.getOAuthServerHost());
        assertEquals(DEFAULT_OAUTH_SERVER_API_PATH, config.getoAuthServerApiPath());
        assertEquals(TEST_OAUTH_HEADER_KEY, config.getoAuthHeaderKey());
        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertArrayEquals(TEST_CLIENT_SECRET, config.getClientSecret());
    }

    @Test
    public void testCreateConfigWithValidDataProvidedApiPathAndHeaderKey() {
        OAuthServerConfig config = new OAuthServerConfig(TEST_OAUTH_SERVER_HOST, TEST_OAUTH_SERVER_API_PATH,
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_OAUTH_HEADER_KEY);

        assertEquals(TEST_OAUTH_SERVER_HOST, config.getOAuthServerHost());
        assertEquals(TEST_OAUTH_SERVER_API_PATH, config.getoAuthServerApiPath());
        assertEquals(TEST_OAUTH_HEADER_KEY, config.getoAuthHeaderKey());
        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertArrayEquals(TEST_CLIENT_SECRET, config.getClientSecret());
    }

    @Test
    public void testCreateConfigWithNullOAuthServerHost() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(OAUTH_SERVER_HOST_DISPLAY_NAME + CANNOT_BE_BLANK_MSG);

        new OAuthServerConfig(null, TEST_OAUTH_SERVER_API_PATH,
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_OAUTH_HEADER_KEY);
    }

    @Test
    public void testCreateConfigWithBlankOAuthServerHost() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(OAUTH_SERVER_HOST_DISPLAY_NAME + CANNOT_BE_BLANK_MSG);

        new OAuthServerConfig("", TEST_OAUTH_SERVER_API_PATH,
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_OAUTH_HEADER_KEY);
    }

    @Test
    public void testCreateConfigWithIncorrectOAuthServerHost() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(OAUTH_SERVER_HOST_DISPLAY_NAME + NOT_VALID_URL_MSG);

        new OAuthServerConfig(TEST_INCORRECT_OAUTH_SERVER_HOST, TEST_OAUTH_SERVER_API_PATH,
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_OAUTH_HEADER_KEY);
    }

    @Test
    public void testCreateConfigWithNullOAuthServerApiPath() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(OAUTH_SERVER_API_PATH_DISPLAY_NAME + CANNOT_BE_NULL_MSG);

        new OAuthServerConfig(TEST_OAUTH_SERVER_HOST, null,
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_OAUTH_HEADER_KEY);
    }

    @Test
    public void testCreateConfigWithNullClientSecret() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(CLIENT_SECRET_DISPLAY_NAME + CANNOT_BE_NULL_MSG);

        new OAuthServerConfig(TEST_OAUTH_SERVER_HOST, TEST_OAUTH_SERVER_API_PATH,
                TEST_CLIENT_ID, null, TEST_OAUTH_HEADER_KEY);
    }

    @Test
    public void testCreateConfigWithNullClientId() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(CLIENT_ID_DISPLAY_NAME + CANNOT_BE_BLANK_MSG);

        new OAuthServerConfig(TEST_OAUTH_SERVER_HOST, TEST_OAUTH_SERVER_API_PATH,
                null, TEST_CLIENT_SECRET, TEST_OAUTH_HEADER_KEY);
    }

    @Test
    public void testCreateConfigWithEmptyClientId() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(CLIENT_ID_DISPLAY_NAME + CANNOT_BE_BLANK_MSG);

        new OAuthServerConfig(TEST_OAUTH_SERVER_HOST, TEST_OAUTH_SERVER_API_PATH,
                "", TEST_CLIENT_SECRET, TEST_OAUTH_HEADER_KEY);
    }

    @Test
    public void testCreateConfigWithNullOAuthHeaderKey() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(OAUTH_HEADER_KEY_DISPLAY_NAME + CANNOT_BE_BLANK_MSG);

        new OAuthServerConfig(TEST_OAUTH_SERVER_HOST, TEST_OAUTH_SERVER_API_PATH,
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, null);
    }

    @Test
    public void testCreateConfigWithEmptyOAuthHeaderKey() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(OAUTH_HEADER_KEY_DISPLAY_NAME + CANNOT_BE_BLANK_MSG);

        new OAuthServerConfig(TEST_OAUTH_SERVER_HOST, TEST_OAUTH_SERVER_API_PATH,
                TEST_CLIENT_ID, TEST_CLIENT_SECRET, "");
    }

}
