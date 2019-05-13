package com.sap.cloud.rest.api.client.auth.oauth;

import static com.sap.cloud.rest.api.client.auth.oauth.AccessTokenProviderFactory.NON_OAUTH_AUTHENTICATION_MSG;
import static com.sap.cloud.rest.api.client.auth.oauth.AccessTokenProviderFactory.createClientCredentialsAccessTokenProvider;
import static java.text.MessageFormat.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.auth.oauth.AccessTokenProviderFactory;
import com.sap.cloud.rest.api.client.auth.oauth.ClientCredentialsAccessTokenProvider;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfigBuilder;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;
import com.sap.cloud.rest.api.client.config.RestApiClientConfigBuilder;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

public class AccessTokenProviderFactoryTest {

    private static final String TEST_HOST = "https://example.com";
    private static final String TEST_API_PATH = "/oauth/token";
    private static final String TEST_CLIENT_ID = "testClientID";
    private static final char[] TEST_CLIENT_SECRET = "testClientSecret".toCharArray();

    private RestApiClientConfig oAuthRestApiClientConfig;
    private RestApiClientConfig invalidRestApiClientConfig;
    private HttpClientProvider httpClientProviderMock;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void init() {
        OAuthServerConfig oAuthServerConfig = OAuthServerConfigBuilder.getBuilder()
                .oAuthServerHost(TEST_HOST)
                .oAuthServerApiPath(TEST_API_PATH)
                .clientID(TEST_CLIENT_ID)
                .clientSecret(TEST_CLIENT_SECRET)
                .build();

        oAuthRestApiClientConfig = RestApiClientConfigBuilder.getBuilder()
                .oAuthAuthentication(oAuthServerConfig)
                .host(TEST_HOST)
                .build();

        invalidRestApiClientConfig = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .build();

        httpClientProviderMock = Mockito.mock(HttpClientProvider.class);
        new AccessTokenProviderFactory();
    }

    @Test
    public void testCreateClientCredentialsAccessTokenProvider() {
        ClientCredentialsAccessTokenProvider createClientCredentialsAccessTokenProvider = AccessTokenProviderFactory
                .createClientCredentialsAccessTokenProvider(oAuthRestApiClientConfig);

        assertNotNull(createClientCredentialsAccessTokenProvider);
        assertEquals(TEST_API_PATH, createClientCredentialsAccessTokenProvider.getApiPath());
    }

    @Test
    public void testCreateClientCredentialsAccessTokenProvider_withCustomHttpClientProvider() {
        ClientCredentialsAccessTokenProvider createClientCredentialsAccessTokenProvider = AccessTokenProviderFactory
                .createClientCredentialsAccessTokenProvider(oAuthRestApiClientConfig,
                        httpClientProviderMock);

        assertNotNull(createClientCredentialsAccessTokenProvider);
        assertEquals(TEST_API_PATH, createClientCredentialsAccessTokenProvider.getApiPath());
    }

    @Test
    public void testCreateClientCredentialsAccessTokenProvider_withNonOAuthAuthentication() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(format(NON_OAUTH_AUTHENTICATION_MSG, AuthenticationType.NO_AUTH));

        createClientCredentialsAccessTokenProvider(invalidRestApiClientConfig);
    }

    @Test
    public void testCreateClientCredentialsAccessTokenProvider_withCustomHttpClientProviderAndNonOAuthAuthentication() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(format(NON_OAUTH_AUTHENTICATION_MSG, AuthenticationType.NO_AUTH));

        createClientCredentialsAccessTokenProvider(invalidRestApiClientConfig, httpClientProviderMock);

    }
}
