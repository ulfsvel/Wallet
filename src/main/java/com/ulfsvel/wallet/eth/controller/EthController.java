package com.ulfsvel.wallet.eth.controller;

import com.ulfsvel.wallet.common.entiry.Wallet;
import com.ulfsvel.wallet.common.repository.WalletRepository;
import com.ulfsvel.wallet.common.service.ShamirBasicSecurityWalletService;
import com.ulfsvel.wallet.eth.request.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;

@RestController
@RequestMapping("api/eth/")
public class EthController {

    private final WalletRepository walletRepository;

    private final ShamirBasicSecurityWalletService shamirBasicSecurityWalletService;

    private final Web3j web3j;

    public EthController(WalletRepository walletRepository, ShamirBasicSecurityWalletService shamirBasicSecurityWalletService, Web3j web3j) {
        this.walletRepository = walletRepository;
        this.shamirBasicSecurityWalletService = shamirBasicSecurityWalletService;
        this.web3j = web3j;
    }

    @PostMapping("createWallet")
    public Wallet createWallet(@RequestBody CreateWalletRequest createWalletRequest) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        return shamirBasicSecurityWalletService.createWallet(createWalletRequest.getPassword());
    }

    @PostMapping("decryptPrivateKey")
    public BigInteger unlockWallet(@RequestBody DecryptPrivateKeyRequest decryptPrivateKeyRequest) {
        Optional<Wallet> optionalWallet = walletRepository.findById(decryptPrivateKeyRequest.getWalletId());
        if (!optionalWallet.isPresent()) {
            throw new RuntimeException("Wallet does not exist");
        }
        Wallet wallet = optionalWallet.get();
        return shamirBasicSecurityWalletService.getPrivateKeyForWallet(wallet, decryptPrivateKeyRequest.getPassword());
    }

    @PostMapping("recoverPrivateKey")
    public BigInteger recoverWallet(@RequestBody RecoverPrivateKeyRequest recoverPrivateKeyRequest) {
        Optional<Wallet> optionalWallet = walletRepository.findById(recoverPrivateKeyRequest.getWalletId());
        if (!optionalWallet.isPresent()) {
            throw new RuntimeException("Wallet does not exist");
        }
        Wallet wallet = optionalWallet.get();
        return shamirBasicSecurityWalletService.recoverPrivateKeyForWallet(wallet);
    }

    @PostMapping("getAccountBalance")
    public BigInteger getAccountBalance(@RequestBody GetBalanceRequest getBalanceRequest) throws IOException {
        EthGetBalance ethGetBalance = web3j
                .ethGetBalance(getBalanceRequest.getPublicAddress(), DefaultBlockParameterName.LATEST)
                .send();

        return ethGetBalance.getBalance();
    }

    @PostMapping("transferFounds")
    public TransactionReceipt transferBalance(@RequestBody TransferFoundsRequest transferFoundsRequest) throws Exception {
        Optional<Wallet> optionalWallet = walletRepository.findById(transferFoundsRequest.getWalletId());
        if (!optionalWallet.isPresent()) {
            throw new RuntimeException("Wallet does not exist");
        }
        Wallet wallet = optionalWallet.get();
        BigInteger decryptedPrivateKey = shamirBasicSecurityWalletService.getPrivateKeyForWallet(wallet, transferFoundsRequest.getPassword());


        Credentials credentials = Credentials.create(decryptedPrivateKey.toString(16), new BigInteger(wallet.getPublicKey()).toString(16));
        return Transfer.sendFunds(web3j, credentials, transferFoundsRequest.getTo(), new BigDecimal(transferFoundsRequest.getAmount()), Convert.Unit.WEI).send();
    }
}
