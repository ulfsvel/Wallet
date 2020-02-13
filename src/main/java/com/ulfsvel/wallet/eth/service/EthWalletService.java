package com.ulfsvel.wallet.eth.service;

import com.ulfsvel.wallet.common.entiry.UnencryptedWallet;
import com.ulfsvel.wallet.common.entiry.Wallet;
import com.ulfsvel.wallet.common.entiry.WalletCredentials;
import com.ulfsvel.wallet.common.factory.WalletSecurityFactory;
import com.ulfsvel.wallet.common.service.WalletSecurityService;
import com.ulfsvel.wallet.eth.entity.EthTransaction;
import com.ulfsvel.wallet.eth.repository.EthTransactionRepository;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Component
public class EthWalletService {

    private final WalletSecurityFactory walletSecurityFactory;

    private final EthTransactionRepository ethTransactionRepository;

    private final Web3j web3j;

    public EthWalletService(WalletSecurityFactory walletSecurityFactory, EthTransactionRepository ethTransactionRepository, Web3j web3j) {
        this.walletSecurityFactory = walletSecurityFactory;
        this.ethTransactionRepository = ethTransactionRepository;
        this.web3j = web3j;
    }

    private UnencryptedWallet generateWallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Credentials credentials = Credentials.create(Keys.createEcKeyPair());
        ECKeyPair keyPair = credentials.getEcKeyPair();

        return new UnencryptedWallet()
                .setPrivateKey(keyPair.getPrivateKey().toString())
                .setPublicKey(keyPair.getPublicKey().toString())
                .setPublicAddress(credentials.getAddress())
                .setWalletType(Wallet.ETH_WALLET);
    }

    public Wallet createWallet(WalletCredentials walletCredentials, int walletSecurityType) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        return encryptWallet(generateWallet(), walletCredentials, walletSecurityType);
    }

    private Wallet encryptWallet(
            UnencryptedWallet unencryptedWallet,
            WalletCredentials walletCredentials,
            int walletSecurityType
    ) {
        WalletSecurityService walletSecurityService = walletSecurityFactory.getWalletSecurityService(walletSecurityType);
        if (walletSecurityService.areEncryptCredentialsValid(walletCredentials)) {
            return walletSecurityService.encryptWallet(unencryptedWallet, walletCredentials);
        }

        throw new RuntimeException("Error encrypting wallet");
    }

    private UnencryptedWallet decryptWallet(
            Wallet wallet,
            WalletCredentials walletCredentials
    ) {
        WalletSecurityService walletSecurityService = walletSecurityFactory.getWalletSecurityService(wallet.getWalletSecurityType());
        if (walletSecurityService.areDecryptCredentialsValid(walletCredentials)) {
            return walletSecurityService.decryptWallet(wallet, walletCredentials);
        }

        throw new RuntimeException("Error decrypting wallet");
    }

    private UnencryptedWallet recoverWallet(
            Wallet wallet,
            WalletCredentials walletCredentials
    ) {
        WalletSecurityService walletSecurityService = walletSecurityFactory.getWalletSecurityService(wallet.getWalletSecurityType());
        if (walletSecurityService.areRecoverCredentialsValid(walletCredentials)) {
            return walletSecurityService.recoverWallet(wallet, walletCredentials);
        }

        throw new RuntimeException("Error recovering wallet");
    }

    public Wallet changeWalletSecurityType(
            Wallet wallet,
            WalletCredentials initialWalletCredentials,
            WalletCredentials targetCredentials,
            int targetWalletSecurityType
    ) {
        UnencryptedWallet unencryptedWallet = decryptWallet(wallet, initialWalletCredentials);
        return encryptWallet(unencryptedWallet, targetCredentials, targetWalletSecurityType);
    }

    public Wallet recoverAndEncryptWallet(
            Wallet wallet,
            WalletCredentials initialWalletCredentials,
            WalletCredentials targetCredentials,
            int targetWalletSecurityType
    ) {
        UnencryptedWallet unencryptedWallet = recoverWallet(wallet, initialWalletCredentials);
        return encryptWallet(unencryptedWallet, targetCredentials, targetWalletSecurityType);
    }

    public EthTransaction unlockAndTransferFounds(Wallet wallet, WalletCredentials walletCredentials, String to, BigDecimal amount) throws Exception {
        UnencryptedWallet unencryptedWallet = decryptWallet(wallet, walletCredentials);
        Credentials credentials = Credentials.create(
                new BigInteger(unencryptedWallet.getPrivateKey()).toString(16),
                new BigInteger(wallet.getPublicKey()).toString(16)
        );

        return transferFounds(wallet, credentials, to, amount);
    }

    public BigInteger getBalance(Wallet wallet) throws IOException {
        EthGetBalance ethGetBalance = web3j
                .ethGetBalance(wallet.getPublicAddress(), DefaultBlockParameterName.LATEST)
                .send();

        return ethGetBalance.getBalance();
    }

    private EthTransaction transferFounds(Wallet wallet, Credentials credentials, String to, BigDecimal amount) throws Exception {
        TransactionReceipt transactionReceipt = Transfer.sendFunds(web3j, credentials, to, amount, Convert.Unit.WEI).send();
        if (!transactionReceipt.isStatusOK()) {
            throw new RuntimeException("Transaction failed");
        }

        Transaction transaction = web3j.ethGetTransactionByHash(transactionReceipt.getTransactionHash()).send().getResult();
        EthTransaction ethTransaction = EthTransaction.create(transaction);
        ethTransaction.setWallet(wallet);

        return ethTransactionRepository.save(ethTransaction);
    }


}