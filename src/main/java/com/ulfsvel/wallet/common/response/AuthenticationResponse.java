package com.ulfsvel.wallet.common.response;

public class AuthenticationResponse {

    private String accessToken;

    private Long expiresIn;

    public AuthenticationResponse(String accessToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
