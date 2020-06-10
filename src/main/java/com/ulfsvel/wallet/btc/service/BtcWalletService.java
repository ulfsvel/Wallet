package com.ulfsvel.wallet.btc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ulfsvel.wallet.btc.config.BitcoinSettings;
import com.ulfsvel.wallet.btc.entity.*;
import com.ulfsvel.wallet.common.entity.UnencryptedWallet;
import com.ulfsvel.wallet.common.entity.Wallet;
import com.ulfsvel.wallet.common.entity.WalletCredentials;
import com.ulfsvel.wallet.common.factory.WalletSecurityFactory;
import com.ulfsvel.wallet.common.response.WalletSecurityResponse;
import com.ulfsvel.wallet.common.service.WalletSecurityService;
import com.ulfsvel.wallet.common.types.WalletSecurityType;
import com.ulfsvel.wallet.common.types.WalletType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class BtcWalletService {

    private final WalletSecurityFactory walletSecurityFactory;

    private final BitcoindJsonRpcService bitcoindJsonRpcService;

    private final BitcoinSettings bitcoinSettings;

    public BtcWalletService(WalletSecurityFactory walletSecurityFactory, BitcoindJsonRpcService bitcoindJsonRpcService, BitcoinSettings bitcoinSettings) {
        this.walletSecurityFactory = walletSecurityFactory;
        this.bitcoindJsonRpcService = bitcoindJsonRpcService;
        this.bitcoinSettings = bitcoinSettings;
    }

    /**
     * https://developpaper.com/how-to-generate-bitcoin-wallet-address-in-java/
     */
    static private String adjustTo64(String s) {
        switch (s.length()) {
            case 62:
                return "00" + s;
            case 63:
                return "0" + s;
            case 64:
                return s;
            default:
                throw new IllegalArgumentException("not a valid key: " + s);
        }
    }

    /**
     * https://developpaper.com/how-to-generate-bitcoin-wallet-address-in-java/
     */
    public UnencryptedWallet generateWallet() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, JsonProcessingException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec secp256k1 = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(secp256k1);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();


        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
        String bitcoinPrivateKey = adjustTo64(ecPrivateKey.getS().toString(16)).toUpperCase();


        ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
        ECPoint ecPublicKeyW = ecPublicKey.getW();
        String ecPublicKeyWAffineX = adjustTo64(ecPublicKeyW.getAffineX().toString(16)).toUpperCase();
        String ecPublicKeyWAffineY = adjustTo64(ecPublicKeyW.getAffineY().toString(16)).toUpperCase();
        String bitcoinPublicKey = "04" + ecPublicKeyWAffineX + ecPublicKeyWAffineY;


//        MessageDigest sha = MessageDigest.getInstance("SHA-256");
//        byte[] firstSha256 = sha.digest(bitcoinPublicKey.getBytes(StandardCharset.UTF_8));
//        MessageDigest rmd = MessageDigest.getInstance("RipeMD160", "BC");
//        byte[] ripeMd160 = rmd.digest(firstSha256);
//        byte[] adjustedRipeMd160 = new byte[ripeMd160.length + 1];
//        adjustedRipeMd160[0] = bitcoinSettings.isTestnet() ? (byte) 0x6f : 0;
//        System.arraycopy(ripeMd160, 0, adjustedRipeMd160, 1, ripeMd160.length);
//        byte[] secondSha256 = sha.digest(adjustedRipeMd160);
//        byte[] thirdSha256 = sha.digest(secondSha256);
//        byte[] bitcoinPublicAddress = new byte[25];
//        System.arraycopy(adjustedRipeMd160, 0, bitcoinPublicAddress, 0, adjustedRipeMd160.length);
//        System.arraycopy(thirdSha256, 0, bitcoinPublicAddress, 21, 4);


        return new UnencryptedWallet()
                .setPrivateKey(bitcoinPrivateKey)
                .setPublicKey(bitcoinPublicKey)
                //.setPublicAddress(Base58.encode(bitcoinPublicAddress))
                .setWalletType(WalletType.BTC);
    }


    public Wallet importWalletIntoNode(Wallet wallet) throws JsonProcessingException {
        String label = "wallet-id-" + wallet.getId().toString();
        bitcoindJsonRpcService.importPublicKey(new ImportPublicKey().setPubkey(wallet.getPublicKey()).setLabel(label));
        GetAddressResult result = bitcoindJsonRpcService.getAddressesByLabel(new GetAddress().setLabel(label));
        Set<Map.Entry<String, Object>> resultSet = result.getResult().entrySet();
        if (resultSet.size() == 0) {
            throw new RuntimeException("Unable to generate address");
        }

        String address = resultSet.iterator().next().getKey();
        wallet.setPublicAddress(address);

        return wallet;
    }


    public WalletSecurityResponse createWallet(WalletCredentials walletCredentials, WalletSecurityType walletSecurityType) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, JsonProcessingException {
        return encryptWallet(generateWallet(), walletCredentials, walletSecurityType);
    }

    private WalletSecurityResponse encryptWallet(
            UnencryptedWallet unencryptedWallet,
            WalletCredentials walletCredentials,
            WalletSecurityType walletSecurityType
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
        if (walletSecurityService.isRecoveryNotAvailable() || walletSecurityService.areRecoverCredentialsInvalid(walletCredentials)) {
            throw new RuntimeException("Error recovering wallet");
        }

        return walletSecurityService.recoverWallet(wallet, walletCredentials);
    }

    public WalletSecurityResponse changeWalletSecurityType(
            Wallet wallet,
            WalletCredentials initialWalletCredentials,
            WalletCredentials targetCredentials,
            WalletSecurityType targetWalletSecurityType
    ) {
        UnencryptedWallet unencryptedWallet = decryptWallet(wallet, initialWalletCredentials);
        return encryptWallet(unencryptedWallet, targetCredentials, targetWalletSecurityType);
    }

    public WalletSecurityResponse recoverAndEncryptWallet(
            Wallet wallet,
            WalletCredentials initialWalletCredentials,
            WalletCredentials targetCredentials,
            WalletSecurityType targetWalletSecurityType
    ) {
        UnencryptedWallet unencryptedWallet = recoverWallet(wallet, initialWalletCredentials);
        return encryptWallet(unencryptedWallet, targetCredentials, targetWalletSecurityType);
    }

    public String unlockAndTransferFounds(Wallet wallet, WalletCredentials walletCredentials, String to, String amount) throws Exception {
        UnencryptedWallet unencryptedWallet = decryptWallet(wallet, walletCredentials);
        return transferFounds(unencryptedWallet, to, amount);
    }

    public String getBalance(Wallet wallet) throws IOException {
        List<UnspentTx> result = bitcoindJsonRpcService.getBalance(
                new GetBalance()
                        .addAddresses(wallet.getPublicAddress())
                        .setMinconf(1)
        ).getResult();

        Double balance = 0D;
        for (UnspentTx unspentTx : result) {
            if (unspentTx.getSafe()) {
                balance += unspentTx.getAmount();
            }
        }

        return balance.toString();
    }

    private String transferFounds(UnencryptedWallet wallet, String to, String amount) throws Exception {
        double requiredAmount = Double.parseDouble(amount) + 0.0005;
        double amountFoundForTransaction = 0D;

        CreateTransaction createTransaction = new CreateTransaction()
                .putOutput(to, amount)
                .setReplaceable(Boolean.TRUE);

        List<UnspentTx> result = bitcoindJsonRpcService.getBalance(
                new GetBalance()
                        .addAddresses(wallet.getPublicAddress())
                        .setMinconf(1)
        ).getResult();

        for (UnspentTx unspentTx : result) {
            if (unspentTx.getSafe()) {
                createTransaction.putInput(new UnspentTxForFounding(unspentTx));
                amountFoundForTransaction += unspentTx.getAmount();
                if (amountFoundForTransaction >= requiredAmount) {
                    break;
                }
            }
        }

        if (amountFoundForTransaction < requiredAmount) {
            throw new RuntimeException("Insufficient funds");
        }

        double returnAmount = amountFoundForTransaction - requiredAmount;
        createTransaction.putOutput(wallet.getPublicAddress(), String.format("%.12f", returnAmount));
        CreateTransactionResult createTransactionResult = bitcoindJsonRpcService.createRawTransaction(
                createTransaction
        );

        SignTransactionResult signTransactionResult = bitcoindJsonRpcService.signTransaction(
                new SignTransaction()
                        .setHexString(createTransactionResult.getResult())
                        .putPrivateKey(wallet.getPrivateKey())
        );

        SendTransactionResult sendTransactionResult = bitcoindJsonRpcService.sendTransaction(
                new SendTransaction()
                        .setHexString(signTransactionResult.getHex())
        );

        return bitcoindJsonRpcService.decodeTransaction(
                new DecodeTransaction()
                        .setHexString(sendTransactionResult.getResult())
        );
    }


}
