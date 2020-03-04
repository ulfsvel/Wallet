package com.ulfsvel.wallet.eth.request;


import java.util.Map;

public class UpdateWalletSecurityRequest {

    private String publicAddress;

    private Map<String, Object> currentCredentials;

    private Map<String, Object> newCredentials;

    private String newSecurityType;

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public Map<String, Object> getCurrentCredentials() {
        return currentCredentials;
    }

    public void setCurrentCredentials(Map<String, Object> currentCredentials) {
        this.currentCredentials = currentCredentials;
    }

    public Map<String, Object> getNewCredentials() {
        return newCredentials;
    }

    public void setNewCredentials(Map<String, Object> newCredentials) {
        this.newCredentials = newCredentials;
    }

    public String getNewSecurityType() {
        return newSecurityType;
    }

    public void setNewSecurityType(String newSecurityType) {
        this.newSecurityType = newSecurityType;
    }
}
