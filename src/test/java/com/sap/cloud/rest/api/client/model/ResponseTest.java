package com.sap.cloud.rest.api.client.model;

import static com.sap.cloud.rest.api.client.utils.test.MockResponseUtil.makeMockedResponseWithStatusCode;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.junit.Before;
import org.junit.Test;

import com.sap.cloud.rest.api.client.model.Response;

public class ResponseTest {

    private static final int TEST_STATUS_CODE = 500;
    private static final Header[] TEST_RESPONSE_HEADERS = new Header[] {};

    private static final HttpResponse TEST_HTTP_RESPONSE = makeMockedResponseWithStatusCode(TEST_STATUS_CODE);

    @Before
    public void setup() {
        doReturn(TEST_RESPONSE_HEADERS).when(TEST_HTTP_RESPONSE).getAllHeaders();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateResponseWithNullHttpResponseShouldThrow() {
        new Response<TestEntity>(null, new TestEntity());
    }

    @Test
    public void testResponseEntityIsNullShouldNotThrow() {
        new Response<TestEntity>(TEST_HTTP_RESPONSE, null);
    }

    @Test
    public void testGetHttpResponse() {
        Response<TestEntity> response = new Response<>(TEST_HTTP_RESPONSE, null);

        assertEquals(response.getHttpResponse(), TEST_HTTP_RESPONSE);
    }

    @Test
    public void testGetStatusCode() {
        Response<TestEntity> response = new Response<TestEntity>(TEST_HTTP_RESPONSE, new TestEntity());

        assertEquals(TEST_STATUS_CODE, response.getStatusCode());
    }

    @Test
    public void testGetEntity() {
        TestEntity entity = new TestEntity();

        Response<TestEntity> response = new Response<>(TEST_HTTP_RESPONSE, entity);

        assertEquals(response.getEntity(), entity);
    }

    @Test
    public void testGetHeaders() {
        Response<TestEntity> response = new Response<TestEntity>(TEST_HTTP_RESPONSE, new TestEntity());

        assertArrayEquals(TEST_RESPONSE_HEADERS, response.getHeaders());
    }

    @Test
    public void testToStringContatinsHttpResponseAndEntity() {
        doReturn(new Header[] { new BasicHeader("header", "value") }).when(TEST_HTTP_RESPONSE).getAllHeaders();
        TestEntity entity = new TestEntity();

        Response<TestEntity> response = new Response<>(TEST_HTTP_RESPONSE, entity);

        String httpResponseAsString = TEST_HTTP_RESPONSE.toString();
        String entityAsString = entity.toString();
        String toStringResult = response.toString();

        assertTrue(toStringResult.contains(httpResponseAsString));
        assertTrue(toStringResult.contains(entityAsString));
    }

    private static class TestEntity {
    }
}
