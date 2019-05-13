package com.sap.cloud.rest.api.client;

import static com.sap.cloud.rest.api.client.RestApiClient.CONFIG_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.RestApiClient.HOST_NOT_VALID_MSG;
import static com.sap.cloud.rest.api.client.RestApiClient.HTTP_CLIENT_PROVIDER_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.RestApiClient.IO_EXCEPTION_WHILE_EXECUTING_REQUEST_MSG;
import static com.sap.cloud.rest.api.client.RestApiClient.IO_EXCEPTION_WHILE_HANDLING_RESPONSE_MSG;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.CANNOT_BE_NULL_MSG;
import static com.sap.cloud.rest.api.client.utils.test.MockResponseUtil.makeMockedResponseWithStatusAndEntity;
import static java.text.MessageFormat.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.rest.api.client.RestApiClient;
import com.sap.cloud.rest.api.client.auth.cert.ClientCertAuthentication;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfig;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfigBuilder;
import com.sap.cloud.rest.api.client.auth.none.NoAuthentication;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;
import com.sap.cloud.rest.api.client.exceptions.ConnectionException;
import com.sap.cloud.rest.api.client.exceptions.ResponseException;
import com.sap.cloud.rest.api.client.handler.DefaultResponseHandler;
import com.sap.cloud.rest.api.client.handler.DefaultStatusCodeHandler;
import com.sap.cloud.rest.api.client.handler.StatusCodeHandler;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;
import com.sap.cloud.rest.api.client.model.HttpExchangeContext;
import com.sap.cloud.rest.api.client.model.Request;
import com.sap.cloud.rest.api.client.model.Response;

public class RestApiClientTest {

    private static final String TEST_RESPONSE_BODY = "responseBody";
    private static final int TEST_SUCCESS_CODE = 200;
    private static final int TEST_ERROR_CODE = 500;
    private static final String VALID_HOST = "https://example.com";
    private static final String VALID_HOST_WITH_PATH = "https://example.com/path";
    private static final String INVALID_HOST = "asd://example.com";

    private static final char[] TEST_PASSWORD = "password".toCharArray();
    private static final String TEST_ALIAS = "alias";
    private static final KeyStore TEST_KEYSTORE = mock(KeyStore.class);
    private static final KeystoreConfig TEST_KEYSTORE_CONFIG = KeystoreConfigBuilder.getBuilder()
            .keystore(TEST_KEYSTORE)
            .keystorePassword(TEST_PASSWORD)
            .keyAlias(TEST_ALIAS)
            .build();

    private HttpClientProvider httpClientProvider = mock(HttpClientProvider.class);
    private HttpClient httpClient = mock(HttpClient.class);
    private HttpResponse httpResponse = makeMockedResponseWithStatusAndEntity(TEST_SUCCESS_CODE, TEST_RESPONSE_BODY);
    private ResponseHandler<String> responseHandler = mock(DefaultResponseHandler.class);
    private StatusCodeHandler statusCodeHandler = DefaultStatusCodeHandler.create();
    private RestApiClientConfig restApiClientConfig;
    private RestApiClient client;
    private Request<String> request;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private class DefaultRestApiClient extends RestApiClient {

        private static final String API_PATH = "/api";

        protected DefaultRestApiClient(RestApiClientConfig restApiClientConfig) {
            super(restApiClientConfig);
        }

        protected DefaultRestApiClient(RestApiClientConfig restApiClientConfig, HttpClientProvider httpClientProvider) {
            super(restApiClientConfig, httpClientProvider);
        }

        @Override
        protected String getApiPath() {
            return API_PATH;
        }

    }

    @Before
    public void before() throws ClientProtocolException, IOException {
        doReturn(httpClient).when(httpClientProvider).createHttpClient(any());
        doReturn(httpResponse).when(httpClient).execute(any());

        restApiClientConfig = new RestApiClientConfig(VALID_HOST, new NoAuthentication());
        client = new DefaultRestApiClient(restApiClientConfig, httpClientProvider);

        HttpUriRequest httpUriRequest = RequestBuilder.get().build();
        request = new Request<>(httpUriRequest);
    }

