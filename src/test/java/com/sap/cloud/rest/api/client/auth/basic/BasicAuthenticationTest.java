package com.sap.cloud.rest.api.client.auth.basic;

import org.junit.Test;

import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.auth.basic.BasicAuthentication;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BasicAuthenticationTest {

    private static final String USERNAME = "username";
    private static final char[] PASSWORD = "password".toCharArray();

    @Test
    public void validBasicAuthenticationTest() {
        BasicAuthentication authentication = new BasicAuthentication(USERNAME, PASSWORD);

        assertEquals(USERNAME, authentication.getUsername());
        assertArrayEquals(PASSWORD, authentication.getPassword());
        assertEquals(AuthenticationType.BASIC, authentication.getAuthenticationType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUsernameBasicAuthenticationTest() {
        new BasicAuthentication(null, PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullPasswordBasicAuthenticationTest() {
        new BasicAuthentication(USERNAME, null);
    }
}

