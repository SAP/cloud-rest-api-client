package com.sap.cloud.rest.api.client.auth.cert;

import java.security.KeyStore;

/**
 * A builder for @{link {@link KeystoreConfig}.
 */
public class KeystoreConfigBuilder {

    private KeyStore keystore;
    private char[] keystorePassword;
    private String keyAlias;

    /**
     * Attaches a key store to the builder.
     * @param keystore key store
     * @return Returns {@link KeystoreConfigBuilder}
     */  
    public KeystoreConfigBuilder keystore(KeyStore keystore) {
        this.keystore = keystore;
        return this;
    }

    /**
     * Attaches a key store password to the builder.
     * @param keystorePassword key store password
     * @return Returns {@link KeystoreConfigBuilder}
     */
    public KeystoreConfigBuilder keystorePassword(char[] keystorePassword) {
        this.keystorePassword = keystorePassword;
        return this;
    }

    /**
     * Attaches a key store alias to the builder.
     * @param keyAlias key store alias
     * @return Returns {@link KeystoreConfigBuilder}
     */
    public KeystoreConfigBuilder keyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
        return this;
    }

    /**
     * Builds a {@link KeystoreConfig} with the provided key store, password and
     * alias.
     * @return Returns {@link KeystoreConfig}
     */
    public KeystoreConfig build() {
        return new KeystoreConfig(keystore, keystorePassword, keyAlias);
    }

    /**
     * Returns an instance of {@link KeystoreConfigBuilder}.
     * @return Returns {@link KeystoreConfigBuilder}
     */
    public static KeystoreConfigBuilder getBuilder() {
        return new KeystoreConfigBuilder();
    }
}
