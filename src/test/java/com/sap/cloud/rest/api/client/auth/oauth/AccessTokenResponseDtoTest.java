package com.sap.cloud.rest.api.client.auth.oauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.rest.api.client.auth.oauth.AccessTokenResponseDto;

import nl.jqno.equalsverifier.EqualsVerifier;

public class AccessTokenResponseDtoTest {

    private static final String ACCESS_TOKEN = "dummyAccessToken";
    private static final String ACCESS_TOKEN_RESPONSE_JSON = "{\"access_token\":\"" + ACCESS_TOKEN + "\"}";
    private static final AccessTokenResponseDto ACCESS_TOKEN_RESPONSE_DTO = new AccessTokenResponseDto(ACCESS_TOKEN);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getAccessTokenTest() {
        assertEquals(ACCESS_TOKEN, ACCESS_TOKEN_RESPONSE_DTO.getAccessToken());
    }

    @Test
    public void serializeAccessTokenDtoTest() throws JsonProcessingException {
        String serialized = objectMapper.writeValueAsString(ACCESS_TOKEN_RESPONSE_DTO);
        assertEquals(ACCESS_TOKEN_RESPONSE_JSON, serialized);
    }

    @Test
    public void deserializeAccessTokenResponseDtoTest() throws Exception {
        AccessTokenResponseDto accessTokenResponse = objectMapper.readValue(ACCESS_TOKEN_RESPONSE_JSON,
                AccessTokenResponseDto.class);
        assertEquals(ACCESS_TOKEN, accessTokenResponse.getAccessToken());
    }

    @Test
    public void equalsTest() {
        EqualsVerifier.forClass(AccessTokenResponseDto.class).verify();
    }

}
