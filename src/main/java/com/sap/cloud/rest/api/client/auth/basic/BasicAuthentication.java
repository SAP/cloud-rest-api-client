package com.sap.cloud.rest.api.client.auth.basic;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotEmptyOrNull;

import java.util.Arrays;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.AuthenticationType;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;;

/**
 * A class that represents basic authentication for {@link RestApiClientConfig}.
 */
public class BasicAuthentication implements Authentication {

    private static final String USERNAME_DISPLAY_NAME = "Username";
    private static final String PASSWORD_DISPLAY_NAME = "Password";

    private final String username;
    private final char[] password;

    public BasicAuthentication(String username, char[] password) {
        isNotEmptyOrNull(USERNAME_DISPLAY_NAME, username);
        isNotEmptyOrNull(PASSWORD_DISPLAY_NAME, password);

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return Arrays.copyOf(password, password.length);
    }

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.BASIC;
    }
}
