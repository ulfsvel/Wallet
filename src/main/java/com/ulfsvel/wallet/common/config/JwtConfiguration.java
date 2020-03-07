package com.ulfsvel.wallet.common.config;

public class JwtConfiguration {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_TYPE = "JWT";

    private final String authUrl;

    private final String issuer;

    private final String audience;

    private final String secret;

    private final Long expirationTime;

    private final String claimsName;

    private final String typeName;

    public JwtConfiguration(String authUrl, String issuer, String audience, String secret, Long expirationTime, String rolesName, String typeName) {
        this.authUrl = authUrl;
        this.issuer = issuer;
        this.audience = audience;
        this.secret = secret;
        this.expirationTime = expirationTime;
        this.claimsName = rolesName;
        this.typeName = typeName;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public String getClaimsName() {
        return claimsName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTokenHeader() {
        return TOKEN_HEADER;
    }

    public String getTokenPrefix() {
        return TOKEN_PREFIX;
    }

    public String getTokenType() {
        return TOKEN_TYPE;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAudience() {
        return audience;
    }

    public String getSecret() {
        return secret;
    }
}
