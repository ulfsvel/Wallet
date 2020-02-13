package com.ulfsvel.wallet.common.entiry;


public class UnencryptedWallet {

    private long id;
    private String privateKey;
    private String publicKey;
    private String publicAddress;
    private int walletType;

    public UnencryptedWallet() {
    }

    public UnencryptedWallet(Wallet wallet, String decryptedPrivateKey) {
        id = wallet.getId();
        privateKey = decryptedPrivateKey;
        publicKey = wallet.getPublicKey();
        publicAddress = wallet.getPublicAddress();
        walletType = wallet.getWalletType();
    }

    public long getId() {
        return id;
    }

    public UnencryptedWallet setId(long id) {
        this.id = id;
        return this;
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

    public int getWalletType() {
        return walletType;
    }

    public UnencryptedWallet setWalletType(int walletType) {
        this.walletType = walletType;
        return this;
    }
}
