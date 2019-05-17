package com.sap.cloud.rest.api.client.auth;

/**
 * An enumerable that represents the supported authentication types
 */
public enum AuthenticationType {
    /**
     * HTTP Basic Authentication.
     */
    BASIC,
    /**
     * Client Certificate Authentication.
     */
    CLIENT_CERT, 
    /**
     * OAuth Client Credentials flow.
     */
    OAUTH,
    /**
     * No authentication.
     */
    NO_AUTH
}
