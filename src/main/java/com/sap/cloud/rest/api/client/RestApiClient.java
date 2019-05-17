package com.sap.cloud.rest.api.client;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;
import static java.text.MessageFormat.format;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;
import com.sap.cloud.rest.api.client.exceptions.ConnectionException;
import com.sap.cloud.rest.api.client.exceptions.ResponseException;
import com.sap.cloud.rest.api.client.handler.DefaultResponseHandler;
import com.sap.cloud.rest.api.client.handler.DefaultStatusCodeHandler;
import com.sap.cloud.rest.api.client.handler.StatusCodeHandler;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;
import com.sap.cloud.rest.api.client.http.HttpClientProviderFactory;
import com.sap.cloud.rest.api.client.model.HttpExchangeContext;
import com.sap.cloud.rest.api.client.model.Request;
import com.sap.cloud.rest.api.client.model.Response;

/**
 * A generic Java client for Rest API calls. Capable of executing a
 * {@link Request} and returning a {@link Response}. Requires an
 * {@link RestApiClientConfig} to construct. Provided with utility methods for
 * building the needed {@link URI} and {@link HttpEntity}.
 */
public abstract class RestApiClient {

    static final String IO_EXCEPTION_WHILE_HANDLING_RESPONSE_MSG = "IOException occurred while handling response. Context: [{0}].";
    static final String IO_EXCEPTION_WHILE_EXECUTING_REQUEST_MSG = "IOException occurred while executing request. Request: [{0}].";
    static final String PATH_NOT_VALID_MSG = "The given path is not a valid URI.";
    static final String HOST_NOT_VALID_MSG = "Host [{0}] is not a valid URI.";

    static final String HTTP_CLIENT_PROVIDER_DISPLAY_NAME = "HTTP client provider";
    static final String CONFIG_DISPLAY_NAME = "Configuration";

    private final HttpClient httpClient;
    private final URL host;

    /**
     * Creates a {@link RestApiClient} instance with the given configuration.
     *
     * @param restApiClientConfig the configuration to be used.
     */
    protected RestApiClient(RestApiClientConfig restApiClientConfig) {
        isNotNull(CONFIG_DISPLAY_NAME, restApiClientConfig);
        HttpClientProvider httpClientProvider = HttpClientProviderFactory
                .createHttpClientProvider(restApiClientConfig);

        this.httpClient = httpClientProvider.createHttpClient(restApiClientConfig.getRoutePlanner());
        this.host = getHostAsURL(restApiClientConfig.getHost());
    }

    /**
     * A constructor which allows providing a custom {@link HttpClientProvider}.
     *
     * @param restApiClientConfig
     *            the configuration to be used.
     * @param httpClientProvider
     *            the HTTP client provider to be used to create HTTP clients.
     */
    protected RestApiClient(RestApiClientConfig restApiClientConfig, HttpClientProvider httpClientProvider) {
        isNotNull(HTTP_CLIENT_PROVIDER_DISPLAY_NAME, httpClientProvider);
        isNotNull(CONFIG_DISPLAY_NAME, restApiClientConfig);

        this.httpClient = httpClientProvider.createHttpClient(restApiClientConfig.getRoutePlanner());
        this.host = getHostAsURL(restApiClientConfig.getHost());
    }

    private URL getHostAsURL(String host) {
        try {
            return new URL(host);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(format(HOST_NOT_VALID_MSG, host), e);
        }
    }

    /**
     * An abstract method that should be overridden. Implementation should
     * return the API path of the client as String.
     * 
     * @return API path
     */
    protected abstract String getApiPath();

    /**
     * Construct a URI appending a provided path to the host and API path.
     * 
     * @param pathPattern
     *            pattern used to build the path in the format of
     *            {@link MessageFormat}
     * @param args
     *            arguments used the build the path
     * @return the constructed URI object
     */
    protected URI buildRequestUri(String pathPattern, Object... args) {
        String requestPath = format(pathPattern, args);

        try {
            URI uri = host.toURI();
            return new URIBuilder(uri)
                    .setPath(uri.getPath() + getApiPath() + requestPath)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(PATH_NOT_VALID_MSG, e);
        }
    }

    /**
     * Construct a URI from the host and API path.
     * 
     * @return the constructed URI object
     */
    protected URI buildRequestUri() {
        return buildRequestUri("");
    }

    /**
     * Executes a {@link Request} and returns a {@link Response} using the
     * default response and status code handlers.
     * 
     * @param <RequestType>
     *            the type of the request body
     * @param request
     *            the request to be executed
     * @return a Response object constructed from the HTTP response.
     * @throws ConnectionException
     *             thrown in case of an {@link IOException}.
     * @throws ResponseException
     *             may be thrown by the status code handler or in case of an
     *             {@link IOException} while handling response.
     */
    protected <RequestType> Response<String> execute(Request<RequestType> request)
            throws ConnectionException, ResponseException {
        return execute(request, getDefaultResponseHandler());
    }

