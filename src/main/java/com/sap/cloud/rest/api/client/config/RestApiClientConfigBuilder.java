package com.sap.cloud.rest.api.client.config;

public class RestApiClientConfigBuilder
        extends AbstractRestApiClientConfigBuilder<RestApiClientConfigBuilder, RestApiClientConfig> {

    @Override
    public RestApiClientConfig build() {
        if (authentication != null && routePlanner != null) {
            return new RestApiClientConfig(host, authentication, routePlanner);
        }
        if (authentication != null) {
            return new RestApiClientConfig(host, authentication);
        }
        if (routePlanner != null) {
            return new RestApiClientConfig(host, routePlanner);
        }
        return new RestApiClientConfig(host);
    }

    @Override
    public RestApiClientConfigBuilder self() {
        return this;
    }

    /**
     * Returns an instance of {@link RestApiClientConfigBuilder}
     */
    public static RestApiClientConfigBuilder getBuilder() {
        return new RestApiClientConfigBuilder();
    }
}