package com.sap.cloud.rest.api.client.auth.oauth;

import com.sap.cloud.rest.api.client.RequestBuilder;
import com.sap.cloud.rest.api.client.RestApiClient;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;
import com.sap.cloud.rest.api.client.http.HttpClientProvider;
import com.sap.cloud.rest.api.client.model.Request;
import com.sap.cloud.rest.api.client.utils.JacksonJsonResponseHandler;

public class ClientCredentialsAccessTokenProvider extends RestApiClient implements AccessTokenProvider {

    private static final String RESPONSE_TYPE = "response_type";
    private static final String TOKEN = "token";

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_CREDENTIALS = "client_credentials";

    private String apiPath;

    public ClientCredentialsAccessTokenProvider(RestApiClientConfig restApiClientConfig, String apiPath) {
        super(restApiClientConfig);

        this.apiPath = apiPath;
    }

    public ClientCredentialsAccessTokenProvider(RestApiClientConfig restApiClientConfig,
            HttpClientProvider httpClientProvider, String apiPath) {
        super(restApiClientConfig, httpClientProvider);

        this.apiPath = apiPath;
    }

    @Override
    public String retrieveAccessToken() {
        Request<String> retrieveAccessTokenRequest = getAccessTokenRequestBuilder().build();

        AccessTokenResponseDto accessTokenResponse = execute(retrieveAccessTokenRequest,
                new JacksonJsonResponseHandler<>(AccessTokenResponseDto.class)).getEntity();
        return accessTokenResponse.getAccessToken();
    }

    public RequestBuilder<String> getAccessTokenRequestBuilder() {
        return RequestBuilder
                .postRequest()
                .uri(buildRequestUri())
                .addParameter(GRANT_TYPE, CLIENT_CREDENTIALS)
                .addParameter(RESPONSE_TYPE, TOKEN);
    }

    @Override
    protected String getApiPath() {
        return apiPath;
    }

}
