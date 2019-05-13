package com.sap.cloud.rest.api.client.auth.oauth;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class AccessTokenResponseDto {

    private static final String ACCESS_TOKEN_JSON_PROPERTY = "access_token";

    private final String accessToken;

    @JsonCreator
    public AccessTokenResponseDto(
            @JsonProperty(value = ACCESS_TOKEN_JSON_PROPERTY, required = true) final String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty(value = ACCESS_TOKEN_JSON_PROPERTY)
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessTokenResponseDto jwtToken = (AccessTokenResponseDto) o;
        return Objects.equals(accessToken, jwtToken.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken);
    }

}
