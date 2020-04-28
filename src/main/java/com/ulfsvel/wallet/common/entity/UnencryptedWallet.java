package com.ulfsvel.wallet.common.entity;


import com.ulfsvel.wallet.common.types.WalletType;

public class UnencryptedWallet {

    private long id;
    private String privateKey;
    private String publicKey;
    private String publicAddress;
    private WalletType walletType;
    private User user;
    private final String lastKnownBalance;

    public UnencryptedWallet() {
        lastKnownBalance = "0";
    }

    public UnencryptedWallet(Wallet wallet, String decryptedPrivateKey) {
        id = wallet.getId();
        privateKey = decryptedPrivateKey;
        publicKey = wallet.getPublicKey();
        publicAddress = wallet.getPublicAddress();
        walletType = wallet.getWalletType();
        user = wallet.getUser();
        lastKnownBalance = wallet.getLastKnownBalance();
    }

    public long getId() {
        return id;
    }

    public UnencryptedWallet setId(long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public UnencryptedWallet setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public UnencryptedWallet setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public UnencryptedWallet setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
        return this;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public UnencryptedWallet setWalletType(WalletType walletType) {
        this.walletType = walletType;
        return this;
    }

    public String getLastKnownBalance() {
        return lastKnownBalance;
    }
}
