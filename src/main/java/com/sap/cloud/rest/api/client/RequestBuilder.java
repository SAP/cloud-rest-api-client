package com.sap.cloud.rest.api.client;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotEmptyOrNull;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;
import static java.text.MessageFormat.format;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.rest.api.client.exceptions.RequestBuilderException;
import com.sap.cloud.rest.api.client.model.Request;
import com.sap.cloud.rest.api.client.model.multipart.EntityPart;
import com.sap.cloud.rest.api.client.model.multipart.MultipartEntity;

/**
 * A builder for {@link Request}s. Initialize using the static methods to create
 * a builder for a specific HTTP method.
 * 
 * Supports JSON deserialization for POST, PUT and PATCH requests, using Jackson
 * library. To use it pass the type of the object you want to deserialize as a
 * {@link Class} object when initializing the builder and then pass an object of
 * type {@link T} to the entity method. <BR>
 * 
 * Supports multipart requests. To build a multipart {@link Request}, add entity
 * parts using the multipartEntity method and build the request with the
 * buildMultipart method.
 *
 * @param <T>
 *            the type of the {@link Request} that will be build. If no
 *            {@link Class} object is provided during initialization, T defaults
 *            to type {@link String}. If multipart, the {@link Request} will be
 *            of type {@link MultipartEntity} with this generic type.
 */
public class RequestBuilder<T> {

    static final String FAILED_TO_BUILD_REQUEST_OBJECT_MSG = "Failed to build request object. ";
    static final String FAILED_TO_PARSE_ENTITY_TO_JSON_MSG = "Failed to parse entity [{0}] to json.";
    static final String FAILED_TO_SET_URI_X_MSG = "Failed to set URI. Provided URI [{0}] is invalid.";

    static final String ADDING_QUERY_PARAMS_FAILED_MSG = "Adding query parameters failed.";
    static final String URI_IS_NOT_SET_MSG = "URI is not set.";

    static final String ENTITY_DISPLAY_NAME = "Entity";
    static final String NAME_DISPLAY_NAME = "Name";

    private org.apache.http.client.methods.RequestBuilder requestBuilder;
    private MultipartEntityBuilder multipartEntityBuilder;
    private EntityBuilder entityBuilder;

    private List<NameValuePair> parameters;
    private ObjectMapper objectMapper;
    private List<EntityPart<T>> multipartEntities;
    private T entity;

    public static <T> RequestBuilder<T> postRequest(Class<T> clazz) {
        return new RequestBuilder<T>(org.apache.http.client.methods.RequestBuilder.post());
    }

    public static <T> RequestBuilder<T> putRequest(Class<T> clazz) {
        return new RequestBuilder<T>(org.apache.http.client.methods.RequestBuilder.put());
    }

    public static <T> RequestBuilder<T> patchRequest(Class<T> clazz) {
        return new RequestBuilder<T>(org.apache.http.client.methods.RequestBuilder.patch());
    }

    public static RequestBuilder<String> patchRequest() {
        return new RequestBuilder<>(org.apache.http.client.methods.RequestBuilder.patch());
    }

    public static RequestBuilder<String> getRequest() {
        return new RequestBuilder<>(org.apache.http.client.methods.RequestBuilder.get());
    }

    public static RequestBuilder<String> postRequest() {
        return new RequestBuilder<>(org.apache.http.client.methods.RequestBuilder.post());
    }

    public static RequestBuilder<String> putRequest() {
        return new RequestBuilder<>(org.apache.http.client.methods.RequestBuilder.put());
    }

    public static RequestBuilder<String> deleteRequest() {
        return new RequestBuilder<>(org.apache.http.client.methods.RequestBuilder.delete());
    }

    private RequestBuilder(org.apache.http.client.methods.RequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
        this.entityBuilder = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON);
        this.multipartEntityBuilder = MultipartEntityBuilder.create();

