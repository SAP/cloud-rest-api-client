package com.sap.cloud.rest.api.client.auth.none;

import static com.sap.cloud.rest.api.client.utils.test.HttpClientProviderUtils.getHttpClientProviderForAuthentication;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.http.client.HttpClient;
import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.none.NoAuthHttpClientProvider;
import com.sap.cloud.rest.api.client.auth.none.NoAuthentication;
import com.sap.cloud.rest.api.client.http.HttpClientCreationException;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;

public class NoAuthHttpClientProviderTest {

    @Test
    public void createHttpClient() throws HttpClientCreationException {
        HttpClientProvider httpClientProvider = getHttpClientProviderForAuthentication(new NoAuthentication());

        assertTrue(httpClientProvider instanceof NoAuthHttpClientProvider);

        HttpClient httpClient = httpClientProvider.createHttpClient();
        assertNotNull(httpClient);
    }
}
