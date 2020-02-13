package com.ulfsvel.wallet.common.entiry;

import java.util.Map;

public class WalletCredentials {

    private Map<String, ? super Object> credentials;

    public Map<String, ? super Object> getCredentials() {
        return credentials;
    }

    public WalletCredentials setCredentials(Map<String, ? super Object> credentials) {
        this.credentials = credentials;
        return this;
    }
}
