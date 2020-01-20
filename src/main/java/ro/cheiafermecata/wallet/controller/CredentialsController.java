package ro.cheiafermecata.wallet.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import ro.cheiafermecata.wallet.response.WalletCreatedResponse;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
@RequestMapping("api/wallet")
public class CredentialsController {

    @PostMapping("/create")
    public WalletCreatedResponse createWaller() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Credentials credentials = Credentials.create(Keys.createEcKeyPair());
        ECKeyPair keyPair = credentials.getEcKeyPair();
        return new WalletCreatedResponse()
                .setAddress(credentials.getAddress())
                .setPublicKey(keyPair.getPublicKey())
                .setPrivateKey(keyPair.getPrivateKey());
    }

}
