package com.sap.cloud.rest.api.client.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.rest.api.client.handler.DefaultResponseHandler;

/**
 * This implementation of {@link ResponseHandler} expects that the received HTTP
 * response body is in {@link Properties} format.
 * 
 * The received body is stored in {@link Properties} object and serialized to
 * JSON format. After that the JSON is parsed using the Jackson library and
 * returned as an object of type T.
 *
 * @param <T>
 *            type of the object, the {@link Properties} needs to be parsed to.
 */
public class PropertiesResponseHandler<T> implements ResponseHandler<T> {

    private Class<T> clazz;

    private ObjectMapper objectMapper = new ObjectMapper();

    public PropertiesResponseHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T handleResponse(HttpResponse response) throws IOException {
        ResponseHandler<String> handler = new DefaultResponseHandler();
        String body = handler.handleResponse(response);

        return body.isEmpty() ? null : objectMapper.readValue(convertPropertiesToJson(body), clazz);
    }

    private String convertPropertiesToJson(String body) throws IOException, JsonProcessingException {
        Properties props = new Properties();

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8))) {
            props.load(inputStream);
        }

        return objectMapper.writeValueAsString(props);
    }

}