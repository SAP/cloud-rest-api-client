package com.sap.cloud.rest.api.client.http;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;

public abstract class HttpClientProvider {

    public HttpClient createHttpClient() throws HttpClientCreationException {
        return createHttpClient(new DefaultRoutePlanner(DefaultSchemePortResolver.INSTANCE));
    }

    public abstract HttpClient createHttpClient(HttpRoutePlanner proxyRouteHandler) throws HttpClientCreationException;
}
