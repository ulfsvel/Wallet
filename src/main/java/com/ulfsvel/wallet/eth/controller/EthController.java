package com.ulfsvel.wallet.eth.controller;

import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.WalletCredentials;
import com.ulfsvel.wallet.common.enums.WalletSecurityType;
import com.ulfsvel.wallet.common.repository.WalletRepository;
import com.ulfsvel.wallet.eth.entity.EthTransaction;
import com.ulfsvel.wallet.eth.repository.EthTransactionRepository;
import com.ulfsvel.wallet.eth.request.*;
import com.ulfsvel.wallet.eth.service.EthWalletService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/eth/")
public class EthController {

    private final WalletRepository walletRepository;

    private final EthWalletService ethWalletService;

    private final EthTransactionRepository ethTransactionRepository;

    public EthController(WalletRepository walletRepository, EthWalletService ethWalletService, EthTransactionRepository ethTransactionRepository) {
        this.walletRepository = walletRepository;
        this.ethWalletService = ethWalletService;
        this.ethTransactionRepository = ethTransactionRepository;
    }

    @PostMapping("createWallet")
    public Wallet createWallet(@RequestBody CreateWalletRequest createWalletRequest) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        return ethWalletService.createWallet(
                new WalletCredentials(createWalletRequest.getCredentials()),
                WalletSecurityType.valueOf(createWalletRequest.getSecurityType())
        );
    }

    @PostMapping("updateWalletSecurity")
    public Wallet unlockWallet(@RequestBody UpdateWalletSecurityRequest updateWalletSecurityRequest) {
        Optional<Wallet> walletOptional = walletRepository.findWalletByPublicAddress(updateWalletSecurityRequest.getPublicAddress());
        if (!walletOptional.isPresent()) {
            throw new RuntimeException("No walled identified by \"" + updateWalletSecurityRequest.getPublicAddress() + "\" can be found");
        }
        return ethWalletService.changeWalletSecurityType(
                walletOptional.get(),
                new WalletCredentials(updateWalletSecurityRequest.getCurrentCredentials()),
                new WalletCredentials(updateWalletSecurityRequest.getNewCredentials()),
                WalletSecurityType.valueOf(updateWalletSecurityRequest.getNewSecurityType())
        );
    }

    @PostMapping("recoverWallet")
    public Wallet recoverWallet(@RequestBody RecoverWalletRequest recoverWalletRequest) {
        Optional<Wallet> walletOptional = walletRepository.findWalletByPublicAddress(recoverWalletRequest.getPublicAddress());
        if (!walletOptional.isPresent()) {
            throw new RuntimeException("No walled identified by \"" + recoverWalletRequest.getPublicAddress() + "\" can be found");
        }

        return ethWalletService.recoverAndEncryptWallet(
                walletOptional.get(),
                new WalletCredentials(recoverWalletRequest.getRecoverCredentials()),
                new WalletCredentials(recoverWalletRequest.getNewCredentials()),
                WalletSecurityType.valueOf(recoverWalletRequest.getNewSecurityType())
        );
    }

    @PostMapping("getWalletBalance")
    public BigInteger getAccountBalance(@RequestBody GetBalanceRequest getBalanceRequest) throws IOException {
        Optional<Wallet> walletOptional = walletRepository.findWalletByPublicAddress(getBalanceRequest.getPublicAddress());
        if (!walletOptional.isPresent()) {
            throw new RuntimeException("No walled identified by \"" + getBalanceRequest.getPublicAddress() + "\" can be found");
        }
        return ethWalletService.getBalance(walletOptional.get());
    }

    @PostMapping("transferFounds")
    public EthTransaction transferBalance(@RequestBody TransferFoundsRequest transferFoundsRequest) throws Exception {
        Optional<Wallet> walletOptional = walletRepository.findWalletByPublicAddress(transferFoundsRequest.getPublicAddress());
        if (!walletOptional.isPresent()) {
            throw new RuntimeException("No walled identified by \"" + transferFoundsRequest.getPublicAddress() + "\" can be found");
        }
        return ethWalletService.unlockAndTransferFounds(
                walletOptional.get(),
                new WalletCredentials(transferFoundsRequest.getCredentials()),
                transferFoundsRequest.getTo(),
                new BigDecimal(transferFoundsRequest.getAmount())
        );
    }

    @PostMapping("getWalletTransactions")
    public List<EthTransaction> getTransaction(@RequestBody GetTransactionsRequest getTransactionsRequest) {
        return ethTransactionRepository.findAllByWalletPublicAddress(
                getTransactionsRequest.getPublicAddress()
        );
    }
}
