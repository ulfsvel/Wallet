package com.ulfsvel.wallet.eth.controller;

import com.ulfsvel.wallet.common.entiry.Wallet;
import com.ulfsvel.wallet.common.repository.WalletRepository;
import com.ulfsvel.wallet.eth.request.*;
import com.ulfsvel.wallet.eth.service.EthWalletService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;


@RestController
@RequestMapping("api/eth/")
public class EthController {

    private final WalletRepository walletRepository;

    private final EthWalletService ethWalletService;


    public EthController(WalletRepository walletRepository, EthWalletService ethWalletService) {
        this.walletRepository = walletRepository;
        this.ethWalletService = ethWalletService;
    }

    @PostMapping("createWallet")
    public Wallet createWallet(@RequestBody CreateWalletRequest createWalletRequest) {
        return null;
    }

    @PostMapping("decryptPrivateKey")
    public BigInteger unlockWallet(@RequestBody DecryptPrivateKeyRequest decryptPrivateKeyRequest) {
        return null;
    }

    @PostMapping("recoverPrivateKey")
    public BigInteger recoverWallet(@RequestBody RecoverPrivateKeyRequest recoverPrivateKeyRequest) {
        return null;
    }

    @PostMapping("getAccountBalance")
    public BigInteger getAccountBalance(@RequestBody GetBalanceRequest getBalanceRequest) {
        return null;
    }

    @PostMapping("transferFounds")
    public Transaction transferBalance(@RequestBody TransferFoundsRequest transferFoundsRequest) {
        return null;
    }
}
