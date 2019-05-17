package com.sap.cloud.rest.api.client.auth.oauth;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotEmptyOrNull;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isValidUrl;

import org.apache.http.HttpHeaders;

/**
 * This class represents a configuration object used to configure an
 * {@link AccessTokenProvider}.
 *
 */
public class OAuthServerConfig {

    static final String CLIENT_SECRET_DISPLAY_NAME = "Client secret";
    static final String CLIENT_ID_DISPLAY_NAME = "Client ID";
    static final String OAUTH_SERVER_HOST_DISPLAY_NAME = "OAuth server host";
    static final String OAUTH_SERVER_API_PATH_DISPLAY_NAME = "OAuth server API path";
    static final String OAUTH_HEADER_KEY_DISPLAY_NAME = "OAuth server host";

    static final String DEFAULT_OAUTH_SERVER_API_PATH = "/oauth/token";
    static final String DEFAULT_OAUTH_HEADER_KEY = HttpHeaders.AUTHORIZATION;

    private final String oAuthServerHost;
    private final String oAuthServerApiPath;
    private final String oAuthHeaderKey;
    private final String clientID;
    private final char[] clientSecret;

    /**
     * This constructor allows omitting the API path and header key. In this
     * case, <b>/oauth/token</b> is used for API path and <b>Authorization</b>
     * for header key as the default values.
     * @param oAuthServerHost OAuth server host.
     * @param clientID Client ID.
     * @param clientSecret Client secret.
     */
    public OAuthServerConfig(final String oAuthServerHost, final String clientID, final char[] clientSecret) {
        this(oAuthServerHost, DEFAULT_OAUTH_SERVER_API_PATH, clientID, clientSecret);
    }

    /**
     * This constructor allows omitting the header key. In this case,
     * <b>Authorization</b> is used for header key as the default value.
     * @param oAuthServerHost OAuth server host.
     * @param oAuthServerApiPath API Path
     * @param clientID Client ID.
     * @param clientSecret Client secret.
     */
    public OAuthServerConfig(final String oAuthServerHost, final String oAuthServerApiPath, final String clientID,
            final char[] clientSecret) {
        this(oAuthServerHost, oAuthServerApiPath, clientID, clientSecret, DEFAULT_OAUTH_HEADER_KEY);
    }

    /**
     * This constructor allows omitting the API path. In this case,
     * <b>/oauth/token</b> is used the API path as the default value.
     * @param oAuthServerHost OAuth server host.
     * @param clientID Client ID.
     * @param clientSecret Client secret.
     * @param oAuthHeaderKey OAuth Header key.
     */
    public OAuthServerConfig(final String oAuthServerHost, final String clientID, final char[] clientSecret,
            final String oAuthHeaderKey) {
        this(oAuthServerHost, DEFAULT_OAUTH_SERVER_API_PATH, clientID, clientSecret, oAuthHeaderKey);
    }

    /**
     * Fill OAuth server configuration constructor.
     * @param oAuthServerHost OAuth server host.
     * @param oAuthServerApiPath API Path
     * @param clientID Client ID.
     * @param clientSecret Client secret.
     * @param oAuthHeaderKey OAuth Header key.
     */
    public OAuthServerConfig(final String oAuthServerHost, final String oAuthServerApiPath, final String clientID,
            final char[] clientSecret, final String oAuthHeaderKey) {
        isNotEmptyOrNull(OAUTH_SERVER_HOST_DISPLAY_NAME, oAuthServerHost);
        isNotNull(OAUTH_SERVER_API_PATH_DISPLAY_NAME, oAuthServerApiPath);
        isNotEmptyOrNull(OAUTH_HEADER_KEY_DISPLAY_NAME, oAuthHeaderKey);
        isNotEmptyOrNull(CLIENT_ID_DISPLAY_NAME, clientID);
        isNotNull(CLIENT_SECRET_DISPLAY_NAME, clientSecret);
        isValidUrl(OAUTH_SERVER_HOST_DISPLAY_NAME, oAuthServerHost);

        this.oAuthServerHost = oAuthServerHost;
        this.oAuthServerApiPath = oAuthServerApiPath;
        this.oAuthHeaderKey = oAuthHeaderKey;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
    }

    public String getOAuthServerHost() {
        return oAuthServerHost;
    }

    public String getoAuthServerApiPath() {
        return oAuthServerApiPath;
    }

    public String getClientID() {
        return clientID;
    }

    public char[] getClientSecret() {
        return clientSecret.clone();
    }

    public String getoAuthHeaderKey() {
        return oAuthHeaderKey;
    }
}
