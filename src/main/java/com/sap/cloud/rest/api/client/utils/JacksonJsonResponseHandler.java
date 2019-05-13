package com.sap.cloud.rest.api.client.utils;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.rest.api.client.handler.DefaultResponseHandler;

/**
 * This implementation of {@link ResponseHandler} expects that the received HTTP
 * response body is in JSON format.
 *
 * The received JSON is parsed using the Jackson library and returned as an
 * object of type T.
 *
 * @param <T>
 *            type of the object, the JSON needs to be parsed to.
 */
public class JacksonJsonResponseHandler<T> implements ResponseHandler<T> {

    private Class<T> clazz;
    private TypeReference<T> type;

    private ObjectMapper objectMapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    public JacksonJsonResponseHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JacksonJsonResponseHandler(TypeReference<T> type) {
        this.type = type;
    }

    @Override
    public T handleResponse(HttpResponse response) throws IOException {
        ResponseHandler<String> handler = new DefaultResponseHandler();
        String body = handler.handleResponse(response);

        if (type != null) {
            return body.isEmpty() ? null : objectMapper.readValue(body, type);
        }
        return body.isEmpty() ? null : objectMapper.readValue(body, clazz);
    }
}
