package com.sap.cloud.rest.api.client.utils.test;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;
import com.sap.cloud.rest.api.client.http.HttpClientProviderFactory;

public class HttpClientProviderUtils {

    private static final String TEST_HOST = "http://test.com";

    public static HttpClientProvider getHttpClientProviderForAuthentication(Authentication authentication) {
        RestApiClientConfig config = new RestApiClientConfig(TEST_HOST, authentication);
        HttpClientProvider basicHttpClientProvider = HttpClientProviderFactory.createHttpClientProvider(config);
        return basicHttpClientProvider;
    }
}
