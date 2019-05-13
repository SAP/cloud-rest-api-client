package com.sap.cloud.rest.api.client.exceptions;

import static com.sap.cloud.rest.api.client.utils.test.MockResponseUtil.makeMockedResponseWithStatusCode;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import com.sap.cloud.rest.api.client.exceptions.ResponseException;
import com.sap.cloud.rest.api.client.model.HttpExchangeContext;
import com.sap.cloud.rest.api.client.model.Request;
import com.sap.cloud.rest.api.client.model.Response;

public class ResponseExceptionTest {

    private static final int TEST_STATUS_CODE = 500;
    private static final String TEST_RESPONSE_BODY = "test_response_body";
    private static final String TEST_MESSAGE = "test_message";
    private static final Header[] TEST_RESPONSE_HEADERS = new Header[] {};
    private static final Throwable TEST_CAUSE = new IOException();

    private static final Request<String> TEST_REQUEST = new Request<>(RequestBuilder.get().build());
    private static final HttpResponse TEST_HTTP_RESPONSE = makeMockedResponseWithStatusCode(TEST_STATUS_CODE);
    private static final Response<String> TEST_RESPONSE = new Response<>(TEST_HTTP_RESPONSE, TEST_RESPONSE_BODY);
    private static final HttpExchangeContext TEST_CONTEXT = new HttpExchangeContext(TEST_REQUEST, TEST_RESPONSE);

    @Before
    public void setup() {
        doReturn(TEST_RESPONSE_HEADERS).when(TEST_HTTP_RESPONSE).getAllHeaders();
    }

    @Test
    public void testGetRequest() {
        ResponseException ex = new ResponseException(TEST_MESSAGE, TEST_CONTEXT);

        assertEquals(TEST_REQUEST, ex.getRequest());
    }

    @Test
    public void testGetStatusCode() {
        ResponseException ex = new ResponseException(TEST_MESSAGE, TEST_CONTEXT);

        assertEquals(TEST_STATUS_CODE, ex.getResponse().getStatusCode());
    }

    @Test
    public void testGetResponseBody() {
        ResponseException ex = new ResponseException(TEST_MESSAGE, TEST_CONTEXT);

        assertEquals(TEST_RESPONSE_BODY, ex.getResponse().getEntity());
    }

    @Test
    public void testGetResponseHeaders() {
        ResponseException ex = new ResponseException(TEST_MESSAGE, TEST_CONTEXT);

        assertArrayEquals(TEST_RESPONSE_HEADERS, ex.getResponse().getHeaders());
    }

    @Test
    public void testGetCause() {
        ResponseException ex = new ResponseException(TEST_MESSAGE, TEST_CONTEXT, TEST_CAUSE);

        assertEquals(TEST_CAUSE, ex.getCause());
    }
}
