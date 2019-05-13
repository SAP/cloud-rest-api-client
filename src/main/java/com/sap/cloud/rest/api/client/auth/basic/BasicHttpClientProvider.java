package com.sap.cloud.rest.api.client.auth.basic;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import com.sap.cloud.rest.api.client.http.HttpClientCreationException;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

public class BasicHttpClientProvider extends HttpClientProvider {

    private final String username;
    private final char[] password;

    public BasicHttpClientProvider(BasicAuthentication authentication) {
        this.username = authentication.getUsername();
        this.password = authentication.getPassword();
    }

    @Override
    public HttpClient createHttpClient(HttpRoutePlanner routePlanner) throws HttpClientCreationException {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, String.valueOf(password));
        provider.setCredentials(AuthScope.ANY, credentials);

        return HttpClientBuilder.create()
                .useSystemProperties()
                .setRoutePlanner(routePlanner)
                .setDefaultCredentialsProvider(provider)
                .build();
    }
}
