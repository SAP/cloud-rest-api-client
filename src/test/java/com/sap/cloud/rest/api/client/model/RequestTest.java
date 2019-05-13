package com.sap.cloud.rest.api.client.model;

import static com.sap.cloud.rest.api.client.model.Request.MASKED_VALUE;
import static org.junit.Assert.assertEquals;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Test;

import com.sap.cloud.rest.api.client.model.Request;

public class RequestTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRequestWithNullInnerRequestShouldThrow() {
        new Request<String>(null);
    }

    @Test
    public void testGetHttpRequest() {
        HttpUriRequest innerRequest = RequestBuilder.get().build();
        Request<String> request = new Request<String>(innerRequest);

        assertEquals(request.getHttpRequest(), innerRequest);
    }

    @Test
    public void testGetBody() {
        HttpUriRequest innerRequest = RequestBuilder.get().build();
        String body = "test_body";
        Request<String> request = new Request<String>(innerRequest, body);

        assertEquals(request.getEntity(), body);
    }

    @Test
    public void testToStringContatinsHttpUriRequestAndHeadersAndBody() {
        HttpUriRequest httpUriRequest = RequestBuilder.get().addHeader("header", "value").build();
        String body = "test_body";
        Request<String> request = new Request<String>(httpUriRequest, body);

        String expectedString = "{\"httpRequest\":\"GET / HTTP/1.1\",\"requestHeaders\":\"[header: value]\",\"requestEntity\":\"test_body\"}";
        assertEquals(request.toString(), expectedString);
    }

    @Test
    public void testToStringSanitizesAuthorizationHeader() {
        HttpUriRequest httpUriRequest = RequestBuilder.get()
                .addHeader("header", "value")
                .addHeader(HttpHeaders.AUTHORIZATION, "credentials")
                .addHeader(HttpHeaders.PROXY_AUTHENTICATE, "credentials")
                .addHeader(HttpHeaders.PROXY_AUTHORIZATION, "credentials")
                .build();
        String body = "test_body";
        Request<String> request = new Request<String>(httpUriRequest, body);

        String expectedString = "{\"httpRequest\":\"GET / HTTP/1.1\",\"requestHeaders\":\"[header: value, "
                + HttpHeaders.AUTHORIZATION + ": " + MASKED_VALUE + ", "
                + HttpHeaders.PROXY_AUTHENTICATE + ": " + MASKED_VALUE + ", "
                + HttpHeaders.PROXY_AUTHORIZATION + ": " + MASKED_VALUE
                + "]\",\"requestEntity\":\"test_body\"}";
        assertEquals(request.toString(), expectedString);

    }
}
