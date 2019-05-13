package com.sap.cloud.rest.api.client.http;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.cloud.rest.api.client.http.Proxy;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ProxyTest {

    private static final String DEFAUL_PROXY_SCHEME = "http";
    private static final String PROXY_SCHEME = "https";
    private static final String PROXY_HOST = "proxy.int";
    private static final int PROXY_PORT = 8888;

    @Test
    public void createProxyConfigurationTest() {
        Proxy proxyConfiguration = new Proxy(PROXY_HOST, PROXY_PORT, PROXY_SCHEME);

        assertEquals(PROXY_HOST, proxyConfiguration.getProxyHost());
        assertEquals(PROXY_PORT, proxyConfiguration.getProxyPort());
        assertEquals(PROXY_SCHEME, proxyConfiguration.getProxyScheme());
    }

    @Test
    public void createProxyConfigurationWithDefaultSchemeTest() {
        Proxy proxyConfiguration = new Proxy(PROXY_HOST, PROXY_PORT);

        assertEquals(PROXY_HOST, proxyConfiguration.getProxyHost());
        assertEquals(PROXY_PORT, proxyConfiguration.getProxyPort());
        assertEquals(DEFAUL_PROXY_SCHEME, proxyConfiguration.getProxyScheme());
    }

    @Test
    public void toStringTest() {
        Proxy proxyConfiguration = new Proxy(PROXY_HOST, PROXY_PORT, PROXY_SCHEME);

        String expectedString = "{\"proxyScheme\":\"" + PROXY_SCHEME
                + "\",\"proxyHost\":\"" + PROXY_HOST
                + "\",\"proxyPort\":" + PROXY_PORT + "}";
        assertEquals(expectedString, proxyConfiguration.toString());
    }

    @Test
    public void equalsTest() {
        EqualsVerifier.forClass(Proxy.class).usingGetClass().verify();
    }
}
