package com.ulfsvel.wallet.eth.request;

import com.ulfsvel.wallet.common.entity.WalletCredentials;

import java.util.Map;

public class RecoverWalletRequest {

    private String publicAddress;

    private Map<String, Object> recoverCredentials;

    private Map<String, Object> newCredentials;

    private String newSecurityType;

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public Map<String, Object> getRecoverCredentials() {
        return recoverCredentials;
    }

    public void setRecoverCredentials(Map<String, Object> recoverCredentials) {
        this.recoverCredentials = recoverCredentials;
    }

    public Map<String, Object> getNewCredentials() {
        return newCredentials;
    }

    public void setNewCredentials(WalletCredentials newCredentials) {
        this.newCredentials = newCredentials;
    }

    public String getNewSecurityType() {
        return newSecurityType;
    }

    public void setNewSecurityType(String newSecurityType) {
        this.newSecurityType = newSecurityType;
    }
}
