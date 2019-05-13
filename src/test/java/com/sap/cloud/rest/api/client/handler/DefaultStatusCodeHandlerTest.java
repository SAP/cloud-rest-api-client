package com.sap.cloud.rest.api.client.handler;

import static com.sap.cloud.rest.api.client.handler.DefaultStatusCodeHandler.HTTP_RESPOSNE_EXCEPTION_MSG;
import static com.sap.cloud.rest.api.client.handler.DefaultStatusCodeHandler.UNAUTHORIZED_MSG;
import static java.text.MessageFormat.format;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.cloud.rest.api.client.exceptions.ResponseException;
import com.sap.cloud.rest.api.client.exceptions.UnauthorizedException;
import com.sap.cloud.rest.api.client.handler.DefaultStatusCodeHandler;
import com.sap.cloud.rest.api.client.handler.StatusCodeHandler;
import com.sap.cloud.rest.api.client.model.HttpExchangeContext;
import com.sap.cloud.rest.api.client.model.Request;
import com.sap.cloud.rest.api.client.model.Response;
import com.sap.cloud.rest.api.client.utils.test.MockResponseUtil;

public class DefaultStatusCodeHandlerTest {

    private static final int TEST_SUCCESS_CODE = 200;
    private static final int TEST_ERROR_CODE = 500;
    private static final String TEST_RESPONSE_BODY = "responseBody";
    private static final Request<String> TEST_REQUEST = new Request<>(RequestBuilder.get().build());

    private HttpResponse httpResponse = MockResponseUtil.makeMockedResponseWithStatusCode(TEST_SUCCESS_CODE);
    private Response<String> response = new Response<String>(httpResponse, TEST_RESPONSE_BODY);
    private HttpExchangeContext context = new HttpExchangeContext(TEST_REQUEST, response);
    private StatusCodeHandler handler = DefaultStatusCodeHandler.create();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void testDefaultHandler() {
        handler.handleStatusCode(TEST_SUCCESS_CODE, context);
    }

    @Test
    public void testDefaultHandlerErrorResponseCode() throws ResponseException {
        expected.expect(ResponseException.class);
        expected.expectMessage(format(HTTP_RESPOSNE_EXCEPTION_MSG, context));

        StatusLine statusLine = mock(StatusLine.class);
        doReturn(TEST_ERROR_CODE).when(statusLine).getStatusCode();
        doReturn(statusLine).when(httpResponse).getStatusLine();

        handler.handleStatusCode(TEST_ERROR_CODE, context);
    }

    @Test
    public void testUnauthorizedHandler() throws ResponseException {
        expected.expect(UnauthorizedException.class);
        expected.expectMessage(format(UNAUTHORIZED_MSG, context));

        handler.handleStatusCode(401, context);
    }
}
