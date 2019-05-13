package com.sap.cloud.rest.api.client.model;

import static com.sap.cloud.rest.api.client.model.HttpExchangeContext.REQUEST_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.model.HttpExchangeContext.RESPONSE_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.CANNOT_BE_NULL_MSG;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.cloud.rest.api.client.model.HttpExchangeContext;
import com.sap.cloud.rest.api.client.model.Request;
import com.sap.cloud.rest.api.client.model.Response;

public class HttpExchangeContextTest {

    private static final Request<String> TEST_REQUEST = new Request<>(RequestBuilder.get().build());
    private static final String TEST_RESPONSE_BODY = "test_response_body";
    private static final HttpResponse TEST_HTTP_RESPONSE = mock(HttpResponse.class);
    private static final Response<String> TEST_RESPONSE = new Response<>(TEST_HTTP_RESPONSE, TEST_RESPONSE_BODY);
    private static final HttpExchangeContext TEST_CONTEXT = new HttpExchangeContext(TEST_REQUEST, TEST_RESPONSE);

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void testGetRequest() {
        assertEquals(TEST_REQUEST, TEST_CONTEXT.getRequest());
    }

    @Test
    public void testGetResponse() {
        assertEquals(TEST_RESPONSE, TEST_CONTEXT.getResponse());
    }

    @Test
    public void testCreateHttpExchangeContextWithNullRequest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(REQUEST_DISPLAY_NAME + CANNOT_BE_NULL_MSG);

        new HttpExchangeContext(null, TEST_RESPONSE);
    }

    @Test
    public void testCreateHttpExchangeContextWithNullResponse() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(RESPONSE_DISPLAY_NAME + CANNOT_BE_NULL_MSG);

        new HttpExchangeContext(TEST_REQUEST, null);
    }
}
