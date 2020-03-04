package com.ulfsvel.wallet.eth.request;


import java.util.Map;

public class CreateWalletRequest {

    private Map<String, Object> credentials;
    private String securityType;

    public Map<String, Object> getCredentials() {
        return credentials;
    }

    public void setCredentials(Map<String, Object> credentials) {
        this.credentials = credentials;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }
}
