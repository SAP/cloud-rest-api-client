package com.sap.cloud.rest.api.client.config;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotEmptyOrNull;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;

import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;

import com.sap.cloud.rest.api.client.RestApiClient;
import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.none.NoAuthentication;

/**
 * This class represents a configuration object used to configure the
 * {@link RestApiClient}.
 *
 */
public class RestApiClientConfig {

    static final String HOST_DISPLAY_NAME = "Host";
    static final String AUTHENTICATION_DISPLAY_NAME = "Authentication";
    static final String ROUTE_PLANNER_DISPLAY_NAME = "Route Planner";

    private final String host;
    private final Authentication authentication;
    private final HttpRoutePlanner routePlanner;

    /**
     * Creates a {@link RestApiClientConfig} instance with the given host,
     * {@link NoAuthentication} for authentication and default route planner.
     * @param host Endpoint host.
     */
    public RestApiClientConfig(String host) {
        this(host, new NoAuthentication());
    }

    /**
     * Creates a {@link RestApiClientConfig} instance with the given host and
     * route planner and {@link NoAuthentication} for authentication.
     * @param host Endpoint host.
     * @param routePlanner Route planner.
     */
    public RestApiClientConfig(String host, HttpRoutePlanner routePlanner) {
        this(host, new NoAuthentication(), routePlanner);
    }

    /**
     * Creates a {@link RestApiClientConfig} instance with the given host and
     * authentication and the default route planner.
     * @param host Endpoint host.
     * @param authentication Authentication configuration.
     */
    public RestApiClientConfig(String host, Authentication authentication) {
        this(host, authentication, new DefaultRoutePlanner(DefaultSchemePortResolver.INSTANCE));
    }

    /**
     * Creates a {@link RestApiClientConfig} instance with the given host,
     * authentication and route planner.
     * @param host Endpoint host.
     * @param authentication Authentication configuration.
     * @param routePlanner Route planner.
     */
    public RestApiClientConfig(String host, Authentication authentication, HttpRoutePlanner routePlanner) {
        isNotEmptyOrNull(HOST_DISPLAY_NAME, host);
        isNotNull(AUTHENTICATION_DISPLAY_NAME, authentication);
        isNotNull(ROUTE_PLANNER_DISPLAY_NAME, routePlanner);

        this.host = host;
        this.authentication = authentication;
        this.routePlanner = routePlanner;
    }

    /**
     * @return Returns endpoint host.
     */
    public String getHost() {
        return host;
    }

    /**
     * @return Returns authentication configuration.
     */
    public Authentication getAuthentication() {
        return authentication;
    }

    /**
     * @return Returns configured route planner.
     */
    public HttpRoutePlanner getRoutePlanner() {
        return routePlanner;
    }
}