package com.sap.cloud.rest.api.client.exceptions;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.client.methods.RequestBuilder;
import org.junit.Test;

import com.sap.cloud.rest.api.client.exceptions.ConnectionException;
import com.sap.cloud.rest.api.client.model.Request;

public class ConnectionExceptionTest {

    private static final String TEST_MESSAGE = "test_message";
    private static final IOException TEST_CAUSE = new IOException();
    private static final Request<String> TEST_REQUEST = new Request<>(RequestBuilder.get().build());

    @Test
    public void testGetRequest() {
        ConnectionException ex = new ConnectionException(TEST_MESSAGE, TEST_CAUSE, TEST_REQUEST);

        assertEquals(TEST_REQUEST, ex.getRequest());
    }
}
