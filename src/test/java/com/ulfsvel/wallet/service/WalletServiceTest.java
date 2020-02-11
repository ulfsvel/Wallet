package com.ulfsvel.wallet.service;

import com.ulfsvel.shamir.SecretGroup;
import com.ulfsvel.shamir.SecretsFactory;
import com.ulfsvel.shamir.Share;
import com.ulfsvel.wallet.entity.Wallet;
import com.ulfsvel.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import com.ulfsvel.wallet.service.SecureSecretStorage.SecureSecretStorage;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WalletServiceTest {

    private static final String password = "test";

    @Test
    public void testPrivateKeyDecryptionWithUserPassword() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        WalletService walletService = getWalletService();
        Wallet resultedWallet = walletService.createWallet(password);

        BigInteger privateKey = walletService.getPrivateKeyForWallet(resultedWallet, password);
        assertNotNull(privateKey);
    }

    private WalletService getWalletService() {
        return new WalletService(
                getWalletRepository(),
                getSecretsFactory(),
                getSecureSecretStorage()
        );
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
