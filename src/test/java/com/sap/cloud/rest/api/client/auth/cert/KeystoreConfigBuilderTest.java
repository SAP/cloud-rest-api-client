package com.sap.cloud.rest.api.client.auth.cert;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.security.KeyStore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfig;
import com.sap.cloud.rest.api.client.auth.cert.KeystoreConfigBuilder;

public class KeystoreConfigBuilderTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private static final KeyStore TEST_KEYSTORE = mock(KeyStore.class);
    private static final char[] TEST_PASSWORD = "example-password".toCharArray();
    private static final String TEST_ALIAS = "exampleAlias";

    @Test
    public void testWithValidCanBuild() {
        KeystoreConfig config = KeystoreConfigBuilder.getBuilder()
                .keystore(TEST_KEYSTORE)
                .keystorePassword(TEST_PASSWORD)
                .keyAlias(TEST_ALIAS)
                .build();

        assertEquals(config.getKeystore(), TEST_KEYSTORE);
        assertArrayEquals(config.getKeystorePassword(), TEST_PASSWORD);
        assertEquals(config.getKeyAlias(), TEST_ALIAS);
    }

    @Test
    public void testThrowsExceptionOnMissingKeystore() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Key store cannot be null.");

        KeystoreConfigBuilder.getBuilder()
                .keystorePassword(TEST_PASSWORD)
                .keyAlias(TEST_ALIAS)
                .build();
    }

    @Test
    public void testThrowsExceptionOnMissingPassword() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Key store password cannot be blank.");

        KeystoreConfigBuilder.getBuilder()
                .keystore(TEST_KEYSTORE)
                .keyAlias(TEST_ALIAS)
                .build();
    }

    @Test
    public void testThrowsExceptionOnMissingAlias() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Key store alias cannot be blank.");

        KeystoreConfigBuilder.getBuilder()
                .keystore(TEST_KEYSTORE)
                .keystorePassword(TEST_PASSWORD)
                .build();
    }

    @Test
    public void getKeystoreConfigBuilderTest() {
        assertTrue(KeystoreConfigBuilder.getBuilder() instanceof KeystoreConfigBuilder);
    }
}
