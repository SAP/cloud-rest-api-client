package com.sap.cloud.rest.api.client.utils.test;

import static org.mockito.Mockito.when;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.EntityBuilder;
import org.mockito.Mockito;

public class MockResponseUtil {

    public static HttpResponse makeMockedResponseWithStatusAndEntity(int statusCode, String responseText) {
        HttpResponse mockedResponse = makeMockedResponseWithStatusCode(statusCode);

        when(mockedResponse.getEntity()).thenReturn(
                EntityBuilder.create()
                        .setText(responseText)
                        .build());

        return mockedResponse;
    }

    public static HttpResponse makeMockedResponseWithStatusCode(int statusCode) {
        HttpResponse mockedResponse = Mockito.mock(HttpResponse.class);
        
        StatusLine mockedStatusLine = Mockito.mock(StatusLine.class);
        when(mockedResponse.getStatusLine()).thenReturn(mockedStatusLine);
        when(mockedStatusLine.getStatusCode()).thenReturn(statusCode);

        return mockedResponse;
    }

    public static HttpResponse makeMockedResponseWithEntity(String responseText) {
        HttpResponse mockedResponse = Mockito.mock(HttpResponse.class);

        when(mockedResponse.getEntity()).thenReturn(
                EntityBuilder.create()
                        .setText(responseText)
                        .build());

        return mockedResponse;
    }
}
