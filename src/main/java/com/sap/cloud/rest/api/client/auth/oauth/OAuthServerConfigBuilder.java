package com.sap.cloud.rest.api.client.auth.oauth;

import java.net.URL;

/**
 * A builder for @{link OAuthServerConfig}.
 */
public class OAuthServerConfigBuilder {

    private String oAuthServerHost;
    private String oAuthServerApiPath;
    private String oAuthHeaderKey;
    private String clientID;
    private char[] clientSecret;

    /**
     * Attaches an OAuth Server Host to the builder.
     * @param oAuthServerHost OAuth Server host.
     * @return {@link OAuthServerConfigBuilder} instance.
     */
    public OAuthServerConfigBuilder oAuthServerHost(final URL oAuthServerHost) {
        final String protocol = oAuthServerHost.getProtocol();
        final String host = oAuthServerHost.getHost();
        final int port = oAuthServerHost.getPort();

        this.oAuthServerHost = protocol + "://" + host;
        if (port > 0) {
            this.oAuthServerHost += ":" + port;
        }
        return this;
    }

    /**
     * Attaches an OAuth Server Host to the builder.
     * @param oAuthServerHost OAuth Server host.
     * @return {@link OAuthServerConfigBuilder} instance.
     */
    public OAuthServerConfigBuilder oAuthServerHost(final String oAuthServerHost) {
        this.oAuthServerHost = oAuthServerHost;
        return this;
    }

    /**
     * Attaches an OAuth Server API path to the builder.
     * @param oAuthServerApiPath OAuth Server API path
     * @return {@link OAuthServerConfigBuilder} instance.
     */
    public OAuthServerConfigBuilder oAuthServerApiPath(final String oAuthServerApiPath) {
        this.oAuthServerApiPath = oAuthServerApiPath;
        return this;
    }

    /**
     * Attaches a client ID to the builder.
     * @param clientID client ID
     * @return {@link OAuthServerConfigBuilder} instance.
     */
    public OAuthServerConfigBuilder clientID(final String clientID) {
        this.clientID = clientID;
        return this;
    }

    /**
     * Attaches a client secret to the builder.
     * @param clientSecret a client secret
     * @return {@link OAuthServerConfigBuilder} instance.
     */
    public OAuthServerConfigBuilder clientSecret(final char[] clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * Attaches a custom OAuth header to the builder
     * @param oAuthHeaderKey custom OAuth header.
     * @return {@link OAuthServerConfigBuilder} instance.
     */
    public OAuthServerConfigBuilder oAuthHeaderKey(String oAuthHeaderKey) {
        this.oAuthHeaderKey = oAuthHeaderKey;
        return this;
    }

    /**
     * Builds a {@link OAuthServerConfig} with the provided OAuth Server Host,
     * OAuth Server API Path, client ID and client secret and OAuth Header key.
     * If API path or OAuth Header key are not provided, the default ones from
     * {@link OAuthServerConfig} are used.
     * @return {@link OAuthServerConfig} instance.
     */
    public OAuthServerConfig build() {
        if (oAuthServerApiPath != null && oAuthHeaderKey != null) {
            return new OAuthServerConfig(oAuthServerHost, oAuthServerApiPath, clientID, clientSecret, oAuthHeaderKey);
        }
        if (oAuthServerApiPath != null) {
            return new OAuthServerConfig(oAuthServerHost, oAuthServerApiPath, clientID, clientSecret);
        }
        if (oAuthHeaderKey != null) {
            return new OAuthServerConfig(oAuthServerHost, clientID, clientSecret, oAuthHeaderKey);
        }
        return new OAuthServerConfig(oAuthServerHost, clientID, clientSecret);
    }

    /**
     * @return Returns an instance of {@link OAuthServerConfigBuilder}.
     */
    public static OAuthServerConfigBuilder getBuilder() {
        return new OAuthServerConfigBuilder();
    }
}
