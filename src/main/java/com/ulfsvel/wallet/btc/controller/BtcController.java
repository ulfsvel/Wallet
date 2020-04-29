package com.ulfsvel.wallet.btc.controller;

import com.ulfsvel.wallet.btc.service.BtcWalletService;
import com.ulfsvel.wallet.common.entity.User;
import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.WalletCredentials;
import com.ulfsvel.wallet.common.repository.UserRepository;
import com.ulfsvel.wallet.common.repository.WalletRepository;
import com.ulfsvel.wallet.common.response.WalletBalanceResponse;
import com.ulfsvel.wallet.common.response.WalletSecurityResponse;
import com.ulfsvel.wallet.common.types.WalletSecurityType;
import com.ulfsvel.wallet.common.types.WalletType;
import com.ulfsvel.wallet.eth.request.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/btc/")
public class BtcController {

    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    private final BtcWalletService btcWalletService;

    public BtcController(WalletRepository walletRepository, UserRepository userRepository, BtcWalletService btcWalletService) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.btcWalletService = btcWalletService;
    }

    @PostMapping("createWallet")
    public WalletSecurityResponse createWallet(
            @RequestBody CreateWalletRequest createWalletRequest,
            Principal principal
    ) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, ValidationException {
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());
        if (!optionalUser.isPresent()) {
            throw new ValidationException("User does not exist.");
        }

        WalletSecurityResponse walletSecurityResponse = btcWalletService.createWallet(
                new WalletCredentials(createWalletRequest.getCredentials()),
                WalletSecurityType.valueOf(createWalletRequest.getSecurityType())
        );
        User currentUser = optionalUser.get();
        Wallet currentWallet = walletSecurityResponse.getWallet();
        currentUser.addWallet(currentWallet);
        currentWallet.setUser(currentUser);
        walletRepository.save(walletSecurityResponse.getWallet());
        userRepository.save(optionalUser.get());

        return walletSecurityResponse;
    }

    @PostMapping("updateWalletSecurity")
    public WalletSecurityResponse updateWalletSecurity(
            @RequestBody UpdateWalletSecurityRequest updateWalletSecurityRequest,
            Principal principal
    ) {
        Optional<Wallet> walletOptional = walletRepository.findWalletByPublicAddressAndUserEmail(
                updateWalletSecurityRequest.getPublicAddress(),
                principal.getName()
        );
        if (!walletOptional.isPresent()) {
            throw new RuntimeException("No walled identified by \"" + updateWalletSecurityRequest.getPublicAddress() + "\" can be found");
        }
        return btcWalletService.changeWalletSecurityType(
                walletOptional.get(),
                new WalletCredentials(updateWalletSecurityRequest.getCurrentCredentials()),
                new WalletCredentials(updateWalletSecurityRequest.getNewCredentials()),
                WalletSecurityType.valueOf(updateWalletSecurityRequest.getNewSecurityType())
        );
    }

    @PostMapping("recoverWallet")
    public WalletSecurityResponse recoverWallet(
            @RequestBody RecoverWalletRequest recoverWalletRequest,
            Principal principal
    ) {
        Optional<Wallet> walletOptional = walletRepository.findWalletByPublicAddressAndUserEmail(
                recoverWalletRequest.getPublicAddress(),
                principal.getName()
        );
        if (!walletOptional.isPresent()) {
            throw new RuntimeException("No walled identified by \"" + recoverWalletRequest.getPublicAddress() + "\" can be found");
        }

        return btcWalletService.recoverAndEncryptWallet(
                walletOptional.get(),
                new WalletCredentials(recoverWalletRequest.getRecoverCredentials()),
                new WalletCredentials(recoverWalletRequest.getNewCredentials()),
                WalletSecurityType.valueOf(recoverWalletRequest.getNewSecurityType())
        );
    }

    @PostMapping("getWalletBalance")
    public WalletBalanceResponse getAccountBalance(
            @RequestBody GetBalanceRequest getBalanceRequest,
            Principal principal
    ) throws IOException {
        Optional<Wallet> walletOptional = walletRepository.findWalletByPublicAddressAndUserEmail(
                getBalanceRequest.getPublicAddress(),
                principal.getName()
        );
        if (!walletOptional.isPresent()) {
            throw new RuntimeException("No walled identified by \"" + getBalanceRequest.getPublicAddress() + "\" can be found");
        }
        Wallet wallet = walletOptional.get();
        String balance = btcWalletService.getBalance(wallet);
        wallet.setLastKnownBalance(balance);
        walletRepository.save(wallet);

        return new WalletBalanceResponse(balance);
    }

    @PostMapping("transferFounds")
    public String transferBalance(
            @RequestBody TransferFoundsRequest transferFoundsRequest,
            Principal principal
    ) throws Exception {
        Optional<Wallet> walletOptional = walletRepository.findWalletByPublicAddressAndUserEmail(
                transferFoundsRequest.getPublicAddress(),
                principal.getName()
        );
        if (!walletOptional.isPresent()) {
            throw new RuntimeException("No walled identified by \"" + transferFoundsRequest.getPublicAddress() + "\" can be found");
        }
        return btcWalletService.unlockAndTransferFounds(
                walletOptional.get(),
                new WalletCredentials(transferFoundsRequest.getCredentials()),
                transferFoundsRequest.getTo(),
                Double.parseDouble(transferFoundsRequest.getAmount())
        );
    }

//    @PostMapping("getWalletTransactions")
//    public List<EthTransaction> getTransaction(
//            @RequestBody GetTransactionsRequest getTransactionsRequest,
//            Principal principal
//    ) {
//        return ethTransactionRepository.findAllByWalletPublicAddressAndWalletUserEmail(
//                getTransactionsRequest.getPublicAddress(),
//                principal.getName()
//        );
//
//        return new LinkedList<>();
//    }

    @PostMapping("getWallets")
    public List<Wallet> getWallets(Principal principal) {
        return walletRepository.findAllByWalletTypeAndUserEmail(WalletType.BTC, principal.getName());
    }
}
