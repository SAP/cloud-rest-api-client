package com.sap.cloud.rest.api.client.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.sap.cloud.rest.api.client.exceptions.ConnectionException;

/**
 * This implementation of {@link ResponseHandler} parses the entity of the
 * received {link {@link HttpResponse} as a String. Returns an empty String if
 * the response entity is null.
 *
 */
public class DefaultResponseHandler implements ResponseHandler<String> {

    @Override
    public String handleResponse(final HttpResponse httpResponse)
            throws ConnectionException, IOException {
        HttpEntity responseEntity = httpResponse.getEntity();
        return responseEntity == null ? "" : EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
    }
}
