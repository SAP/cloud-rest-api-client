package com.sap.cloud.rest.api.client.config;

import static com.sap.cloud.rest.api.client.config.RestApiClientConfig.AUTHENTICATION_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.config.RestApiClientConfig.HOST_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.config.RestApiClientConfig.ROUTE_PLANNER_DISPLAY_NAME;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.CANNOT_BE_BLANK_MSG;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.CANNOT_BE_NULL_MSG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.cloud.rest.api.client.auth.Authentication;
import com.sap.cloud.rest.api.client.auth.none.NoAuthentication;
import com.sap.cloud.rest.api.client.config.RestApiClientConfig;

public class RestApiClientConfigTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private static final String TEST_HOST = "https://example.com";
    private static final Authentication AUTHENTICATION = mock(Authentication.class);
    private static final HttpRoutePlanner ROUTE_PLANNER = mock(HttpRoutePlanner.class);

    @Test
    public void createRestApiClientConfigTest() {
        RestApiClientConfig config = new RestApiClientConfig(TEST_HOST, AUTHENTICATION, ROUTE_PLANNER);

        assertEquals(TEST_HOST, config.getHost());
        assertEquals(AUTHENTICATION, config.getAuthentication());
        assertEquals(ROUTE_PLANNER, config.getRoutePlanner());
    }

    @Test
    public void createRestApiClientConfigWithDefaultRoutePlannerTest() {
        RestApiClientConfig config = new RestApiClientConfig(TEST_HOST, AUTHENTICATION);

        assertEquals(TEST_HOST, config.getHost());
        assertEquals(AUTHENTICATION, config.getAuthentication());
        assertTrue(config.getRoutePlanner() instanceof DefaultRoutePlanner);
    }

    @Test
    public void createRestApiClientConfigWithDefaultAuthenticationTest() {
        RestApiClientConfig config = new RestApiClientConfig(TEST_HOST, ROUTE_PLANNER);

        assertEquals(TEST_HOST, config.getHost());
        assertTrue(config.getAuthentication() instanceof NoAuthentication);
        assertEquals(ROUTE_PLANNER, config.getRoutePlanner());
    }

    @Test
    public void createRestApiClientConfigWithDefaultAuthenticationAndRoutePlannerTest() {
        RestApiClientConfig config = new RestApiClientConfig(TEST_HOST);

        assertEquals(TEST_HOST, config.getHost());
        assertTrue(config.getAuthentication() instanceof NoAuthentication);
        assertTrue(config.getRoutePlanner() instanceof DefaultRoutePlanner);
    }

    @Test
    public void createRestApiClientConfigNullHostTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(HOST_DISPLAY_NAME + CANNOT_BE_BLANK_MSG);

        new RestApiClientConfig(null, AUTHENTICATION);
    }

    @Test
    public void createRestApiClientConfigEmptyHostTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(HOST_DISPLAY_NAME + CANNOT_BE_BLANK_MSG);

        new RestApiClientConfig(null, AUTHENTICATION);
    }

    @Test
    public void createRestApiClientConfigNullAuthenticationTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(AUTHENTICATION_DISPLAY_NAME + CANNOT_BE_NULL_MSG);

        Authentication authentication = null;
        new RestApiClientConfig(TEST_HOST, authentication);
    }

    @Test
    public void createRestApiClientConfigNullRoutePlannerTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(ROUTE_PLANNER_DISPLAY_NAME + CANNOT_BE_NULL_MSG);

        HttpRoutePlanner httpRoutePlanner = null;
        new RestApiClientConfig(TEST_HOST, httpRoutePlanner);
    }
}
