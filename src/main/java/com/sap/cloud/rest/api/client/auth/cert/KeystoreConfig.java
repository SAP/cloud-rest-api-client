package com.sap.cloud.rest.api.client.auth.cert;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotEmptyOrNull;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;

import java.security.KeyStore;
import java.util.Arrays;

/**
 * This class represents a configuration object used to configure the key store.
 *
 */
public class KeystoreConfig {

    private static final String KEY_ALIAS_DISPLAY_NAME = "Key store alias";
    private static final String KEYSTORE_PASSWORD_DISPLAY_NAME = "Key store password";
    private static final String KEYSTORE_DISPLAY_NAME = "Key store";

    private final KeyStore keystore;
    private final char[] keystorePassword;
    private final String keyAlias;
    
    /**
     * Creates new {@link KeystoreConfig} instance with specific password and alias.
     * @param keystore Keystore
     * @param keystorePassword Keystore password
     * @param keyAlias Keystore alias
     */
    public KeystoreConfig(KeyStore keystore, char[] keystorePassword, String keyAlias) {
        isNotNull(KEYSTORE_DISPLAY_NAME, keystore);
        isNotEmptyOrNull(KEYSTORE_PASSWORD_DISPLAY_NAME, keystorePassword);
        isNotEmptyOrNull(KEY_ALIAS_DISPLAY_NAME, keyAlias);

        this.keystore = keystore;
        this.keystorePassword = keystorePassword;
        this.keyAlias = keyAlias;
    }

    /**
     * @return Returns the keystore.
     */
    public KeyStore getKeystore() {
        return keystore;
    }

    /**
     * @return Returns the keystore password.
     */
    public char[] getKeystorePassword() {
        return Arrays.copyOf(keystorePassword, keystorePassword.length);
    }

    /**
     * @return Returns the keystore alias.
     */
    public String getKeyAlias() {
        return keyAlias;
    }
}
