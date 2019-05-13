package com.sap.cloud.rest.api.client.auth.basic;

import static com.sap.cloud.rest.api.client.utils.test.HttpClientProviderUtils.getHttpClientProviderForAuthentication;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.http.client.HttpClient;
import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.basic.BasicAuthentication;
import com.sap.cloud.rest.api.client.auth.basic.BasicHttpClientProvider;
import com.sap.cloud.rest.api.client.http.HttpClientCreationException;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

public class BasicHttpClientProviderTest {

    private static final String USERNAME = "username";
    private static final char[] PASSWORD = "password".toCharArray();

    @Test
    public void createHttpClient() throws HttpClientCreationException {
        Authentication authentication = new BasicAuthentication(USERNAME, PASSWORD);

        HttpClientProvider httpClientProvider = getHttpClientProviderForAuthentication(authentication);

        assertTrue(httpClientProvider instanceof BasicHttpClientProvider);

        HttpClient httpClient = httpClientProvider.createHttpClient();
        assertNotNull(httpClient);
    }
}
