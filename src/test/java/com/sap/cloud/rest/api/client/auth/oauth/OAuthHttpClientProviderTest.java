package com.sap.cloud.rest.api.client.auth.oauth;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.http.client.HttpClient;
import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthAuthentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthHttpClientProvider;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig;
import com.sap.cloud.rest.api.client.http.HttpClientCreationException;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;
import com.sap.cloud.rest.api.client.utils.test.HttpClientProviderUtils;

public class OAuthHttpClientProviderTest {

    private static final char[] TEST_CLIENT_SECRET = "testClientSecret".toCharArray();
    private static final String TEST_CLIENT_ID = "testClientID";
    private static final String TEST_OAUTH_SERVER_URL = "https://testOauthServerURL";

    @Test
    public void createHttpClient() throws HttpClientCreationException {
        OAuthServerConfig oAuthServerConfig = new OAuthServerConfig(TEST_OAUTH_SERVER_URL, TEST_CLIENT_ID,
                TEST_CLIENT_SECRET);
        Authentication authentication = new OAuthAuthentication(oAuthServerConfig);

        HttpClientProvider httpClientProvider = HttpClientProviderUtils
                .getHttpClientProviderForAuthentication(authentication);

        assertTrue(httpClientProvider instanceof OAuthHttpClientProvider);

        HttpClient httpClient = httpClientProvider.createHttpClient();
        assertNotNull(httpClient);
    }
}
