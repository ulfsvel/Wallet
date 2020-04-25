package com.ulfsvel.wallet.common.factory;

import com.ulfsvel.wallet.common.service.*;
import com.ulfsvel.wallet.common.types.WalletSecurityType;
import org.springframework.stereotype.Component;

@Component
public class WalletSecurityFactory {

    private final ShamirBasicWalletSecurityService shamirBasicWalletSecurityService;

    private final AesBasicWalletSecurityService aesBasicWalletSecurityService;

    private final PaperWalletSecurityService paperWalletSecurityService;

    private final ShamirAdvancedWalletSecurityService shamirAdvancedWalletSecurityService;

    public WalletSecurityFactory(ShamirBasicWalletSecurityService shamirBasicWalletSecurityService, AesBasicWalletSecurityService aesBasicWalletSecurityService, PaperWalletSecurityService paperWalletSecurityService, ShamirAdvancedWalletSecurityService shamirAdvancedWalletSecurityService) {
        this.shamirBasicWalletSecurityService = shamirBasicWalletSecurityService;
        this.aesBasicWalletSecurityService = aesBasicWalletSecurityService;
        this.paperWalletSecurityService = paperWalletSecurityService;
        this.shamirAdvancedWalletSecurityService = shamirAdvancedWalletSecurityService;
    }

    public WalletSecurityService getWalletSecurityService(WalletSecurityType walletSecurityType) {
        switch (walletSecurityType) {
            case ShamirBasic:
                return shamirBasicWalletSecurityService;
            case AesBasic:
                return aesBasicWalletSecurityService;
            case Paper:
                return paperWalletSecurityService;
            case ShamirAdvanced:
                return shamirAdvancedWalletSecurityService;
        }
        throw new RuntimeException("Wallet security type does not exist");
    }
}