    @Test
    public void createRestApiClientTest() {
        restApiClientConfig = new RestApiClientConfig(VALID_HOST, new NoAuthentication());
        client = new DefaultRestApiClient(restApiClientConfig);

        assertTrue(client instanceof RestApiClient);
    }

    @Test
    public void createRestApiClientPathTest() {
        client = new DefaultRestApiClient(new RestApiClientConfig(VALID_HOST, new NoAuthentication()));
        assertEquals("https://example.com/api", client.buildRequestUri().toString());

        client = new DefaultRestApiClient(new RestApiClientConfig(VALID_HOST_WITH_PATH, new NoAuthentication()));
        assertEquals("https://example.com/path/api", client.buildRequestUri().toString());
    }

    @Test
    public void createRestApiClientTestWithProvidedHttpClientProviderTest() {
        restApiClientConfig = new RestApiClientConfig(VALID_HOST, new ClientCertAuthentication(TEST_KEYSTORE_CONFIG));
        client = new DefaultRestApiClient(restApiClientConfig, httpClientProvider);

        assertTrue(client instanceof RestApiClient);
    }

    @Test
    public void createAbstractRestApiClientInvalidHostTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(format(HOST_NOT_VALID_MSG, INVALID_HOST));

        restApiClientConfig = new RestApiClientConfig(INVALID_HOST, new NoAuthentication());
        new DefaultRestApiClient(restApiClientConfig);
    }

    @Test
    public void createAbstractRestApiClientNullHttpClientProviderTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(HTTP_CLIENT_PROVIDER_DISPLAY_NAME + CANNOT_BE_NULL_MSG);

        restApiClientConfig = new RestApiClientConfig(VALID_HOST, new NoAuthentication());
        new DefaultRestApiClient(restApiClientConfig, null);
    }

    @Test
    public void createAbstractRestApiClientWithProvidedHttpClientProviderNullConfigTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(CONFIG_DISPLAY_NAME + CANNOT_BE_NULL_MSG);

        new DefaultRestApiClient(null, httpClientProvider);
    }

    @Test
    public void createAbstractRestApiClientNullConfigTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(CONFIG_DISPLAY_NAME + CANNOT_BE_NULL_MSG);

        new DefaultRestApiClient(null);
    }

    @Test
    public void executeRequestTest() {
        Response<String> response = client.execute(request);

        assertEquals(httpResponse, response.getHttpResponse());
        assertEquals(TEST_RESPONSE_BODY, response.getEntity());
    }

    @Test
    public void executeRequestErrorStatusCodeTest() {
        expected.expect(ResponseException.class);

        mockHttpResponseCode(TEST_ERROR_CODE);

        client.execute(request);
    }

    @Test
    public void executeRequestErrorStatusCodeCustomHandlerTest() {
        mockHttpResponseCode(TEST_ERROR_CODE);

        statusCodeHandler.addHandler(TEST_ERROR_CODE, context -> {
        });

        Response<String> response = client.execute(request, statusCodeHandler);
        assertEquals(httpResponse, response.getHttpResponse());
    }

    @Test
    public void executeRequestNullResponseEntityResponseHandlingTest() throws ClientProtocolException, IOException {
        doReturn(null).when(responseHandler).handleResponse(httpResponse);

        Response<String> response = client.execute(request, responseHandler);

        assertEquals(httpResponse, response.getHttpResponse());
        assertNull(response.getEntity());
    }

    @Test
    public void executeRequestIOExceptionTest() throws ClientProtocolException, IOException {
        doThrow(new IOException()).when(httpClient).execute(any());

        expected.expect(ConnectionException.class);
        expected.expectMessage(format(IO_EXCEPTION_WHILE_EXECUTING_REQUEST_MSG, request));

        client.execute(request);
    }

    @Test
    public void executeNullEntityRequestIOExceptionWhileHandlingResponseTest()
            throws ClientProtocolException, IOException {
        IOException cause = new IOException();
        doThrow(cause).when(responseHandler).handleResponse(httpResponse);

        Request<String> request = new Request<>(RequestBuilder.get().build());
        HttpExchangeContext context = new HttpExchangeContext(request, new Response<String>(httpResponse, ""));

        expected.expect(ResponseException.class);
        expected.expectCause(equalTo(cause));
        expected.expectMessage(format(IO_EXCEPTION_WHILE_HANDLING_RESPONSE_MSG, context));

        client.execute(request, responseHandler);
    }

    @Test
    public void executeStringEntityRequestIOExceptionWhileHandlingResponseTest()
            throws ClientProtocolException, IOException {
        IOException cause = new IOException();
        doThrow(cause).when(responseHandler).handleResponse(httpResponse);

        Request<String> request = new Request<>(RequestBuilder.get().build(), "string value");
        HttpExchangeContext context = new HttpExchangeContext(request, new Response<String>(httpResponse, ""));

        expected.expect(ResponseException.class);
        expected.expectCause(equalTo(cause));
        expected.expectMessage(format(IO_EXCEPTION_WHILE_HANDLING_RESPONSE_MSG, context));

        client.execute(request, responseHandler);
    }

    @Test
    public void executeNonStringEntityRequestIOExceptionWhileHandlingResponseTest()
            throws ClientProtocolException, IOException {
        IOException cause = new IOException();
        doThrow(cause).when(responseHandler).handleResponse(httpResponse);

        TestEntity testEntity = new TestEntity("value");
        Request<TestEntity> request = new Request<>(RequestBuilder.get().build(), testEntity);
        Request<String> contextRequest = new Request<>(request.getHttpRequest(),
                new ObjectMapper().writeValueAsString(request.getEntity()));
        HttpExchangeContext context = new HttpExchangeContext(contextRequest, new Response<String>(httpResponse, ""));

        expected.expect(ResponseException.class);
        expected.expectCause(equalTo(cause));
        expected.expectMessage(format(IO_EXCEPTION_WHILE_HANDLING_RESPONSE_MSG, context));

        try {
            client.execute(request, responseHandler);
        } catch (ResponseException e) {
            String expectedJson = new ObjectMapper().writeValueAsString(testEntity);
            assertEquals(expectedJson, e.getRequest().getEntity());
            throw e;
        }
    }

    private class TestEntity {

        public String field;

        public TestEntity(String field) {
            this.field = field;
        }

    }

    @Test
    public void getApiPathTest() {
        assertEquals(DefaultRestApiClient.API_PATH, client.getApiPath());
    }

    @Test
    public void getDefaultResponseHandlerTest() {
        assertTrue(client.getDefaultResponseHandler() instanceof ResponseHandler);
    }

    @Test
    public void getDefaultStatusCodeHandlerTest() {
        assertTrue(client.getDefaultStatusCodeHandler() instanceof StatusCodeHandler);
    }

    @Test
    public void buildRequestUriTest() {
        String pathPattern = "/{0}/endpoint";
        String version = "v1";
        URI uri = client.buildRequestUri(pathPattern, version);

        assertEquals(VALID_HOST + DefaultRestApiClient.API_PATH + "/v1/endpoint", uri.toString());
    }

    @Test
    public void buildRequestUriNoPatternTest() {
        URI uri = client.buildRequestUri();

        assertEquals(VALID_HOST + DefaultRestApiClient.API_PATH, uri.toString());
    }

    private void mockHttpResponseCode(int statusCode) {
        StatusLine statusLine = mock(StatusLine.class);
        doReturn(statusCode).when(statusLine).getStatusCode();
        doReturn(statusLine).when(httpResponse).getStatusLine();
    }
}
