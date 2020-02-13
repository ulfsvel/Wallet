package com.ulfsvel.wallet.common.service;

import com.ulfsvel.shamir.SecretGroup;
import com.ulfsvel.shamir.SecretsFactory;
import com.ulfsvel.shamir.Share;
import com.ulfsvel.wallet.common.entiry.UnencryptedWallet;
import com.ulfsvel.wallet.common.entiry.Wallet;
import com.ulfsvel.wallet.common.entiry.security.ShamirBasicSecurity;
import com.ulfsvel.wallet.common.repository.WalletRepository;
import com.ulfsvel.wallet.common.repository.security.ShamirBasicSecurityRepository;
import com.ulfsvel.wallet.common.service.SecureSecretStorage.SecureSecretStorage;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShamirBasicSecurityEthWalletSecurityServiceTest {

    private static final String password = "test";

    private static final String privateKey = "privateKey";

    @Test
    public void testPrivateKeyDecryptionWithUserPassword() {
        ShamirBasicSecurityWalletSecurityService shamirBasicSecurityWalletService = getWalletService();
        UnencryptedWallet unencryptedWallet = new UnencryptedWallet().setPrivateKey(privateKey);

        Wallet resultedWallet = shamirBasicSecurityWalletService.encryptWallet(unencryptedWallet, password);

        UnencryptedWallet recoveredWallet = shamirBasicSecurityWalletService.decryptWallet(resultedWallet, password);
        assertEquals(unencryptedWallet.getPrivateKey(), recoveredWallet.getPrivateKey());
    }

    private ShamirBasicSecurityWalletSecurityService getWalletService() {
        return new ShamirBasicSecurityWalletSecurityService(
                getWalletRepository(),
                getShamirBasicSecurityRepository(),
                getSecretsFactory(),
                getSecureSecretStorage()
        );
    }

    private ShamirBasicSecurityRepository getShamirBasicSecurityRepository() {
        ShamirBasicSecurityRepository shamirBasicSecurityRepository = mock(ShamirBasicSecurityRepository.class);
        List<ShamirBasicSecurity> savedElements = new LinkedList<>();
        when(shamirBasicSecurityRepository.save(any())).thenAnswer(i -> {
            ShamirBasicSecurity shamirBasicSecurity = (ShamirBasicSecurity) i.getArguments()[0];
            savedElements.add(shamirBasicSecurity);

            return shamirBasicSecurity;
        });

        when(shamirBasicSecurityRepository.findShamirBasicSecurityByWallet(any())).thenAnswer(i -> {
            Wallet wallet = (Wallet) i.getArguments()[0];
            for (ShamirBasicSecurity element : savedElements) {
                if (element.getWallet().equals(wallet)) {
                    return Optional.of(element);
                }
            }
            return Optional.empty();
        });

        return shamirBasicSecurityRepository;
    }

    private WalletRepository getWalletRepository() {
        WalletRepository walletRepository = mock(WalletRepository.class);
        when(walletRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        return walletRepository;
    }

    private SecureSecretStorage getSecureSecretStorage() {
        return mock(SecureSecretStorage.class);
    }

    private SecretsFactory getSecretsFactory() {
        SecretsFactory secretsFactory = mock(SecretsFactory.class);

        List<Share> shareList = new LinkedList<>();
        shareList.add(new Share(new BigInteger("134294255902634115788063599554417876591"), new BigInteger("1")));
        shareList.add(new Share(new BigInteger("3117698273103263506000272481295076170"), new BigInteger("2")));
        shareList.add(new Share(new BigInteger("42082324104041642955624249124056381476"), new BigInteger("3")));

        when(secretsFactory.createSecret(2, 3)).thenReturn(
                new SecretGroup(2, shareList, new BigInteger("95329630071695736338439622911656571285"))
        );

        when(secretsFactory.rebuildSecret(any())).thenReturn(new BigInteger("95329630071695736338439622911656571285"));

        return secretsFactory;
    }

}
