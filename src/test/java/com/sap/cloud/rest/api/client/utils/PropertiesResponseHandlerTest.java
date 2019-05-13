package com.sap.cloud.rest.api.client.utils;

import static com.sap.cloud.rest.api.client.utils.test.MockResponseUtil.makeMockedResponseWithStatusAndEntity;
import static com.sap.cloud.rest.api.client.utils.test.MockResponseUtil.makeMockedResponseWithStatusCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sap.cloud.rest.api.client.utils.PropertiesResponseHandler;

public class PropertiesResponseHandlerTest {

    @Test
    public void testWithValidPropertiesShouldParseToObject() throws Exception {
        HttpResponse mockResponse = makeMockedResponseWithStatusAndEntity(200,
                "field=value");

        PropertiesResponseHandler<TestClass> responseHandler = new PropertiesResponseHandler<>(TestClass.class);
        TestClass resultEntity = responseHandler.handleResponse(mockResponse);

        assertEquals("value", resultEntity.field);
    }

    @Test
    public void testWithEmptyBodyShouldReturnNull() throws Exception {
        HttpResponse mockResponse = makeMockedResponseWithStatusCode(200);

        PropertiesResponseHandler<TestClass> responseHandler = new PropertiesResponseHandler<>(TestClass.class);
        TestClass resultEntity = responseHandler.handleResponse(mockResponse);

        assertNull(resultEntity);
    }

    @Test(expected = UnrecognizedPropertyException.class)
    public void testWithJsonBodyShouldThrowUnrecognizedPropertyException() throws Exception {
        HttpResponse mockResponse = makeMockedResponseWithStatusAndEntity(200, "{ \"field\": \"value\"}");

        PropertiesResponseHandler<TestClass> responseHandler = new PropertiesResponseHandler<>(TestClass.class);
        responseHandler.handleResponse(mockResponse);
    }

    private static class TestClass {

        public String field;
    }

}