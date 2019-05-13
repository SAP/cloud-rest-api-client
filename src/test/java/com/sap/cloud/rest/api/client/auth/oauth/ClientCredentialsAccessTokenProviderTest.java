package com.sap.cloud.rest.api.client.auth.oauth;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.rest.api.client.auth.oauth.AccessTokenResponseDto;
import com.sap.cloud.rest.api.client.auth.oauth.ClientCredentialsAccessTokenProvider;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;
import com.sap.cloud.rest.api.client.config.RestApiClientConfigBuilder;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;
import com.sap.cloud.rest.api.client.utils.test.RequestMatcher;

public class ClientCredentialsAccessTokenProviderTest {

    private static final String TEST_HOST = "https://example.com";
    private static final String TEST_API_PATH = "/oauth/token";
    private static final String TEST_ACCESS_TOKEN_STRING = "dummyAccessToken";
    private static final String QUERY_PARAMS_AND_VALUES = "?grant_type=client_credentials&response_type=token";

    private static final AccessTokenResponseDto TEST_ACCESS_TOKEN_RESPONSE_DTO = new AccessTokenResponseDto(TEST_ACCESS_TOKEN_STRING);

    private HttpClient httpClientMock;
    private HttpClientProvider httpClientProviderMock;
    private RestApiClientConfig restApiClientConfig;
    private ObjectMapper objectMapper = new ObjectMapper();
    private HttpResponse mockedResponse;
    private ClientCredentialsAccessTokenProvider clientCredentialsAccessTokenProvider;

    @Before
    public void before() throws ClientProtocolException, IOException {
        setUpRestApiClientConfig();
        setupMockedHttpClient();
        clientCredentialsAccessTokenProvider = new ClientCredentialsAccessTokenProvider(restApiClientConfig,
                httpClientProviderMock, TEST_API_PATH);
    }

    @Test
    public void testAccessTokenReceived() throws Exception {
        String accessToken = clientCredentialsAccessTokenProvider.retrieveAccessToken();
        
        assertEquals(TEST_ACCESS_TOKEN_STRING, accessToken);
    }

    @Test
    public void testAccessTokenRequestUrl() throws Exception {
        clientCredentialsAccessTokenProvider.retrieveAccessToken();
        
        verify(httpClientMock).execute(argThat(new RequestMatcher("POST",
                TEST_HOST + TEST_API_PATH + QUERY_PARAMS_AND_VALUES)));
    }

    private void setupMockedHttpClient() throws JsonProcessingException, IOException, ClientProtocolException {
        httpClientProviderMock = Mockito.mock(HttpClientProvider.class);
        httpClientMock = Mockito.mock(HttpClient.class);

        setupMockedResponse(200, objectMapper.writeValueAsString(TEST_ACCESS_TOKEN_RESPONSE_DTO));
        when(httpClientMock.execute(any())).thenReturn(mockedResponse);
        when(httpClientProviderMock.createHttpClient(any())).thenReturn(httpClientMock);
    }

    private void setUpRestApiClientConfig() {
        restApiClientConfig = RestApiClientConfigBuilder.getBuilder()
                .host(TEST_HOST)
                .noAuthentication()
                .build();
    }

    private void setupMockedResponse(int statusCode, String responseText) {
        mockedResponse = Mockito.mock(HttpResponse.class);
        StatusLine mockedStatusLine = Mockito.mock(StatusLine.class);
        when(mockedResponse.getStatusLine()).thenReturn(mockedStatusLine);
        when(mockedStatusLine.getStatusCode()).thenReturn(statusCode);
        when(mockedResponse.getEntity()).thenReturn(EntityBuilder.create().setText(responseText).build());
    }
}
