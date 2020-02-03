package ro.cheiafermecata.wallet.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.cheiafermecata.wallet.entity.Wallet;
import ro.cheiafermecata.wallet.repository.WalletRepository;
import ro.cheiafermecata.wallet.request.WalletCreationRequest;
import ro.cheiafermecata.wallet.request.WalletRecoverRequest;
import ro.cheiafermecata.wallet.request.WalletUnlockRequest;
import ro.cheiafermecata.wallet.service.WalletService;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;

@RestController
@RequestMapping("api/wallet")
public class CredentialsController {

    private final WalletRepository walletRepository;

    private final WalletService walletService;

    public CredentialsController(WalletRepository walletRepository, WalletService walletService) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public Wallet createWallet(@RequestBody WalletCreationRequest walletCreationRequest) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        return walletService.createWallet(walletCreationRequest.getPassword());
    }

    @PostMapping("/unlock")
    public BigInteger unlockWallet(@RequestBody WalletUnlockRequest walletUnlockRequest) {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletUnlockRequest.getWalletId());
        if (!optionalWallet.isPresent()) {
            throw new RuntimeException("Wallet does not exist");
        }
        Wallet wallet = optionalWallet.get();
        return walletService.getPrivateKeyForWallet(wallet, walletUnlockRequest.getPassword());
    }

    @PostMapping("/recover")
    public BigInteger recoverWallet(@RequestBody WalletRecoverRequest walletRecoverRequest) {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletRecoverRequest.getWalletId());
        if (!optionalWallet.isPresent()) {
            throw new RuntimeException("Wallet does not exist");
        }
        Wallet wallet = optionalWallet.get();
        return walletService.recoverPrivateKeyForWallet(wallet);
    }
}
