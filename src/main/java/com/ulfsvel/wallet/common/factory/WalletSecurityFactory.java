package com.ulfsvel.wallet.common.factory;

import com.ulfsvel.wallet.common.enums.WalletSecurityType;
import com.ulfsvel.wallet.common.service.AesBasicWalletSecurityService;
import com.ulfsvel.wallet.common.service.ShamirBasicWalletSecurityService;
import com.ulfsvel.wallet.common.service.WalletSecurityService;
import org.springframework.stereotype.Component;

@Component
public class WalletSecurityFactory {

    private final ShamirBasicWalletSecurityService shamirBasicWalletSecurityService;

    private final AesBasicWalletSecurityService aesBasicWalletSecurityService;

    public WalletSecurityFactory(ShamirBasicWalletSecurityService shamirBasicWalletSecurityService, AesBasicWalletSecurityService aesBasicWalletSecurityService) {
        this.shamirBasicWalletSecurityService = shamirBasicWalletSecurityService;
        this.aesBasicWalletSecurityService = aesBasicWalletSecurityService;
    }

    public WalletSecurityService getWalletSecurityService(WalletSecurityType walletSecurityType) {
        switch (walletSecurityType) {
            case ShamirBasic:
                return shamirBasicWalletSecurityService;
            case AesBasic:
                return aesBasicWalletSecurityService;
        }
        throw new RuntimeException("Wallet security type does not exist");
    }
}
