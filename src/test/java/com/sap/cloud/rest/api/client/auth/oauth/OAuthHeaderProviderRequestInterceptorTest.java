package com.sap.cloud.rest.api.client.auth.oauth;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sap.cloud.rest.api.client.auth.oauth.AccessTokenProvider;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthHeaderProviderRequestInterceptor;

@RunWith(MockitoJUnitRunner.class)
public class OAuthHeaderProviderRequestInterceptorTest {

    private static final String OAUTH_HEADER_KEY = HttpHeaders.AUTHORIZATION;
    private static final String TEST_ACCESS_TOKEN = "dummyAccessToken";
    private static final String EXPECTED_AUTH_HEADER_VALUE = "Bearer " + TEST_ACCESS_TOKEN;

    @Mock
    private HttpRequest httpRequestMock;
    @Mock
    private AccessTokenProvider accessTokenProviderMock;

    @Test
    public void testAuthHeaderAdded() {
        when(accessTokenProviderMock.retrieveAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        OAuthHeaderProviderRequestInterceptor оаuthHeaderProviderRequestInterceptor = new OAuthHeaderProviderRequestInterceptor(
                accessTokenProviderMock, OAUTH_HEADER_KEY);

        оаuthHeaderProviderRequestInterceptor.process(httpRequestMock, null);

        verify(httpRequestMock).addHeader(OAUTH_HEADER_KEY, EXPECTED_AUTH_HEADER_VALUE);

    }

}
