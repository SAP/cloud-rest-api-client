package com.sap.cloud.rest.api.client.handler;

import static com.sap.cloud.rest.api.client.utils.test.MockResponseUtil.makeMockedResponseWithEntity;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.cloud.rest.api.client.exceptions.ConnectionException;
import com.sap.cloud.rest.api.client.handler.DefaultResponseHandler;

public class DefaultResponseHandlerTest {

    private DefaultResponseHandler responseHandler = new DefaultResponseHandler();

    @Test
    public void testHandleResponse() throws ConnectionException, IOException {
        String expectedResponse = "responseBody";
        HttpResponse httpResponse = makeMockedResponseWithEntity(expectedResponse);

        String actualResponse = responseHandler.handleResponse(httpResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testHandleResponseWithNullEntity() throws ConnectionException, IOException {
        HttpResponse httpResponse = mock(HttpResponse.class);
        doReturn(null).when(httpResponse).getEntity();

        String actualResponse = responseHandler.handleResponse(httpResponse);
        assertEquals("", actualResponse);
    }
}
