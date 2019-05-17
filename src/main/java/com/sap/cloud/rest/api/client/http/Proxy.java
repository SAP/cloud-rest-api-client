package com.sap.cloud.rest.api.client.http;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a proxy. Consists of proxy host, proxy port and proxy scheme. The
 * default proxy scheme is "http".
 */
public class Proxy {

    private static final String DEFAULT_PROXY_SCHEME = "http";

    private final String proxyScheme;
    private final String proxyHost;
    private final int proxyPort;

    /**
     * Public constructor.
     * @param proxyHost Proxy host.
     * @param proxyPort Proxy port.
     * @param proxyScheme Proxy http scheme.
     */
    public Proxy(final String proxyHost, final int proxyPort, String proxyScheme) {
        this.proxyScheme = proxyScheme;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    /**
     * A constructor using the default proxy scheme.
     * @param proxyHost Proxy host.
     * @param proxyPort Proxy port.
     */
    public Proxy(final String proxyHost, final int proxyPort) {
        this(proxyHost, proxyPort, DEFAULT_PROXY_SCHEME);
    }

    public String getProxyScheme() {
        return proxyScheme;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String toString() {
        return new ToStringBuilder(Proxy.class.getName(), ToStringStyle.JSON_STYLE)
                .append("proxyScheme", proxyScheme)
                .append("proxyHost", proxyHost)
                .append("proxyPort", proxyPort)
                .toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((proxyHost == null) ? 0 : proxyHost.hashCode());
        result = prime * result + proxyPort;
        result = prime * result + ((proxyScheme == null) ? 0 : proxyScheme.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Proxy other = (Proxy) obj;
        if (proxyHost == null) {
            if (other.proxyHost != null) return false;
        } else if (!proxyHost.equals(other.proxyHost)) return false;
        if (proxyPort != other.proxyPort) return false;
        if (proxyScheme == null) {
            if (other.proxyScheme != null) return false;
        } else if (!proxyScheme.equals(other.proxyScheme)) return false;
        return true;
    }
}