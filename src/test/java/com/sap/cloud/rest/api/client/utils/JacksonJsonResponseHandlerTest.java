package com.sap.cloud.rest.api.client.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sap.cloud.rest.api.client.utils.JacksonJsonResponseHandler;

import org.apache.http.HttpResponse;
import org.junit.Test;

import java.util.List;

import static com.sap.cloud.rest.api.client.utils.test.MockResponseUtil.makeMockedResponseWithStatusAndEntity;
import static com.sap.cloud.rest.api.client.utils.test.MockResponseUtil.makeMockedResponseWithStatusCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JacksonJsonResponseHandlerTest {

    @Test
    public void testWithValidJsonShouldParseToObject() throws Exception {
        HttpResponse mockResponse = makeMockedResponseWithStatusAndEntity(200,
                "{ \"field\": \"value\"}");

        JacksonJsonResponseHandler<TestClass> responseHandler = new JacksonJsonResponseHandler<>(TestClass.class);
        TestClass resultEntity = responseHandler.handleResponse(mockResponse);

        assertEquals("value", resultEntity.field);
    }

    @Test
    public void testWithValidJsonUsingTypeReferenceShouldParseToObject() throws Exception {
        HttpResponse mockResponse = makeMockedResponseWithStatusAndEntity(200,
                "[{ \"field\": \"value\"}]");

        JacksonJsonResponseHandler<List<TestClass>> responseHandler =
                new JacksonJsonResponseHandler<>(new TypeReference<List<TestClass>>() {
                });
        List<TestClass> resultActual = responseHandler.handleResponse(mockResponse);
        assertEquals(1, resultActual.size());
        assertEquals("value", resultActual.get(0).field);
    }

    @Test
    public void testWithEmptyBodyShouldReturnNull() throws Exception {
        HttpResponse mockResponse = makeMockedResponseWithStatusCode(200);

        JacksonJsonResponseHandler<TestClass> responseHandler = new JacksonJsonResponseHandler<>(TestClass.class);
        TestClass resultEntity = responseHandler.handleResponse(mockResponse);

        assertNull(resultEntity);
    }

    @Test
    public void testWithEmptyBodyUsingTypeReferenceShouldReturnNull() throws Exception {
        HttpResponse mockResponse = makeMockedResponseWithStatusCode(200);

        JacksonJsonResponseHandler<List<TestClass>> responseHandler = new JacksonJsonResponseHandler<>(new TypeReference<List<TestClass>>() {
        });
        List<TestClass> resultActual = responseHandler.handleResponse(mockResponse);

        assertNull(resultActual);
    }

    @Test(expected = JsonParseException.class)
    public void testWithInvalidJsonShouldThrowJsonParseException() throws Exception {
        HttpResponse mockResponse = makeMockedResponseWithStatusAndEntity(200, "field=value");

        JacksonJsonResponseHandler<TestClass> responseHandler = new JacksonJsonResponseHandler<>(TestClass.class);
        responseHandler.handleResponse(mockResponse);
    }

    @Test(expected = JsonParseException.class)
    public void testWithInvalidJsonUsingTypeReferenceShouldThrowJsonParseException() throws Exception {
        HttpResponse mockResponse = makeMockedResponseWithStatusAndEntity(200, "[field=value]");

        JacksonJsonResponseHandler<List<TestClass>> responseHandler = new JacksonJsonResponseHandler<>(new TypeReference<List<TestClass>>() {
        });
        responseHandler.handleResponse(mockResponse);
    }

    private static class TestClass {

        public String field;
    }

}