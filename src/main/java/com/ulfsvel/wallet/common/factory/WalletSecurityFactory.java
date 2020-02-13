package com.ulfsvel.wallet.common.factory;

import com.ulfsvel.wallet.common.entiry.Wallet;
import com.ulfsvel.wallet.common.service.ShamirBasicSecurityWalletSecurityService;
import com.ulfsvel.wallet.common.service.WalletSecurityService;
import org.springframework.stereotype.Component;

@Component
public class WalletSecurityFactory {

    private final ShamirBasicSecurityWalletSecurityService shamirBasicSecurityWalletSecurityService;

    public WalletSecurityFactory(ShamirBasicSecurityWalletSecurityService shamirBasicSecurityWalletSecurityService) {
        this.shamirBasicSecurityWalletSecurityService = shamirBasicSecurityWalletSecurityService;
    }

    public WalletSecurityService getWalletSecurityService(int walletSecurityType) {
        if (walletSecurityType == Wallet.SHAMIR_BASIC_SECURITY) {
            return shamirBasicSecurityWalletSecurityService;
        }
        throw new RuntimeException("Wallet security type does not exist");
    }
}