        this.parameters = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.multipartEntities = new ArrayList<>();
        this.entity = null;
    }

    public RequestBuilder<T> uri(String uri) {
        try {
            requestBuilder.setUri(uri);
        } catch (IllegalArgumentException e) {
            throw new RequestBuilderException(format(FAILED_TO_SET_URI_X_MSG, uri), e);
        }
        return this;
    }

    public RequestBuilder<T> uri(URI uri) {
        return uri(uri.toString());
    }

    public RequestBuilder<T> addHeader(String name, String value) {
        requestBuilder.addHeader(name, value);
        return this;
    }

    public RequestBuilder<T> addHeader(Header header) {
        requestBuilder.addHeader(header);
        return this;
    }

    public RequestBuilder<T> addHeaders(List<Header> headers) {
        for (Header header : headers) {
            requestBuilder.addHeader(header);
        }
        return this;
    }

    public RequestBuilder<T> addHeaders(Header[] headers) {
        return addHeaders(Arrays.asList(headers));
    }

    public RequestBuilder<T> addParameter(String param, String value) {
        parameters.add(new BasicNameValuePair(param, value));
        return this;
    }

    public RequestBuilder<T> addParameters(List<NameValuePair> nvps) {
        parameters.addAll(nvps);
        return this;
    }

    /**
     * Sets an entity as request body. If the entity is not of type
     * {@link String} it is deserialized using Jackson.
     * 
     * @param entity the request entity
     * @return request builder
     * @throws RequestBuilderException
     *             if Jackson fails to parse the provided entity
     */
    public RequestBuilder<T> entity(T entity) {
        isNotNull(ENTITY_DISPLAY_NAME, entity);

        if (entity instanceof String) {
            entityBuilder.setText((String) entity);
            this.entity = entity;
            return this;
        }
        try {
            entityBuilder.setText(objectMapper.writeValueAsString(entity));
            this.entity = entity;
            return this;
        } catch (JsonProcessingException e) {
            throw new RequestBuilderException(format(FAILED_TO_PARSE_ENTITY_TO_JSON_MSG, entity), e);
        }
    }

    /**
     * Adds an entity part to the request builder in case a multipart request is
     * to be created. The {@link Request} object is to be build with the
     * buildMultipart() method instead of the normal build() method in order to
     * use multipart entities that are set.
     * 
     * @param name the name of the entity part
     * @param entity an entity representing the part
     * @throws RequestBuilderException
     *             if Jackson fails to parse the provided entity
     * @return RequestBuilder instance.
     */
    public RequestBuilder<T> multipartEntity(String name, T entity) {
        isNotEmptyOrNull(NAME_DISPLAY_NAME, name);
        isNotNull(ENTITY_DISPLAY_NAME, entity);

        if (entity instanceof String) {
            multipartEntityBuilder.addTextBody(name, (String) entity);
        } else {
            try {
                String jsonObj = objectMapper.writeValueAsString(entity);
                Properties props = objectMapper.readValue(jsonObj, Properties.class);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                props.store(outputStream, null);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

                multipartEntityBuilder.addPart(name, new InputStreamBody(inputStream, name));
            } catch (IOException e) {
                throw new RequestBuilderException(format(FAILED_TO_PARSE_ENTITY_TO_JSON_MSG, entity), e);
            }
        }

        this.multipartEntities.add(new EntityPart<>(name, entity));
        return this;
    }

    /**
     * Builds a {@link Request} with generic type {@link T} with the provided
     * URI and optionally an request entity, any headers and any query
     * parameters if set.
     * 
     * @throws RequestBuilderException
     *             in case of a problem while building the request
     * @return Request instance.
     */
    public Request<T> build() {
        if (requestBuilder.getUri() == null) {
            throw new RequestBuilderException(FAILED_TO_BUILD_REQUEST_OBJECT_MSG + URI_IS_NOT_SET_MSG);
        }
        if (!parameters.isEmpty()) {
            this.uri(getUriWithParametersSet());
        }
        if (entity != null) {
            requestBuilder.setEntity(entityBuilder.build());
        }

        return new Request<>(requestBuilder.build(), entity);
    }

    /**
     * Builds a multipart {@link Request} of type {@link MultipartEntity} with
     * generic type {@link T} with the provided URI and optionally any request
     * entity parts, any headers and any query parameters if set.
     * 
     * @throws RequestBuilderException
     *             in case of a problem while building the request
     * @return Returns multipart request.
     */
    public Request<MultipartEntity<T>> buildMultipart() {
        if (requestBuilder.getUri() == null) {
            throw new RequestBuilderException(FAILED_TO_BUILD_REQUEST_OBJECT_MSG + URI_IS_NOT_SET_MSG);
        }
        if (!parameters.isEmpty()) {
            this.uri(getUriWithParametersSet());
        }
        if (!multipartEntities.isEmpty()) {
            requestBuilder.setEntity(multipartEntityBuilder.build());
        }

        return new Request<>(requestBuilder.build(), new MultipartEntity<>(multipartEntities));
    }

    protected String getUriWithParametersSet() {
        try {
            URI uri = requestBuilder.getUri();
            return URLDecoder.decode(new URIBuilder(uri)
                    .setParameters(parameters)
                    .build().toString(), StandardCharsets.UTF_8.name());
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            throw new RequestBuilderException(FAILED_TO_BUILD_REQUEST_OBJECT_MSG + ADDING_QUERY_PARAMS_FAILED_MSG, e);
        }
    }
}
