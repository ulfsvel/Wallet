package com.ulfsvel.wallet.common.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ulfsvel.wallet.common.types.WalletSecurityType;
import com.ulfsvel.wallet.common.types.WalletType;

import javax.persistence.*;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    private String encryptedPrivateKey;

    private String publicKey;
    private String publicAddress;
    private WalletSecurityType walletSecurityType;
    private WalletType walletType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Wallet() {
    }

    public Wallet(UnencryptedWallet wallet) {
        id = wallet.getId();
        publicKey = wallet.getPublicKey();
        publicAddress = wallet.getPublicAddress();
        walletType = wallet.getWalletType();
        user = wallet.getUser();
    }

    public Long getId() {
        return id;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public Wallet setWalletType(WalletType walletType) {
        this.walletType = walletType;
        return this;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public Wallet setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public Wallet setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public Wallet setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
        return this;
    }

    public WalletSecurityType getWalletSecurityType() {
        return walletSecurityType;
    }

    public Wallet setWalletSecurityType(WalletSecurityType walletSecurityType) {
        this.walletSecurityType = walletSecurityType;
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