    /**
     * Executes a {@link Request} and returns a {@link Response} using the
     * default status code handler and custom {@link ResponseHandler}.
     * 
     * @param <RequestType>
     *            the type of the request body
     * @param <ResponseType>
     *            the type of the response body
     * @param request
     *            the request to be executed
     * @param responseHandler
     *            the custom response handler.
     * @return a Response object constructed from the HTTP response.
     * @throws ConnectionException
     *             thrown in case of an {@link IOException}.
     * @throws ResponseException
     *             may be thrown by the status code handler or in case of an
     *             {@link IOException} while handling response.
     */
    protected <RequestType, ResponseType> Response<ResponseType> execute(Request<RequestType> request,
            ResponseHandler<ResponseType> responseHandler) throws ConnectionException, ResponseException {
        return execute(request, responseHandler, getDefaultStatusCodeHandler());
    }

    /**
     * Executes a {@link Request} and returns a {@link Response} using the
     * default response handler and custom {@link StatusCodeHandler}.
     * 
     * @param <RequestType>
     *            the type of the request body
     * @param request
     *            the request to be executed
     * @param statusCodeHandler
     *            the custom response handler.
     * @return a Response object constructed from the HTTP response.
     * @throws ConnectionException
     *             thrown in case of an {@link IOException}.
     * @throws ResponseException
     *             may be thrown by the status code handler or in case of an
     *             {@link IOException} while handling response.
     */
    protected <RequestType> Response<String> execute(Request<RequestType> request, StatusCodeHandler statusCodeHandler)
            throws ConnectionException, ResponseException {
        return execute(request, getDefaultResponseHandler(), statusCodeHandler);
    }

    /**
     * Executes a {@link Request} using custom {@link ResponseHandler} and
     * {@link StatusCodeHandler}.
     * 
     * @param <RequestType>
     *            the type of the request body
     * @param <ResponseType>
     *            the type of the response body
     * @param request
     *            the request to be executed
     * @param responseHandler
     *            the custom response handler.
     * @param statusCodeHandler
     *            the custom response handler.
     * @return a {@link Response} object constructed from the HTTP response.
     * @throws ConnectionException
     *             thrown in case of an {@link IOException}.
     * @throws ResponseException
     *             may be thrown by the status code handler or in case of an
     *             {@link IOException} while handling response.
     */
    protected <RequestType, ResponseType> Response<ResponseType> execute(Request<RequestType> request,
            ResponseHandler<ResponseType> responseHandler, StatusCodeHandler statusCodeHandler)
            throws ConnectionException, ResponseException {
        try {
            HttpResponse httpResponse = httpClient.execute(request.getHttpRequest());
            return handleResponse(request, httpResponse, responseHandler, statusCodeHandler);
        } catch (IOException e) {
            throw new ConnectionException(format(IO_EXCEPTION_WHILE_EXECUTING_REQUEST_MSG, request), e,
                    getStringRequest(request));
        }
    }

    private <RequestType, ResponseType> Response<ResponseType> handleResponse(Request<RequestType> request,
            HttpResponse httpResponse, ResponseHandler<ResponseType> responseHandler,
            StatusCodeHandler statusCodeHandler) {
        ResponseType responseEntity = null;
        try {
            responseEntity = responseHandler.handleResponse(httpResponse);
        } catch (IOException e) {
            HttpExchangeContext context = buildContext(request, httpResponse, responseEntity);
            throw new ResponseException(format(IO_EXCEPTION_WHILE_HANDLING_RESPONSE_MSG, context), context, e);
        }
        handleStatusCode(request, httpResponse, responseEntity, statusCodeHandler);
        return new Response<ResponseType>(httpResponse, responseEntity);
    }

    private <RequestType, ResponseType> void handleStatusCode(Request<RequestType> request, HttpResponse httpResponse,
            ResponseType responseEntity, StatusCodeHandler statusCodeHandler) {
        HttpExchangeContext context = buildContext(request, httpResponse, responseEntity);

        statusCodeHandler.handleStatusCode(context.getResponse().getStatusCode(), context);
    }

    private <ResponseType, RequestType> HttpExchangeContext buildContext(Request<RequestType> request,
            HttpResponse httpResponse, ResponseType responseEntity) {
        Request<String> contextRequest = getStringRequest(request);
        Response<String> contextResponse = getStringResponse(httpResponse, responseEntity);
        HttpExchangeContext context = new HttpExchangeContext(contextRequest, contextResponse);
        return context;
    }

    private <RequestType> Request<String> getStringRequest(Request<RequestType> request) {
        HttpUriRequest httpRequest = request.getHttpRequest();
        RequestType requestEntity = request.getEntity();

        if (requestEntity == null) {
            return new Request<>(httpRequest);
        }
        if (requestEntity instanceof String) {
            return new Request<>(httpRequest, (String) requestEntity);
        }
        try {
            String requestBody = new ObjectMapper().writeValueAsString(requestEntity);
            return new Request<>(httpRequest, requestBody);
        } catch (JsonProcessingException e) {
            return new Request<>(httpRequest);
        }
    }

    private <ResponseType> Response<String> getStringResponse(HttpResponse httpResponse, ResponseType responseEntity) {
        return new Response<>(httpResponse, responseEntity == null ? "" : responseEntity.toString());
    }

    /**
     * @return Returns a {@link DefaultResponseHandler}
     */
    protected ResponseHandler<String> getDefaultResponseHandler() {
        return new DefaultResponseHandler();
    }

    /**
     * @return Returns a {@link DefaultStatusCodeHandler}
     */
    protected StatusCodeHandler getDefaultStatusCodeHandler() {
        return DefaultStatusCodeHandler.create();
    }
}