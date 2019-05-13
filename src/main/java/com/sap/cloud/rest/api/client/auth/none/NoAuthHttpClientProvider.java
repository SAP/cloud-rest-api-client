package com.sap.cloud.rest.api.client.auth.none;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.HttpClients;

import com.sap.cloud.rest.api.client.http.HttpClientCreationException;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

public class NoAuthHttpClientProvider extends HttpClientProvider {

    @Override
    public HttpClient createHttpClient(HttpRoutePlanner routePlanner) throws HttpClientCreationException {
        return HttpClients.custom()
                .useSystemProperties()
                .setRoutePlanner(routePlanner)
                .build();
    }
}
