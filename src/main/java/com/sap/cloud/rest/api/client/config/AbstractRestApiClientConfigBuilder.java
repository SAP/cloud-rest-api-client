package com.sap.cloud.rest.api.client.config;

import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.basic.BasicAuthentication;
import com.sap.cloud.rest.api.client.auth.cert.ClientCertAuthentication;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfig;
import com.sap.cloud.rest.api.client.auth.none.NoAuthentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthAuthentication;
import com.sap.cloud.rest.api.client.auth.oauth.OAuthServerConfig;
import com.sap.cloud.rest.api.client.http.Proxy;

/**
 * A builder for {@link RestApiClientConfig}.
 */
public abstract class AbstractRestApiClientConfigBuilder<Builder extends AbstractRestApiClientConfigBuilder<Builder, Configuration>, Configuration extends RestApiClientConfig> {

    protected String host;
    protected Authentication authentication;
    protected HttpRoutePlanner routePlanner;

    /**
     * Attaches the host for the configuration to the builder.
     *
     * @param hostUrl
     *            the host that will be used by the client
     * @return Builder instance.
     */
    public Builder host(URL hostUrl) {
        final String protocol = hostUrl.getProtocol();
        final String host = hostUrl.getHost();
        final int port = hostUrl.getPort();

        String hostString = protocol + "://" + host;
        if (port > 0) {
            hostString += ":" + port;
        }

        return host(hostString);
    }

    /**
     * Attaches the host for the configuration to the builder.
     * 
     * @param host
     *            the host that will be used by the client
     * @return Builder instance.
     */
    public Builder host(String host) {
        this.host = host;
        return self();
    }

    /**
     * Attaches the provided authentication to the builder.
     * @param authentication authentication configuration.
     * @return Builder instance.
     */
    public Builder authentication(Authentication authentication) {
        this.authentication = authentication;
        return self();
    }

    /**
     * Attaches a {@link BasicAuthentication} as the authentication type for the
     * configuration to the builder.
     * 
     * @param username
     *            the username used to create the Authorization header
     * @param password
     *            the password used to create the Authorization header
     * @return Builder instance.
     */
    public Builder basicAuthentication(String username, char[] password) {
        return authentication(new BasicAuthentication(username, password));
    }

    /**
     * Attaches an {@link OAuthAuthentication} as the authentication type for
     * the configuration to the builder.
     * 
     * @param oAuthServerConfig
     *            the {@link OAuthServerConfig} needed for creation of
     *            {@link OAuthAuthentication}
     * @return Builder instance.
     */
    public Builder oAuthAuthentication(OAuthServerConfig oAuthServerConfig) {
        return authentication(new OAuthAuthentication(oAuthServerConfig));
    }

    /**
     * Attaches a {@link ClientCertAuthentication} as the authentication type
     * for the configuration to the builder.
     * 
     * @param keystoreConfig
     *            the key store configuration used to the set the SSL context
     * @return Builder instance.
     */
    public Builder clientCertAuthentication(KeystoreConfig keystoreConfig) {
        return authentication(new ClientCertAuthentication(keystoreConfig));
    }

    /**
     * Attaches {@link NoAuthentication} as the authentication type for the
     * configuration to the builder.
     * @return Builder instance.
     */
    public Builder noAuthentication() {
        return authentication(new NoAuthentication());
    }

    /**
     * Attaches a proxy route planner, with an {@link HttpHost} created using
     * the provided {@link Proxy}, to the builder.
     * @param proxy Proxy
     * @return Builder instance.
     */
    public Builder proxy(Proxy proxy) {
        if (proxy != null) {
            HttpHost httpHost = new HttpHost(proxy.getProxyHost(), proxy.getProxyPort(),
                    proxy.getProxyScheme());
            this.routePlanner = new DefaultProxyRoutePlanner(httpHost);
        }
        return self();
    }

    /**
     * Attaches a proxy route planner using the provided
     * {@link HttpRoutePlanner}, to the builder.
     * @param routePlanner Route planner
     * @return Builder instance.
     */
    public Builder proxy(HttpRoutePlanner routePlanner) {
        this.routePlanner = routePlanner;
        return self();
    }

    /**
     * Override to return an instance of {@link Configuration} with the attached
     * to the builder host, authentication and any subclass properties.
     * @return Configuration instance.
     */
    public abstract Configuration build();

    /**
     * Override to return the instance of the subclass {@link Builder}.
     * @return Builder instance.
     */
    protected abstract Builder self();
}
